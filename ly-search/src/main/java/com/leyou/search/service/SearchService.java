package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsRepository repository;

    public Goods buildGoods(Spu spu){
        Long spuId = spu.getId();

        // 查询分类;
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
        );
        if (CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FIND); // 其实这个异常不用抛也可以，因为queryCategoryByIds提供方已经抛了;
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());

        // 查询品牌;
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if(brand == null){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }

        // 搜索字段
        String all = spu.getTitle() + StringUtils.join(names, " ") + brand.getName();

        // 查询sku;
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnums.GOODS_SKU_NOT_FOUND);
        }

        // 对sku进行处理，只要部分字段; 动态对象 即是 动态map;
        List<Map<String, Object>> skus = new ArrayList<>();

        // 价格集合
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList){
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.substringBefore(sku.getImages(), ",")); // 只要第一张图片即可;
            skus.add(map);

            // 处理价格
            priceList.add(sku.getPrice());
        }
        /*
        * 1. 要把规格参数 封装成 map 放到索引库里；
        * 2. 索引 是支持 object 字段类型的；
        * 3. map key 来自 queryParamList;
        * value 来自 queryDetailById;
        * 4. 数字参数，需要处理成 段 存入库;
        * 5. 我的表数据商品详情表 与视频教学的不一样，我的数据结构，自带有name等规格的属性字段，
        * 所以我这里就不用 再查 queryParamList 取 name.
        * 并且 我的商品详情表的 参数字段 specifications 包含了 通用参数和特有参数;
        * 6. IndexParam 规格参数字段 说明
        private String k;
        private Boolean searchable;
        private Boolean global;  // 是否是通用规格参数
        private String v;
        private Boolean numerical; // 如果值是数字，则要处理成“段”，比如当前spu的屏幕像素是3500，
        * 则要根据 分段条件，把值处理成 3000-4000，则入库map是 {"屏幕像素":"3000-4000"},
        * 然后 搜索 3000-4000像素时，匹配到 这个spu；
        private String unit;
        private List<String> options; // 特有的规格参数，直接整个数组存入索引库即可，比如 直接入库map {"内存":["128","64"]},
        * 因为存储单位是 spu,搜索内存 64 或者 128 那都是 搜索出 这个spu；
        *
        * 7. 综上所述，我的规格参数封装代码，与视频教程是 不一样的，我的是更简单;
        * 但 SpuDetailParam 是双层数组嵌套，要双重遍历;
        * */

        // 索引库规格参数map
        Map<String, Object> specs = new HashMap<>();

        // 查询规格参数
        List<SpecParam> params = specClient.queryParamList(null, spu.getCid3(), true);
        if(CollectionUtils.isEmpty(params)){
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOND);
        }

        // 查询商品详情;
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);


        //获取通用的规格参数值
        Map<String,Object> genericSpecMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(),new TypeReference<Map<String,Object>>(){});

        //获取特殊的规格参数值
        Map<String,List<Object>> specialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(),new TypeReference<Map<String,List<Object>>>(){});

        params.forEach(param->{
            //判断规格参数的类型,是否是通用的规格参数
            if (param.getGeneric()) {
                //如果是通用类型的参数,从genericSpecMap中获取参数值
                String value = String.valueOf(genericSpecMap.get(param.getId().toString()));
                //判断是否是数值类型,返回一个区间
                if (param.getNumeric()) {
                    value = chooseSegment(value, param);
                }
                specs.put(param.getName(), value);
            } else {
                //如果是特殊类型的参数,从specialSpecMap中获取参数值
                List<Object> value =specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(), value);
            }
        });


        // 构建goods对象
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all); //  搜索字段，包含标题，分类，品牌，规格等;
        goods.setPrice(priceList); //  所有sku的价格集合;
        goods.setSpecs(specs); //  所有的可搜索的规格参数;
        goods.setSkus(JsonUtils.serialize(skus)); //  所有sku的集合的json格式;
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }



    private String chooseSegment(String value, SpecParam p){
        double val = NumberUtils.toDouble(value);
        String result = "其他";

        // 保存数值段;
        for(String segment : p.getSegments().split(",")){
            String[] segs = segment.split("-");

            // 获取数值范围;
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }

            // 判断是否在范围内 , 比如 0-1.0,1.0-1.5,1.5-2.0
            if(val >= begin && val < end){
                if(segs.length == 1){ // 只有一个数，且 val > begin 则 肯定是 以上
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){ // 有2个数，且 第一个数是 0 ，则是第一段 0-1，所以是 xxx 以下；
                    result = segs[1] + p.getUnit() + "以下";
                }else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage() - 1; //es是从0开始;
        int size = request.getSize();

        // 创建查询构建器;
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 0. 结果过滤;
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));

        // 1. 分页
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 2. 查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", request.getKey()));

        // 3. 启动查询
        Page<Goods> result = repository.search(queryBuilder.build());

        // 4. 解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList  = result.getContent();
        return new PageResult<>(total, totalPage, goodsList);
    }

    public void createOrUpdateIndex(Long spuId) {
        // 查询spu
        Spu spu = goodsClient.querySpuById(spuId);

        // 构建goods;
        Goods goods = buildGoods(spu);

        // 存入索引库;如果库已存在，则会修改；
        repository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        repository.deleteById(spuId);
    }
}

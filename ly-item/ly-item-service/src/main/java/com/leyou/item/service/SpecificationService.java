/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SpecificationService
 * Author:   Administrator
 * Date:     2021/5/9 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2021/5/9
 * @since 1.0.0
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper groupMapper;
    @Autowired
    private SpecParamMapper paramMapper;


    public List<SpecGroup> queryGroupByCid(Long cid) {
		SpecGroup group = new SpecGroup();
		group.setCid(cid);
		List<SpecGroup> list =  groupMapper.select(group);
		if(CollectionUtils.isEmpty(list)){
			throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOND);//商品规格组不存在
		}
		return list;
    }
	
    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
		SpecParam param = new SpecParam();
		param.setGroupId(gid);
		param.setCid(cid);
		param.setSearching(searching);
		List<SpecParam> list =  paramMapper.select(param);
		if(CollectionUtils.isEmpty(list)){
			throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOND);//商品规格参数不存在
		}
		return list;
    }


    public List<SpecGroup> queryListByCid(Long cid) {
    	// 查询规格组：
		List<SpecGroup> specGroups = queryGroupByCid(cid);

		// 查询当前分类下的参数;
		List<SpecParam> specParams = queryParamList(null, cid, null);

    	// 转map
		Map<Long, List<SpecParam>> map = new HashMap<>();
		for (SpecParam param : specParams) {
			if(!map.containsKey(param.getGroupId())){
				map.put(param.getGroupId(), new ArrayList<>());
			}
			map.get(param.getGroupId()).add(param);
		}
		for (SpecGroup specGroup : specGroups){
			specGroup.setParams(map.get(specGroup.getId()));
		}
		return specGroups;
    }
}













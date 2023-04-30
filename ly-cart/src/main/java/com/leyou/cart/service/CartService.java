package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";
    public void addCart(Cart cart) {
        // 获取用户信息;
        UserInfo user = UserInterceptor.getUser();
        // key
        String key = KEY_PREFIX + user.getId();
        // hashKy
        String hashKey = cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        // 判断当前购物商品，是否存在;
        if(operation.hasKey(hashKey)){
            // 存在，修改数量;
            String json = operation.get(hashKey).toString();
            Cart cacheCart = JsonUtils.parse(json, Cart.class);
            cacheCart.setNum(cacheCart.getNum() + cart.getNum());
            // 写回redis;
            operation.put(hashKey, JsonUtils.serialize(cacheCart));
        }else {
            // 否，新增;
            operation.put(hashKey, JsonUtils.serialize(cart));
        }
    }

    /**
     * V2 解决重复代码
     * @param cart
     */
    public void addCartV2(Cart cart) {
        // 获取用户信息;
        UserInfo user = UserInterceptor.getUser();
        // key
        String key = KEY_PREFIX + user.getId();
        // hashKy
        String hashKey = cart.getSkuId().toString();
        // 记录num
        Integer addNum = cart.getNum();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        // 判断当前购物商品，是否存在;
        if(operation.hasKey(hashKey)) {
            // 存在，修改数量;
            String json = operation.get(hashKey).toString();
            cart = JsonUtils.parse(json, Cart.class);
            // 旧num + 新增num
            cart.setNum(cart.getNum() + addNum);
        }
            // 写回redis;
            operation.put(hashKey, JsonUtils.serialize(cart));
    }


    public List<Cart> queryCartList() {
        // 获取登录用户;
        UserInfo user = UserInterceptor.getUser();
        // key
        String key = KEY_PREFIX + user.getId();
        if( !redisTemplate.hasKey(key)){
            // Key不存在，返回404;
            throw new LyException(ExceptionEnums.CART_NOT_FOUND);
        }
        // redis key Oper;
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        // Stream 把 List<Object> 转成 List<Cart>;
        List<Object> values = operation.values();
        Stream<Cart> cartStream = values.stream().map(obj -> JsonUtils.parse(obj.toString(), Cart.class));
        List<Cart> collect = cartStream.collect(Collectors.toList());

        return collect;
    }

    public void updateNum(Long skuId, Integer num) {
        // 获取登录用户;
        UserInfo user = UserInterceptor.getUser();
        // key
        String key = KEY_PREFIX + user.getId();
        String hashkey = skuId.toString();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        // 判断是否存在
        if(!operation.hasKey(skuId.toString())){
            throw new LyException(ExceptionEnums.CART_NOT_FOUND);
        }
        // 查询;
        Cart cart = JsonUtils.parse(operation.get(skuId.toString()).toString(), Cart.class);
        cart.setNum(num);
        // 写回redis;
        operation.put(hashkey, JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
        // 获取登录用户;
        UserInfo user = UserInterceptor.getUser();
        // key
        String key = KEY_PREFIX + user.getId();

        // 删除
        redisTemplate.opsForHash().delete(key, skuId.toString());
    }
}


















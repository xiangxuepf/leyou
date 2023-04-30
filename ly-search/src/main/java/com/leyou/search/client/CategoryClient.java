package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

    // feign可以不用 ResponseEntity，因为ResponseEntity只是给mvcy一个状态标识，其实返回内容还是List<>；
//    @GetMapping("category/list/ids")
//    List<Category> queryCategoryByIds(@RequestParam("ids")List<Long> ids);
}

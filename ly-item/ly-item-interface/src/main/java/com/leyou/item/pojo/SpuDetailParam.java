package com.leyou.item.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SpuDetailParam {
    private String group;
    private List<IndexParam> params;
}

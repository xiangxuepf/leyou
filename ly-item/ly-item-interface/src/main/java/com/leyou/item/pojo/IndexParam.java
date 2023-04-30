package com.leyou.item.pojo;

import lombok.Data;

import java.util.List;

@Data
public class IndexParam {
    private String k;
    private Boolean searchable;
    private Boolean global;  // 是否是通用规格参数
    private String v;
    private Boolean numerical;
    private String unit;
    private List<String> options;

}

package com.wms.entity;

import lombok.Data;

@Data
public class Goods {
    private int id;
    private String name;
    private int storage;
    private int goodstype;
    private int count;
    private String remark;
}

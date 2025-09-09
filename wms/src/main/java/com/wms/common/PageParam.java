package com.wms.common;

import lombok.Data;

import java.util.HashMap;

@Data
public class PageParam {
    private int pageNum = 1;
    private int pageSize = 10;

    private HashMap param = new HashMap();
}

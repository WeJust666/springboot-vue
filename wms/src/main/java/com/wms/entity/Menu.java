package com.wms.entity;

import lombok.Data;

@Data
public class Menu {
    private int id;
    private String menucode;
    private String menuname;
    private String menulevel;
    private String menuparentcode;
    private String menuclick;
    private String menuright;
    private String menucomponent;
    private String menuicon;
}

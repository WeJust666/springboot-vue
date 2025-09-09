package com.wms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Record {
    private int id;
    private int goods;
    private int userid;
    private int adminId;
    private int count;
    private LocalDateTime createtime;
    private String remark;
    @TableField(exist = false)
    private String action;
}

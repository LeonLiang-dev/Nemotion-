package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_random_step")
public class ExamRandomStep implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String itemid;
    private String name;
    private Integer sort;
    private Integer subnum;
    private Integer subpoint;
    private String tiptype;
    private String typeid;
    private String knowid;
    private String pcontent;
}

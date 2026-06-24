package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_know")
public class Know implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;
    private String parentid;
    private String treecode;
    private String comments;
    private Integer sort;
    private String state;
    private String cuser;
    private String muser;
    private String ctime;
    private String utime;
}

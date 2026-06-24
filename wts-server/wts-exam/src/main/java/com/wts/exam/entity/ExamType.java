package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_exam_type")
public class ExamType implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String treecode;
    private String comments;
    private String name;
    private String ctime;
    private String utime;
    private String state;
    private String cuser;
    private String muser;
    private String parentid;
    private Integer sort;
    private String domain;
    private String mngpop;
    private String adjudgepop;
    private String querypop;
    private String superpop;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_exam_pop")
public class ExamPop implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String typeid;
    private String username;
    private String userid;
    private String funtype;
}

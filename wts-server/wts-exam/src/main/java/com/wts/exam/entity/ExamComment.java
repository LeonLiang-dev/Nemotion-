package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_comment")
public class ExamComment implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String bandid;
    private String type;
    private String text;
    private String cuser;
    private String cusername;
}

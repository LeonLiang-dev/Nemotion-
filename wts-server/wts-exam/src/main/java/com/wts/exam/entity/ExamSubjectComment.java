package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_subject_comment")
public class ExamSubjectComment implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String ctime;
    private String cusername;
    private String pstate;
    private String cuser;
    private String pcontent;
    private String text;
    private String subjectid;
}

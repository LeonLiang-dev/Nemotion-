package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_subject_version")
public class ExamSubjectVersion implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String ctime;
    private String cusername;
    private String cuser;
    private String pstate;
    private String pcontent;
    private String tipstr;
    private String tipnote;
    private String tiptype;
    private String subjectid;
    private String answered;
}

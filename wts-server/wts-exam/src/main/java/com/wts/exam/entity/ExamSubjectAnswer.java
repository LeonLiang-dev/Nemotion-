package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_subject_answer")
public class ExamSubjectAnswer implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String versionid;
    private String answer;
    private String answernote;
    private String rightanswer;
    private Integer sort;
    private Integer pointweight;
    private Integer groupno;
    private String uuid;
    private String pstate;
    private String pcontent;
    private String cuser;
    private String cusername;
    private String ctime;
}

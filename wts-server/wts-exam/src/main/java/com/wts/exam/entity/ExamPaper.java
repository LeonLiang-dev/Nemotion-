package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_paper")
public class ExamPaper implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String examtypeid;
    private String ctime;
    private String etime;
    private String cusername;
    private String cuser;
    private String eusername;
    private String euser;
    private String pstate;
    private String pcontent;
    private String name;
    private Integer subjectnum;
    private Integer pointnum;
    private Integer completetnum;
    private Integer avgpoint;
    private Integer toppoint;
    private Integer lowpoint;
    private Integer advicetime;
    private String papernote;
    private Integer booknum;
    private String uuid;
    private String knowid;
}

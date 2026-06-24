package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_subject")
public class ExamSubject implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String typeid;
    private String versionid;
    private String pstate;
    private String materialid;
    private Integer praisenum;
    private Integer commentnum;
    private Integer analysisnum;
    private Integer donum;
    private Integer rightnum;
    private String uuid;
    private String introduction;
    private Integer level;
    private Integer point;
}

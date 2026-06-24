package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_paper_chapter")
public class ExamPaperChapter implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String stype;
    private String ptype;
    private Integer initpoint;
    private String subjecttypeid;
    private Integer subjectnum;
    private Integer subjectpoint;
    private String name;
    private String textnote;
    private String parentid;
    private String paperid;
    private Integer sort;
    private String treecode;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_paper_subject")
public class ExamPaperSubject implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String versionid;
    private String subjectid;
    private String chapterid;
    private Integer sort;
    private Integer point;
    private String paperid;
}

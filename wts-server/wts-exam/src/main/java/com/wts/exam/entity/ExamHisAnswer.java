package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_his_answer")
public class ExamHisAnswer implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String backversion;
    private String answeruuid;
    private String subjectuuid;
    private String paperuuid;
    private String roomuuid;
    private String title;
    private String rightanswer;
    private Integer sort;
}

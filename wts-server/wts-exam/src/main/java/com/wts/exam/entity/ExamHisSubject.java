package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_his_subject")
public class ExamHisSubject implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String backversion;
    private String title;
    private String type;
    private String subjectuuid;
    private String paperuuid;
    private String roomuuid;
    private Integer sort;
    private Integer point;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_card")
public class ExamCard implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String paperid;
    private String roomid;
    private String userid;
    private Float point;
    private String adjudgeusername;
    private String adjudgeuser;
    private String adjudgetime;
    private String starttime;
    private String endtime;
    private String pstate;
    private String pcontent;
    private Integer completenum;
    private Integer allnum;
    private String overtime;
    private String resultstype;
    private String submittime;
    private String roomuuid;
    private String paperuuid;
    private String useruuid;
    private String adjudgeuseruuid;
    private String statistical;
}

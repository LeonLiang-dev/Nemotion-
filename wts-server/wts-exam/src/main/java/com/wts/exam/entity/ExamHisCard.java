package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_his_card")
public class ExamHisCard implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String backversion;
    private String username;
    private String roomname;
    private String papername;
    private String useruuid;
    private String roomuuid;
    private String paperuuid;
    private String pstate;
    private Float allpoint;
    private String alltime;
    private String resultstype;
    private String cuser;
    private String ctime;
    private Integer completenum;
    private Integer allnum;
    private String statistical;
}

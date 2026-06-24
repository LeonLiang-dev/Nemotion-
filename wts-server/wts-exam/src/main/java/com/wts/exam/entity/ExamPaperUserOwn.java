package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_paper_userown")
public class ExamPaperUserOwn implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String ctime;
    private String cusername;
    private String pstate;
    private String cuser;
    private String pcontent;
    private String paperid;
    private String papername;
    private String roomid;
    private String roomname;
    private String cardid;
    private String modeltype;
    private Float score;
    private Float rpcent;
}

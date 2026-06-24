package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_his_card_answer")
public class ExamHisCardAnswer implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String backversion;
    private String cardid;
    private String subjectuuid;
    private String answeruuid;
    private String valstr;
    private String cuser;
    private String ctime;
    private String pstate;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_know_cardsta")
public class KnowCardSta implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String knowid;
    private String cardid;
    private Integer allnum;
    private Integer rightnum;
    private String cuser;
    private String ctime;
}

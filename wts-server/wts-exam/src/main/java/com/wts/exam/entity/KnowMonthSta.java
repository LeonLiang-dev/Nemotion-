package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_know_monthsta")
public class KnowMonthSta implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String knowid;
    private String months;
    private Integer allnum;
    private Integer rightnum;
    private String cuser;
    private String ctime;
}

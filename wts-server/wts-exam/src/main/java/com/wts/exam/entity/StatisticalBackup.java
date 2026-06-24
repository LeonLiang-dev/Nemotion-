package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_statistical_backup")
public class StatisticalBackup implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String roomid;
    private String paperid;
    private String userid;
    private String cardid;
    private Float point;
    private Integer completenum;
    private Integer allnum;
    private String ctime;
}

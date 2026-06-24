package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_statistical_room")
public class StatisticalRoom implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String roomid;
    private String key1;
    private String val1;
    private String ctime;
}

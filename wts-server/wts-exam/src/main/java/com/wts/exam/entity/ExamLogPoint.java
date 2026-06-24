package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_log_point")
public class ExamLogPoint implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String userid;
    private String paperid;
    private String papername;
    private String roomid;
    private String roomname;
    private Float point;
    private String ctime;
}

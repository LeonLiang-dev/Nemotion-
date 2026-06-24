package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_room_paper")
public class ExamRoomPaper implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String roomid;
    private String paperid;
    private String name;
    private Float passpoint;
}

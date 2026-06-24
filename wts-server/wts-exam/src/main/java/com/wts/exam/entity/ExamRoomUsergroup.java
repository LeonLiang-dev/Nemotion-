package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_room_usergroup")
public class ExamRoomUsergroup implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String roomid;
    private String objid;
    private String objtype;
    private String objname;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_task_mirror")
public class TaskMirror implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String ctime;
    private String pstate;
    private String pcontent;
    private String cardid;
    private String versionid;
    private String answerid;
    private String val;
    private String type;
}

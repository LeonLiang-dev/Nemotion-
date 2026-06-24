package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_docfile")
public class SysDocfile implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String uuid;
    private String name;
    private Long size;
    private String type;
    private String ext;
    private String storepath;
    private String cuser;
    private String cusername;
    private String ctime;
    private String pstate;
}

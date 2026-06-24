package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_know_resource")
public class KnowResource implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String knowid;
    private String resourceid;
    private String resourcetype;
    private String cuser;
    private String ctime;
}

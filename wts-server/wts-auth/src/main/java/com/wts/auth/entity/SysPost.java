package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_post")
public class SysPost implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String ctime;
    private String etime;
    private String cusername;
    private String cuser;
    private String eusername;
    private String euser;
    private String pstate;
    private String organizationid;
    private String name;
    /** 是否继承: 0否, 1是 */
    private String extendis;
    private String uuid;
    private String sourceid;
}

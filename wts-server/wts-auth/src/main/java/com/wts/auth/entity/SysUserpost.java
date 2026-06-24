package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_userpost")
public class SysUserpost implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String userid;
    private String postid;
}

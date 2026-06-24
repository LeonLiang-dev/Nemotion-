package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_user")
public class SysUser implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;
    private String loginname;
    private String password;
    private String comments;
    /** 用户类型: 1系统用户, 2其他, 3超级管理员 */
    private String type;
    private String ctime;
    private String utime;
    private String cuser;
    private String muser;
    /** 状态: 1正常, 0禁用 */
    private String state;
    private String logintime;
    private String imgid;
    private String uuid;

    @TableField(exist = false)
    private String ip;
}

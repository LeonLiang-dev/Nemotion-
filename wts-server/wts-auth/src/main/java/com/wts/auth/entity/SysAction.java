package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_action")
public class SysAction implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    /** 权限标识 (如 user/list) */
    private String authkey;
    private String name;
    private String comments;
    private String ctime;
    private String utime;
    private String cuser;
    private String muser;
    private String state;
    /** 是否检查: 1是, 0否 */
    private String checkis;
    /** 是否需要登录: 1是, 0否 */
    private String loginis;
}

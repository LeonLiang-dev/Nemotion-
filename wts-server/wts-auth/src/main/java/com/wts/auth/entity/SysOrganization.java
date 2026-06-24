package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_organization")
public class SysOrganization implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String treecode;
    private String comments;
    private String name;
    private String ctime;
    private String utime;
    private String state;
    private String cuser;
    private String muser;
    /** 父组织ID, NONE为根 */
    private String parentid;
    private Integer sort;
    /** 组织类型: 1科室, 2班组, 3队组, 0其他 */
    private String type;
    private String appid;
    private String uuid;
}

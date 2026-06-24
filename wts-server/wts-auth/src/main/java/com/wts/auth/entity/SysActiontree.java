package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_actiontree")
public class SysActiontree implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private Integer sort;
    /** 父节点ID, NONE为根 */
    private String parentid;
    private String name;
    /** 树编码 (全路径拼接) */
    private String treecode;
    private String comments;
    /** 类型: 1分类, 2菜单 */
    private String type;
    private String ctime;
    private String utime;
    private String cuser;
    private String uuser;
    private String state;
    /** 关联的权限ID */
    private String actionid;
    private String domain;
    private String icon;
    private String imgid;
    private String params;
}

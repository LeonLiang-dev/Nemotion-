package com.wts.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("alone_auth_pop")
public class SysPop implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    /** 授权类型: 1人, 2组织, 3岗位 */
    private String poptype;
    /** 授权对象ID */
    private String oid;
    /** 授权对象名称 */
    private String oname;
    /** 目标业务类型 */
    private String targettype;
    /** 目标业务ID */
    private String targetid;
    private String targetname;
    private String ctime;
    private String cusername;
    private String cuser;
    private String pstate;
    private String pcontent;
}

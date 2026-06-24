package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_share_user")
public class ShareUser implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String nodeid;
    private String userid;
    private String state;
    private String cuser;
    private String ctime;
}

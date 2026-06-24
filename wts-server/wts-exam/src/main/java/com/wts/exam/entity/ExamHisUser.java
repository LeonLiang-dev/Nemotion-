package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_his_user")
public class ExamHisUser implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String backversion;
    private String useruuid;
    private String name;
    private String loginname;
    private String orguuid;
    private String orgname;
    private String postuuid;
    private String postnames;
    private String roomuuid;
}

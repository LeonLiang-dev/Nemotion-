package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_material")
public class ExamMaterial implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String uuid;
    private String title;
    private String text;
    private String pstate;
    private String cuser;
    private String euser;
    private String ctime;
    private String etime;
}

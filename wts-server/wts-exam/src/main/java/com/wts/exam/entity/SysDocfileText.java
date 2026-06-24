package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_docfile_text")
public class SysDocfileText implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String docid;
    private String text;
    private String ctime;
}

package com.wts.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("wts_exam_stat")
public class ExamStat implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String cuser;
    private Integer papernum;
    private Integer subjectnum;
    private Integer errorsubnum;
    private Integer testnum;
}

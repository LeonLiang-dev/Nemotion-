package com.wts.exam.dto;

import lombok.Data;
import java.util.List;

@Data
public class CardSubmitDTO {
    private List<CardAnswerDTO> answers;
}

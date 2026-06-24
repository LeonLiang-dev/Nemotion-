package com.wts.exam.service.impl;

import com.wts.exam.entity.ExamCardAnswer;
import com.wts.exam.entity.ExamSubjectAnswer;
import com.wts.exam.enums.TipType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardAnswerGrader {

    public int calculateWeight(String tipType, List<ExamCardAnswer> answers, List<ExamSubjectAnswer> correctAnswers) {
        if (answers.isEmpty()) return 0;
        TipType tt = TipType.fromCode(tipType);
        if (tt == null) return 0;

        switch (tt) {
            case SELECT:
            case JUDGE:
                for (ExamCardAnswer ca : answers) {
                    if ("true".equals(ca.getValstr())) {
                        for (ExamSubjectAnswer sa : correctAnswers) {
                            if (sa.getId().equals(ca.getAnswerid()) && "1".equals(sa.getRightanswer())) {
                                return 100;
                            }
                        }
                    }
                }
                return 0;
            case CHECKBOX:
                boolean allCorrect = true;
                for (ExamSubjectAnswer sa : correctAnswers) {
                    if ("1".equals(sa.getRightanswer())) {
                        boolean found = answers.stream().anyMatch(
                                ca -> ca.getAnswerid().equals(sa.getId()) && "true".equals(ca.getValstr()));
                        if (!found) { allCorrect = false; break; }
                    }
                }
                if (allCorrect) {
                    for (ExamCardAnswer ca : answers) {
                        if ("true".equals(ca.getValstr())) {
                            boolean isCorrect = correctAnswers.stream().anyMatch(
                                    sa -> sa.getId().equals(ca.getAnswerid()) && "1".equals(sa.getRightanswer()));
                            if (!isCorrect) { allCorrect = false; break; }
                        }
                    }
                }
                return allCorrect ? 100 : 0;
            case VACANCY:
                int totalWeight = 0;
                int matchedWeight = 0;
                for (ExamSubjectAnswer sa : correctAnswers) {
                    int w = sa.getPointweight() != null && sa.getPointweight() > 0 ? sa.getPointweight() : 100;
                    totalWeight += w;
                    for (ExamCardAnswer ca : answers) {
                        if (ca.getAnswerid().equals(sa.getId())) {
                            String[] alternatives = sa.getAnswer().split("\\|");
                            for (String alt : alternatives) {
                                if (alt.trim().equalsIgnoreCase(ca.getValstr() != null ? ca.getValstr().trim() : "")) {
                                    matchedWeight += w;
                                    break;
                                }
                            }
                        }
                    }
                }
                return totalWeight > 0 ? matchedWeight * 100 / totalWeight : 0;
            case INTERLOCUTION:
            case FILEUP:
                return 0;
            default:
                return 0;
        }
    }
}

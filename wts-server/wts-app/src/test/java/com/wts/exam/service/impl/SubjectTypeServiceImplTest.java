package com.wts.exam.service.impl;

import com.wts.exam.entity.ExamSubjectType;
import com.wts.exam.mapper.ExamSubjectTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubjectTypeServiceImplTest {

    @Mock
    private ExamSubjectTypeMapper typeMapper;

    private SubjectTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SubjectTypeServiceImpl(typeMapper);
    }

    @Test
    void createFillsLegacyRequiredPermissionColumns() {
        ExamSubjectType input = new ExamSubjectType();
        input.setName("数学");

        ExamSubjectType created = service.create(input, "teacher-1");

        ArgumentCaptor<ExamSubjectType> typeCaptor = ArgumentCaptor.forClass(ExamSubjectType.class);
        verify(typeMapper).insert(typeCaptor.capture());
        ExamSubjectType type = typeCaptor.getValue();
        assertEquals(created, type);
        assertNotNull(type.getId());
        assertEquals(type.getId(), type.getTreecode());
        assertEquals("1", type.getState());
        assertEquals("teacher-1", type.getCuser());
        assertEquals("teacher-1", type.getMuser());
        assertEquals("NONE", type.getParentid());
        assertEquals(1, type.getSort());
        assertEquals("1", type.getReadpop());
        assertEquals("1", type.getWritepop());
    }
}

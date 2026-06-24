package com.wts.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wts.common.exception.BizException;
import com.wts.exam.entity.ExamRoom;
import com.wts.exam.entity.ExamRoomUser;
import com.wts.exam.mapper.ExamRoomUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomParticipationPolicy {
    private static final String PUBLIC_ROOM = "1";

    private final ExamRoomUserMapper roomUserMapper;

    public void requireParticipant(ExamRoom room, String userId) {
        if (!canParticipate(room, userId)) {
            throw BizException.forbidden("无权进入此答题室");
        }
    }

    public boolean canParticipate(ExamRoom room, String userId) {
        if (isPublicRoom(room)) {
            return true;
        }
        if (userId == null || userId.isBlank() || room == null || room.getId() == null) {
            return false;
        }
        Long count = roomUserMapper.selectCount(new LambdaQueryWrapper<ExamRoomUser>()
                .eq(ExamRoomUser::getRoomid, room.getId())
                .eq(ExamRoomUser::getUserid, userId));
        return count != null && count > 0;
    }

    public boolean canParticipate(ExamRoom room, Set<String> assignedRoomIds) {
        if (isPublicRoom(room)) {
            return true;
        }
        return room != null && assignedRoomIds.contains(room.getId());
    }

    public Set<String> getAssignedRoomIds(String userId) {
        if (userId == null || userId.isBlank()) {
            return Collections.emptySet();
        }
        return roomUserMapper.selectList(new LambdaQueryWrapper<ExamRoomUser>()
                        .eq(ExamRoomUser::getUserid, userId))
                .stream()
                .map(ExamRoomUser::getRoomid)
                .collect(Collectors.toSet());
    }

    private boolean isPublicRoom(ExamRoom room) {
        String publicType = room != null ? room.getPublictype() : null;
        return publicType == null || publicType.isBlank() || PUBLIC_ROOM.equals(publicType);
    }
}

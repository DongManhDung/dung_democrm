package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class ActivityStatisticsResponse {

    // Tổng Activity
    private Long totalActivities;

    // Theo loai Activity
    private Map<ActivityType, Long> activitiesByType;

    // Theo trạng thái
    private Map<ActivityStatus, Long> activitiesByStatus;

    // Quá hạn
    private Long overdueActivities;

    // Đã hoàn thành
    private Long completedActivities;
}

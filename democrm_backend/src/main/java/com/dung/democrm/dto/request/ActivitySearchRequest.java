package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.ActivityStatus;
import com.dung.democrm.common.enums.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivitySearchRequest {
    private Long leadId;

    private Long createdById;

    private ActivityType type;

    private ActivityStatus status;

    private String subject;

    private LocalDate dueFrom;

    private LocalDate dueTo;

    /**
    * dueDate < today && status = PENDING -> True
    * dueDate >= today || status != PENDING -> False
    * null thì không filter
    */
    private Boolean overDue;
}

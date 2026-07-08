package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.LeadPriority;
import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class LeadSearchRequest {
    private String customerName;

    private Long ownerId;

    private Long teamOwnerId;

    private LeadStatus status;

    private LeadSource source;

    private LeadPriority priority;

    private Boolean expired;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate assignedFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate assignedTo;
}

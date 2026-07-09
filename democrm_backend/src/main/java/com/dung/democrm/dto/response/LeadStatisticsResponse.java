package com.dung.democrm.dto.response;

import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class LeadStatisticsResponse {
    private Long totalLeads;

    private Map<LeadStatus, Long> leadStatusStatistics;

    private Map<LeadSource, Long> leadSourceStatistics;

    private Long expiringSoonLeads;

    private Long expiredLeads;

}

package com.dung.democrm.common.constant;

import com.dung.democrm.common.enums.LeadStatus;

import java.util.Set;

public final class LeadConstants {
    private LeadConstants(){

    }

    public static final Set<LeadStatus> ACTIVE_LEAD_STATUSES = Set.of(
            LeadStatus.NEW,
            LeadStatus.CONTACTED,
            LeadStatus.QUALIFIED,
            LeadStatus.PROPOSAL,
            LeadStatus.NEGOTIATING
    );
}

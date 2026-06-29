package com.dung.democrm.common.enums;

public enum LeadStatus {
    NEW,
    CONTACTED,
    QUALIFIED,
    DEMO,
    PROPOSAL,
    NEGOTIATION,
    WON,
    LOST,
    REASSIGNED, // Lead được chuyển cho Sales khác
    ARCHIVED // Lead không có chút hy vọng gì nữa, lưu thống kê
}

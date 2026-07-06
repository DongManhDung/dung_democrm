package com.dung.democrm.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CustomerTimelineResponse {
    private LocalDateTime occurredAt;
    private String type;
    private String title;
    private String description;
}

package com.dung.democrm.dto.request;

import com.dung.democrm.common.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {

    @NotNull(message = "Lead is required.")
    private Long leadId;

    @NotNull(message = "Activity type is required.")
    private ActivityType type;

    @NotBlank(message = "Subject is required.")
    @Size(max = 150)
    private String subject;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Due date is required.")
    private LocalDate dueDate;
}

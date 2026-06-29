package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.ActivityType;
import com.dung.democrm.entity.Activity;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.repository.ActivityRepository;
import com.dung.democrm.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ActivitySeeder {
    private final ActivityRepository activityRepository;

    private final LeadRepository leadRepository;
    private final Random random = new Random();

    public void seed(){
        if(activityRepository.count() > 0) return;

        List<Activity> activities = new ArrayList<>();

        for(Lead lead : leadRepository.findAll()){
            List<ActivityType> types = buildActivities(lead);

            LocalDateTime activityDate = lead.getCreatedAt();

            for(ActivityType type : types){
                Activity activity = new Activity();

                activity.setLead(lead);
                activity.setUser(lead.getOwner());
                activity.setType(type);
                activity.setNote(generateNote(type));
                activity.setActivityDate(activityDate.plusDays(random.nextInt(3) + 1));
                activities.add(activity);
            }
        }

        activityRepository.saveAll(activities);
    }

    private List<ActivityType> buildActivities(Lead lead){
        return switch (lead.getLeadStatus()){
            case NEW -> List.of(ActivityType.EMAIL, ActivityType.CALL);
            case CONTACTED -> List.of(ActivityType.EMAIL, ActivityType.CALL, ActivityType.FOLLOW_UP);
            case QUALIFIED -> List.of(ActivityType.CALL, ActivityType.MEETING, ActivityType.FOLLOW_UP);
            case DEMO -> List.of(ActivityType.CALL, ActivityType.MEETING, ActivityType.DEMO);
            case PROPOSAL -> List.of(ActivityType.CALL, ActivityType.DEMO, ActivityType.QUOTATION);
            case NEGOTIATION -> List.of(ActivityType.DEMO, ActivityType.FOLLOW_UP, ActivityType.MEETING);
            case WON -> List.of(ActivityType.CALL, ActivityType.DEMO, ActivityType.FOLLOW_UP);
            case LOST -> List.of(ActivityType.CALL, ActivityType.FOLLOW_UP);
            case REASSIGNED -> List.of(ActivityType.CALL, ActivityType.EMAIL, ActivityType.FOLLOW_UP);
            case ARCHIVED -> List.of(ActivityType.CALL, ActivityType.QUOTATION);
        };
    }

    private String generateNote(ActivityType type){
        return switch (type){
            case CALL -> "Called customer to discuss requirements.";
            case EMAIL -> "Sent follow-up email.";
            case MEETING -> "Meeting with customer.";
            case DEMO -> "Product demo completed.";
            case QUOTATION -> "Sent quotation.";
            case FOLLOW_UP -> "Follow-up after quotation.";
            default -> "General customer interaction.";
        };
    }

}

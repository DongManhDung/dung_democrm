package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.OpportunityStatus;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.Opportunity;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.OpportunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class OpportunitySeeder {
    private final OpportunityRepository opportunityRepository;

    private final LeadRepository leadRepository;

    private final Random random = new Random();

    public void seed(){
        if(opportunityRepository.count() > 0) return;

        List<Opportunity> opportunities = new ArrayList<>();

        List<Lead> leads = leadRepository.findAll();

        for(Lead lead: leads){

            OpportunityStatus status = mapOpportunityStatus(lead.getLeadStatus());
            if(status == null) continue;;

            Opportunity opportunity = new Opportunity();

            opportunity.setLead(lead);

            opportunity.setAmount(randomAmount());

            opportunity.setProbability(randomProbability(status));

            opportunity.setStatus(status);

            opportunity.setExpectedCloseDate(LocalDate.now().plusDays(random.nextInt(30) + 15));

            if (status == OpportunityStatus.WON || status == OpportunityStatus.LOST){
                opportunity.setClosedAt(LocalDateTime.now().minusDays(random.nextInt(10)));
            }

            if (status == OpportunityStatus.LOST){
                opportunity.setLostReason(randomLostReason());
            }

            opportunities.add(opportunity);
        }

        opportunityRepository.saveAll(opportunities);
    }

    private BigDecimal randomAmount(){
        int amount = (random.nextInt(48) + 3) * 1_000_000;
        return BigDecimal.valueOf(amount);
    }

    private Integer randomProbability(OpportunityStatus opportunityStatus){
        return switch (opportunityStatus){
            case OPEN -> random.nextInt(61) + 20;
            case WON -> 100;
            case LOST -> 0;
        };
    }

    private OpportunityStatus mapOpportunityStatus(LeadStatus leadStatus){
        return switch (leadStatus){
            case NEGOTIATING -> OpportunityStatus.OPEN;
            case WON -> OpportunityStatus.WON;
            case LOST -> OpportunityStatus.LOST;
            default -> null;
        };
    }

    private String randomLostReason(){
        String[] reasons = {
                "Price too high",
                "Lost to competitor",
                "No budget",
                "No response",
                "Project cancelled"
        };

        return reasons[random.nextInt(reasons.length)];
    }
}

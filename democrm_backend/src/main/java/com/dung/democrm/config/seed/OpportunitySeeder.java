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
        if(leadRepository.count() > 0) return;

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
            case OPEN -> 80;
            case WON -> 100;
            case LOST -> 0;
        };
    }

    private OpportunityStatus mapOpportunityStatus(LeadStatus leadStatus){
        return switch (leadStatus){
            case NEGOTIATION -> OpportunityStatus.OPEN;
            case WON -> OpportunityStatus.WON;
            case LOST -> OpportunityStatus.LOST;
            default -> null;
        };
    }
}

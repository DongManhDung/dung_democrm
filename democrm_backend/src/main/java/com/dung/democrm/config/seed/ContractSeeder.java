package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.OpportunityStatus;
import com.dung.democrm.entity.Contract;
import com.dung.democrm.entity.Opportunity;
import com.dung.democrm.repository.ContractRepository;
import com.dung.democrm.repository.OpportunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContractSeeder {
    private final ContractRepository contractRepository;

    private final OpportunityRepository opportunityRepository;

    public void seed(){
        if(contractRepository.count() > 0) return;

        List<Contract> contracts = new ArrayList<>();

        List<Opportunity> opportunities = opportunityRepository.findAll();

        for(Opportunity opportunity : opportunities){
            if(opportunity.getStatus() != OpportunityStatus.WON) continue;

            opportunity.getLead().setLeadStatus(LeadStatus.WON);

            Contract contract = new Contract();
            contract.setOpportunity(opportunity);
            contract.setAmount(opportunity.getAmount());
            contract.setContractNumber(generateContractNumber());

            contracts.add(contract);
        }
        opportunityRepository.saveAll(opportunities);
        contractRepository.saveAll(contracts);
    }

    private String generateContractNumber(){
        return "HD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}

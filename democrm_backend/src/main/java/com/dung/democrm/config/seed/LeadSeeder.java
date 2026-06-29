package com.dung.democrm.config.seed;

import com.dung.democrm.common.enums.LeadSource;
import com.dung.democrm.common.enums.LeadStatus;
import com.dung.democrm.common.enums.Role;
import com.dung.democrm.entity.Customer;
import com.dung.democrm.entity.Lead;
import com.dung.democrm.entity.User;
import com.dung.democrm.repository.CustomerRepository;
import com.dung.democrm.repository.LeadRepository;
import com.dung.democrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class LeadSeeder {
    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public void seed(){
        if(leadRepository.count() > 0) return;

        List<Customer> customers = customerRepository.findAll();

        List<User> sales = userRepository.findAll().stream().filter(u->u.getRole()==Role.SALES).toList();

        List<Lead> leads = new ArrayList<>();


        for(int i = 0; i < 700; i++){
            Lead lead = new Lead();

            lead.setCustomer(customers.get(i));

            User owner = sales.get(random.nextInt(sales.size()));
            lead.setOwner(owner);
            lead.setTeamOwner(owner.getManager());

            lead.setLeadStatus(randomStatus());
            lead.setSource(randomSource());

            LocalDate assignedDate = LocalDate.now().minusDays(random.nextInt(60));
            lead.setAssignedAt(assignedDate);
            lead.setExpiredAt(assignedDate.plusDays(15));

            if(lead.getLeadStatus() == LeadStatus.LOST){
                lead.setLostReason(randomLostReason());
            }

            leads.add(lead);
        }

        leadRepository.saveAll(leads);
    }

    private LeadStatus randomStatus(){
        int randomValue = random.nextInt(100);
        if(randomValue < 25){
            return LeadStatus.NEW;
        }

        if(randomValue < 45){
            return LeadStatus.CONTACTED;
        }

        if(randomValue < 63){
            return LeadStatus.QUALIFIED;
        }

        if(randomValue < 78){
            return LeadStatus.DEMO;
        }

        if(randomValue < 88){
            return LeadStatus.PROPOSAL;
        }

        if(randomValue < 95){
            return LeadStatus.NEGOTIATION;
        }

        if(randomValue < 98){
            return LeadStatus.WON;
        }

        return LeadStatus.LOST;

    }

    private LeadSource randomSource(){
        int randomValue = random.nextInt(100);

        if(randomValue < 35) {
            return LeadSource.FACEBOOK;
        }

        if(randomValue < 55) {
            return LeadSource.WEBSITE;
        }

        if(randomValue < 70) {
            return LeadSource.ZALO;
        }

        if(randomValue < 82) {
            return LeadSource.REFERRAL;
        }

        if(randomValue < 92) {
            return LeadSource.COLD_CALL;
        }

        if(randomValue < 97) {
            return LeadSource.EMAIL;
        }

        return LeadSource.WALK_IN;
    }

    private String randomLostReason(){
        String[] reason = {
                "Không nhấc máy.",
                "Không có nhu cầu.",
                "Gọi thuê bao.",
                "Đã mua của bên khác.",
                "Sai số điện thoại.",
                "Không phản hồi.",
                "Khách có lắng nghe, chưa sử dụng, xin được thông tin tương tác.",
                "Chăm sóc thêm."
        };

        return reason[random.nextInt(reason.length)];
    }
}

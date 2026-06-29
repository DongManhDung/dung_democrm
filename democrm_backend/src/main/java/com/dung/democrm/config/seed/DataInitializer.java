package com.dung.democrm.config.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "app.seed",
        name = "enabled",
        havingValue = "true"
)
public class DataInitializer implements CommandLineRunner {
    private final UserSeeder userSeeder;
    private final CustomerSeeder customerSeeder;
    private final LeadSeeder leadSeeder;
    private final ActivitySeeder activitySeeder;
    private final OpportunitySeeder opportunitySeeder;
    private final ContractSeeder contractSeeder;

    @Override
    public void run(String... args) throws Exception {
        userSeeder.seed();
        customerSeeder.seed();
        leadSeeder.seed();
        activitySeeder.seed();
        opportunitySeeder.seed();
        contractSeeder.seed();
    }
}

package com.dung.democrm.repository;

import com.dung.democrm.entity.Contract;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends BaseRepository<Contract, Long> {
    Optional<Contract> findByContractNumber(String contractNumber);
}

package com.dung.democrm.common.exception;

import com.dung.democrm.dto.response.DuplicateLeadResponse;
import lombok.Getter;

@Getter
public class DuplicateLeadException extends RuntimeException{
    private final DuplicateLeadResponse duplicateLead;

    public DuplicateLeadException(DuplicateLeadResponse duplicateLead){
        super("Customer already has an active lead.");
        this.duplicateLead = duplicateLead;
    }
}

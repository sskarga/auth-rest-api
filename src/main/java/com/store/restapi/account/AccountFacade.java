package com.store.restapi.account;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountFacade {

    private final ModelMapper modelMapper;

    public AccountFacade() {
        this.modelMapper = new ModelMapper();
    }

    public ResponseAccountDto toResponseAccount(Account account) {
        return modelMapper.map(account, ResponseAccountDto.class);
    }
}

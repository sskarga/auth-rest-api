package com.store.restapi.account.facade;

import com.store.restapi.account.dto.ResponseAccountDto;
import com.store.restapi.account.domain.Account;
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

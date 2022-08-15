package com.store.restapi.security.api.facade;

import com.store.restapi.security.api.payload.response.ResponseAccount;
import com.store.restapi.security.domain.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountFacade {

    private final ModelMapper modelMapper;

    public AccountFacade() {
        this.modelMapper = new ModelMapper();
    }

    public ResponseAccount toResponseAccount(Account account) {
        return modelMapper.map(account, ResponseAccount.class);
    }
}

package com.store.restapi.account;

import com.store.restapi.advice.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    public static final String ACCOUNT_NOT_FIND_MSG = "Account with id %s not find.";
    public static final String ACCOUNT_ALREADY_MSG = "Account already";

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account createAccount(Account account) {

        if(Objects.isNull(account)) throw new BadRequestException();
        if(account.getEmail() == null || account.getPassword() == null) throw new BadRequestException();

        boolean exist = accountRepository.existsByEmail(account.getEmail());

        if (exist) {
            throw new BadRequestException(ACCOUNT_ALREADY_MSG);
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Set default role
        if (account.getRole() == null) {
            Role role = roleRepository.findByName("ROLE_USER");
            account.setRole(role);
        }

        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountAndPassword(Account account) {
        Account accountOld = accountRepository.findById(account.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(ACCOUNT_NOT_FIND_MSG, account.getId().toString())));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreatedOn(accountOld.getCreatedOn());
        return  accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Account account) {
        Account accountOld = accountRepository.findById(account.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(ACCOUNT_NOT_FIND_MSG, account.getId().toString())));
        account.setCreatedOn(accountOld.getCreatedOn());
        return accountRepository.save(account);

    }

    @Override
    public Account deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(ACCOUNT_NOT_FIND_MSG, id)));
        accountRepository.delete(account);
        return account;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Account addRoleToAccount(Account account, String roleName) {
        log.info("Adding role {} to account {}", roleName, account.getEmail());
        Role role = roleRepository.findByName(roleName);
        account.setRole(role);
        return accountRepository.save(account);
    }
}

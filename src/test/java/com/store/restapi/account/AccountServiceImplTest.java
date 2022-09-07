package com.store.restapi.account;

import com.store.restapi.advice.exception.BadRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        passwordEncoder = new BCryptPasswordEncoder();

        accountService = new AccountServiceImpl(
                accountRepository,
                roleRepository,
                passwordEncoder);
    }


    @DisplayName("Test create new account")
    @Nested
    class CreateAccountTest {

        private Account account;

        @BeforeEach
        void setUp() {
            account = new Account("email@mail.net", "password", "Ivanov Ivan");
        }

        @DisplayName("No exist email")
        @Test
        void canCreateAccountCheckEmailNoExists() {
            //when
            accountService.createAccount(account);

            //then
            verify(accountRepository).existsByEmail(account.getEmail());
            verify(accountRepository).save(any());
        }

        @DisplayName("Exception account is null")
        @Test
        void canCreateAccountCheckAccountNull() {
            // given
            Account accountNull = null;

            //then
            assertThatThrownBy(() -> accountService.createAccount(accountNull))
                    .isInstanceOf(BadRequestException.class);

            verify(accountRepository, never()).save(any());
        }

        @DisplayName("Exception exist email")
        @Test
        void canCreateAccountCheckEmailExists() {
            // given
            given(accountRepository.existsByEmail(account.getEmail()))
                    .willReturn(true);

            //then
            assertThatThrownBy(() -> accountService.createAccount(account))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessageContaining(AccountServiceImpl.ACCOUNT_ALREADY_MSG);

            verify(accountRepository, never()).save(any());
        }

        @DisplayName("Encoder password")
        @Test
        void canCreateAccountCheckPasswordEncoder() {
            // given
            String password = account.getPassword();

            // when
            accountService.createAccount(account);

            // that
            ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(accountArgumentCaptor.capture());

            Account captorAccount = accountArgumentCaptor.getValue();
            boolean passCheck = passwordEncoder.matches(password, captorAccount.getPassword());
            assertTrue(passCheck);
        }

        @DisplayName("Set default user role")
        @Test
        void canCreateAccountSetDefaultRoleUser() {
            // given
            final String USER_ROLE = "ROLE_USER";

            given(roleRepository.findByName(USER_ROLE))
                    .willReturn(new Role(USER_ROLE));

            // when
            accountService.createAccount(account);

            // that
            ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());

            Account captorAccount = accountArgumentCaptor.getValue();
            assertThat(captorAccount.getRole().getName()).isEqualTo(USER_ROLE);
        }


        @DisplayName("Save account")
        @Test
        void canCreateAccount() {
            // when
            accountService.createAccount(account);

            // that
            ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(accountArgumentCaptor.capture());

            Account captorAccount = accountArgumentCaptor.getValue();

            assertAll(
                    () -> assertThat(captorAccount.getEmail()).isEqualTo(account.getEmail()),
                    () -> assertThat(captorAccount.getUsername()).isEqualTo(account.getUsername())
            );

        }

        @DisplayName("Account arguments exception")
        @ParameterizedTest
        @MethodSource("getArgumentsForCreateAccount")
        void canCreateAccountException(Account testAccount) {
            //then
            assertThatThrownBy(() -> accountService.createAccount(testAccount))
                    .isInstanceOf(BadRequestException.class);

            verify(accountRepository, never()).existsByEmail(any());
            verify(accountRepository, never()).save(any());

        }

        static Stream<Arguments> getArgumentsForCreateAccount() {
            return Stream.of(
                    Arguments.of( new Account("email-test11@mail.ru", null, "username")),
                    Arguments.of( new Account(null, "password123", "username")),
                    Arguments.of( new Account(null, null, "username"))
            );
        }
    }

    @Test
    @Disabled
    void getAllAccounts() {
    }

    @Test
    @Disabled
    void findAccountById() {
    }

    @Test
    @Disabled
    void findAccountByEmail() {
    }

    @Test
    @Disabled
    void updateAccountAndPassword() {
    }

    @Test
    @Disabled
    void updateAccount() {
    }

    @Test
    @Disabled
    void deleteAccount() {
    }

    @Test
    @Disabled
    void getAllRole() {
    }

    @Test
    @Disabled
    void saveRole() {
    }

    @Test
    @Disabled
    void addRoleToAccount() {
    }
}
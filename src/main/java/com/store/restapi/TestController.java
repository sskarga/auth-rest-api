package com.store.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/test")
@Slf4j
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String isAdmin() {
        return "Admin_role";
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public String isStaff() {
        return "Staff_role";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String isUser() {
        return "User_role";
    }
}

package com.store.restapi.registration;

import org.springframework.http.HttpStatus;

public record ResponseMessageDto(HttpStatus status, String msg) {
}

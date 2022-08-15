package com.store.restapi.security.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccount implements Serializable {
    private Long id;
    private String email;
    private String username;
    private Boolean enabled;
    private Boolean locked;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String roleId;
    private String roleName;
}

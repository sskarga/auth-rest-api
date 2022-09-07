package com.store.restapi.account;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAccountDto implements Serializable {
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

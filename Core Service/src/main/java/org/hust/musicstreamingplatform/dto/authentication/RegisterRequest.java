package org.hust.musicstreamingplatform.dto.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
}

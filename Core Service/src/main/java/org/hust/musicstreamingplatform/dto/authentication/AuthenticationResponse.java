package org.hust.musicstreamingplatform.dto.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
}

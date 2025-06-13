package org.hust.musicstreamingplatform.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserInfoRequest {
    String name;
    String email;
}

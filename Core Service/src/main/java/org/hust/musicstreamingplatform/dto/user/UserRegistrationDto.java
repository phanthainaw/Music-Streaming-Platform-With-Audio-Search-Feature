package org.hust.musicstreamingplatform.dto.user;

import lombok.*;
import org.hust.musicstreamingplatform.model.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserRegistrationDto {
    private String username;
    private String password;
    private String email;
    private String name;
    private Role role = Role.LISTENER;

}

package org.hust.musicstreamingplatform.dto.user;

import lombok.*;
import org.hust.musicstreamingplatform.model.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    private int id;
    private String name;
    private String email;
    private Role role;
}

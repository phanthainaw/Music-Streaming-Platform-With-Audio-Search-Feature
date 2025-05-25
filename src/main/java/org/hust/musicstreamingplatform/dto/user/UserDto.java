package org.hust.musicstreamingplatform.dto.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    private int id;
    private String name;
    private String email;
}

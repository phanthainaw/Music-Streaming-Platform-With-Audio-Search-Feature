package org.hust.musicstreamingplatform.dto.user;

import lombok.Data;
import org.hust.musicstreamingplatform.model.enums.Role;

@Data
public class UpdateUserRoleRequest {
    private Role role;
}

package org.hust.musicstreamingplatform.utils;

import org.hust.musicstreamingplatform.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public class Utils {

    public static User getUserFromPrincipal(Principal principal) {
        return ((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
    }
}

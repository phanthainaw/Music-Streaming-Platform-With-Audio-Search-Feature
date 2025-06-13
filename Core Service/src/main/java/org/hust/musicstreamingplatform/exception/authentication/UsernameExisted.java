package org.hust.musicstreamingplatform.exception.authentication;

public class UsernameExisted extends RuntimeException {
    public UsernameExisted(String message) {
        super(message);
    }
}

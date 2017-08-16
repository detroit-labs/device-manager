package com.detroitlabs.devicemanager.sync;


import com.google.firebase.auth.FirebaseUser;

public class SignInResult {
    public final FirebaseUser user;
    public final Exception exception;

    public SignInResult(FirebaseUser user) {
        this(user, null);
    }

    public SignInResult(Exception exception) {
        this(null, exception);
    }

    public SignInResult(FirebaseUser user, Exception exception) {
        this.exception = exception;
        this.user = user;
    }

    public boolean isSuccess() {
        return user != null;
    }

    public static SignInResult empty() {
        return new SignInResult(null, null);
    }
}

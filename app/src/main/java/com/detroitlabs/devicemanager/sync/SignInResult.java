package com.detroitlabs.devicemanager.sync;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;

public class SignInResult extends Result {
    public final FirebaseUser user;

    protected SignInResult(FirebaseUser user) {
        super(null);
        this.user = user;
    }

    protected SignInResult(Exception exception) {
        super(exception);
        this.user = null;
    }

    public static SignInResult success(@NonNull FirebaseUser user) {
        return new SignInResult(user);
    }

    public static SignInResult failure(@Nullable Exception exception) {
        return new SignInResult(exception);
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && user != null;
    }
}

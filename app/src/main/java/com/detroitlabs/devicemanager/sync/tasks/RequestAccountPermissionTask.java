package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.di.qualifiers.DomainRestricted;
import com.detroitlabs.devicemanager.sync.Ui;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

public class RequestAccountPermissionTask extends RequestPermissionTask {

    @Inject
    RequestAccountPermissionTask(@DomainRestricted GoogleApiClient googleApiClient, Ui ui) {
        super(googleApiClient, ui);
    }
}

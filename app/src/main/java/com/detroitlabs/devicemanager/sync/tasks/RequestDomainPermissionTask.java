package com.detroitlabs.devicemanager.sync.tasks;


import com.detroitlabs.devicemanager.di.qualifiers.DomainRestricted;
import com.detroitlabs.devicemanager.sync.Ui;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

public class RequestDomainPermissionTask extends RequestPermissionTask {

    @Inject
    RequestDomainPermissionTask(@DomainRestricted GoogleApiClient googleApiClient, Ui ui) {
        super(googleApiClient, ui);
    }
}

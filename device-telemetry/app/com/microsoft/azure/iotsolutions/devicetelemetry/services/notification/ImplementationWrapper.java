// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.devicetelemetry.services.notification;

import com.google.inject.Inject;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.notification.INotification.EmailImplementationTypes;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.notification.implementation.IImplementation;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.notification.implementation.LogicApp;
import com.microsoft.azure.iotsolutions.devicetelemetry.services.runtime.IServicesConfig;
import play.libs.ws.WSClient;

public class ImplementationWrapper implements IImplementationWrapper{
    private IServicesConfig servicesConfig;
    private WSClient client;

    @Inject
    public ImplementationWrapper(WSClient client, IServicesConfig servicesConfig) {
        this.client = client;
        this.servicesConfig = servicesConfig;
    }

    @Override
    public IImplementation getImplementationType(EmailImplementationTypes actionType) {
        switch (actionType) {
            case LogicApp:
                return new LogicApp(this.servicesConfig.getLogicAppEndPointUrl(), this.servicesConfig.getSolutionName(), this.client);
            default:
                throw new IllegalArgumentException("Improper action type");
        }
    }
}

// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.iothubmanager.webservice.runtime;

import com.google.inject.Singleton;
import com.microsoft.azure.iotsolutions.iothubmanager.services.exceptions.InvalidConfigurationException;
import com.microsoft.azure.iotsolutions.iothubmanager.services.runtime.ConfigData;
import com.microsoft.azure.iotsolutions.iothubmanager.services.runtime.IServicesConfig;
import com.microsoft.azure.iotsolutions.iothubmanager.services.runtime.ServicesConfig;
import com.microsoft.azure.iotsolutions.iothubmanager.webservice.auth.ClientAuthConfig;
import com.microsoft.azure.iotsolutions.iothubmanager.webservice.auth.IClientAuthConfig;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

// TODO: documentation

@Singleton
public class Config implements IConfig {

    // Namespace applied to all the custom configuration settings
    private final String NAMESPACE = "com.microsoft.azure.iotsolutions.";

    // Settings about this application
    private final String APPLICATION_KEY = NAMESPACE + "iothub-manager.";
    private final String PORT_KEY = APPLICATION_KEY + "webservicePort";
    private final String IOTHUB_CONNSTRING_KEY = APPLICATION_KEY + "iotHubConnectionString";
    private final String STORAGE_ADAPTER_WEBSERVICE_URL = APPLICATION_KEY + "storageAdapterWebServiceUrl";

    private final String DEVICE_PROPERTIES_KEY = APPLICATION_KEY + "device-properties-cache.";
    private final String DEVICE_PROPERTIES_TTL = DEVICE_PROPERTIES_KEY + "TTL";
    private final String DEVICE_PROPERTIES_REBUILD_TIMEOUT = DEVICE_PROPERTIES_KEY + "rebuild_timeout";
    private final String DEVICE_PROPERTIES_WHITELIST_KEY = DEVICE_PROPERTIES_KEY + "whitelist";

    private final String CLIENT_AUTH_KEY = APPLICATION_KEY + "client-auth.";
    private final String AUTH_REQUIRED_KEY = CLIENT_AUTH_KEY + "authRequired";
    private final String AUTH_WEB_SERVICE_URL_KEY = CLIENT_AUTH_KEY + "authWebServiceUrl";
    private final String AUTH_TYPE_KEY = CLIENT_AUTH_KEY + "authType";

    private final String JWT_KEY = APPLICATION_KEY + "client-auth.JWT.";
    private final String JWT_ALGOS_KEY = JWT_KEY + "allowedAlgorithms";
    private final String JWT_ISSUER_KEY = JWT_KEY + "authIssuer";
    private final String JWT_AUDIENCE_KEY = JWT_KEY + "aadAppId";
    private final String JWT_CLOCK_SKEW_KEY = JWT_KEY + "clockSkewSeconds";

    private ConfigData data;
    private IServicesConfig servicesConfig;
    private IClientAuthConfig clientAuthConfig;

    public Config() {
        // Load `application.conf` and replace placeholders with
        // environment variables
        this.data = new ConfigData(APPLICATION_KEY);;
    }

    /**
     * Get the TCP port number where the service listen for requests.
     *
     * @return TCP port number
     */
    public int getPort() throws InvalidConfigurationException {
        return data.getInt(PORT_KEY);
    }

    /**
     * Service layer configuration
     */
    public IServicesConfig getServicesConfig() throws InvalidConfigurationException {
        if (this.servicesConfig != null) return this.servicesConfig;

        String cs = data.getString(IOTHUB_CONNSTRING_KEY);
        String storageadapterServiceUrl = data.getString(STORAGE_ADAPTER_WEBSERVICE_URL);
        String userManagementApiUrl = data.getString(AUTH_WEB_SERVICE_URL_KEY);
        this.servicesConfig = new ServicesConfig(
            cs,
            storageadapterServiceUrl,
            userManagementApiUrl,
            this.data.getInt(DEVICE_PROPERTIES_TTL),
            this.data.getInt(DEVICE_PROPERTIES_REBUILD_TIMEOUT),
            this.data.getStringList(DEVICE_PROPERTIES_WHITELIST_KEY)
        );
        return this.servicesConfig;
    }

    /**
     * Client authorization configuration
     */
    public IClientAuthConfig getClientAuthConfig() {
        if (this.clientAuthConfig != null) return this.clientAuthConfig;

        // Default to True unless explicitly disabled
        Boolean authRequired = !data.hasPath(AUTH_REQUIRED_KEY)
            || data.getString(AUTH_REQUIRED_KEY).isEmpty()
            || data.getBool(AUTH_REQUIRED_KEY);

        String authServiceUrl = data.getString(AUTH_WEB_SERVICE_URL_KEY);

        // Default to JWT
        String authType = "JWT";
        if (data.hasPath(AUTH_REQUIRED_KEY)) {
            authType = data.getString(AUTH_TYPE_KEY);
        }

        // Default to RS256, RS384, RS512
        HashSet<String> jwtAllowedAlgos = new HashSet<>();
        jwtAllowedAlgos.add("RS256");
        jwtAllowedAlgos.add("RS384");
        jwtAllowedAlgos.add("RS512");
        if (data.hasPath(JWT_ALGOS_KEY)) {
            jwtAllowedAlgos.clear();
            Collections.addAll(
                jwtAllowedAlgos,
                data.getString(JWT_ALGOS_KEY).split(","));
        }

        // Default to empty, no issuer
        String jwtIssuer = "";
        if (data.hasPath(JWT_ISSUER_KEY)) {
            jwtIssuer = data.getString(JWT_ISSUER_KEY);
        }

        // Default to empty, no audience
        String jwtAudience = "";
        if (data.hasPath(JWT_AUDIENCE_KEY)) {
            jwtAudience = data.getString(JWT_AUDIENCE_KEY);
        }

        // Default to 2 minutes
        Duration jwtClockSkew = Duration.ofSeconds(120);
        if (data.hasPath(JWT_AUDIENCE_KEY)) {
            jwtClockSkew = data.getDuration(JWT_CLOCK_SKEW_KEY);
        }

        this.clientAuthConfig = new ClientAuthConfig(
            authRequired,
            authServiceUrl,
            authType,
            jwtAllowedAlgos,
            jwtIssuer,
            jwtAudience,
            jwtClockSkew);

        return this.clientAuthConfig;
    }
}

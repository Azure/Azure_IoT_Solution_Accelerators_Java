package com.microsoft.azure.iotsolutions.devicetelemetry.services.runtime;

import com.microsoft.azure.iotsolutions.devicetelemetry.services.exceptions.InvalidConfigurationException;

public interface IConfigData {
    String getString(String key);
    boolean getBool(String key);
    int getInt(String key) throws InvalidConfigurationException;
    boolean hasPath(String path);
}

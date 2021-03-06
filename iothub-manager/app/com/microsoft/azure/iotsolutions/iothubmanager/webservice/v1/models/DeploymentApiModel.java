// Copyright (c) Microsoft. All rights reserved.

package com.microsoft.azure.iotsolutions.iothubmanager.webservice.v1.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.iotsolutions.iothubmanager.services.models.DeploymentServiceModel;
import com.microsoft.azure.iotsolutions.iothubmanager.services.models.PackageType;
import com.microsoft.azure.iotsolutions.iothubmanager.services.models.DeviceGroup;
import com.microsoft.azure.iotsolutions.iothubmanager.webservice.v1.Version;

import java.util.Dictionary;
import java.util.Hashtable;

public class DeploymentApiModel {
    private String id;
    private String name;
    private String createdDateTimeUtc;
    private String deviceGroupId;
    private String deviceGroupName;
    private String deviceGroupQuery;
    private String packageContent;
    private String packageName;
    private int priority;
    private PackageType packageType;
    private String configType;
    private DeploymentMetricsApiModel metrics;

    public DeploymentApiModel() {}

    public DeploymentApiModel(String deploymentName, String deviceGroupId, String deviceGroupName,
                              String deviceGroupQuery, String packageContent, String packageName,
                              int priority, PackageType packageType, String configType) {
        this.name = deploymentName;
        this.deviceGroupId = deviceGroupId;
        this.deviceGroupName = deviceGroupName;
        this.deviceGroupQuery = deviceGroupQuery;
        this.packageContent = packageContent;
        this.packageName = packageName;
        this.priority = priority;
        this.packageType = packageType;
        this.configType = configType;
    }

    public DeploymentApiModel(DeploymentServiceModel serviceModel) {
        this.createdDateTimeUtc = serviceModel.getCreatedDateTimeUtc();
        this.id = serviceModel.getId();
        this.deviceGroupId = serviceModel.getDeviceGroup().getId();
        this.deviceGroupName = serviceModel.getDeviceGroup().getName();
        this.deviceGroupQuery = serviceModel.getDeviceGroup().getQuery();
        this.name = serviceModel.getName();
        this.packageContent = serviceModel.getPackageContent();
        this.packageName = serviceModel.getPackageName();
        this.priority = serviceModel.getPriority();
        this.packageType = serviceModel.getPackageType();
        this.configType = serviceModel.getConfigType();
        this.metrics = new DeploymentMetricsApiModel(serviceModel.getDeploymentMetrics());
    }

    @JsonProperty("Id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("Name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("CreatedDateTimeUtc")
    public String getCreatedDateTimeUtc() {
        return this.createdDateTimeUtc;
    }

    @JsonProperty("DeviceGroupId")
    public String getDeviceGroupId() {
        return this.deviceGroupId;
    }

    @JsonProperty("DeviceGroupName")
    public String getDeviceGroupName() { return this.deviceGroupName; }

    @JsonProperty("DeviceGroupQuery")
    public String getDeviceGroupQuery() {
        return this.deviceGroupQuery;
    }

    @JsonProperty("PackageContent")
    public String getPackageContent() {
        return this.packageContent;
    }

    @JsonProperty("PackageName")
    public String getPackageName() { return this.packageName; }

    @JsonProperty("Priority")
    public int getPriority() {
        return this.priority;
    }

    @JsonProperty("PackageType")
    public PackageType getPackageType() {
        return this.packageType;
    }

    @JsonProperty("ConfigType")
    public String getConfigType() {
        return this.configType;
    }

    @JsonProperty("Metrics")
    public DeploymentMetricsApiModel getMetrics() {
        return this.metrics;
    }

    public DeploymentServiceModel toServiceModel() {
        return new DeploymentServiceModel(this.name,
                new DeviceGroup(this.deviceGroupId, this.deviceGroupName, this.deviceGroupQuery),
                this.packageContent,
                this.packageName,
                this.priority,
                this.packageType,
                this.configType);
    }

    @JsonProperty("$metadata")
    public Dictionary<String, String> getMetadata() {
        return new Hashtable<String, String>() {{
            put("$type", "Deployments;" + Version.NUMBER);
            put("$uri", "/" + Version.PATH + "/deployments");
        }};
    }
}

package com.example.kubernetesbackend.entities;

import io.kubernetes.client.openapi.models.V1DeploymentCondition;
import io.kubernetes.client.openapi.models.V1PodCondition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DepInfo implements Serializable {
    private String deploymentName;
    private String deploymentNamespace;
    private List<V1DeploymentCondition> state;

    public DepInfo(String deploymentName, String deploymentNamespace, List<V1DeploymentCondition> state) {
        this.deploymentName = deploymentName;
        this.deploymentNamespace = deploymentNamespace;
        this.state = state;
    }
}

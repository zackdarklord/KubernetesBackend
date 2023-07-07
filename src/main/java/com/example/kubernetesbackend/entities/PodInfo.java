package com.example.kubernetesbackend.entities;

import io.kubernetes.client.openapi.models.V1PodCondition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PodInfo implements Serializable {
    private String podName;
    private String namespace;

    private List <V1PodCondition> state;

    public PodInfo(String podName, String namespace, List <V1PodCondition> state) {
        this.podName = podName;
        this.namespace = namespace;
        this.state = state;
    }


}

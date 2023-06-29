package com.example.kubernetesbackend.services;

import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.PatchUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import io.kubernetes.client.openapi.apis.AppsV1Api;



@Service
public class KubernetesService {

    private final ApiClient apiClient;

    public KubernetesService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<String> getNamespaceNames() {
        List<String> namespaceNames = new ArrayList<>();
        try {
            Configuration.setDefaultApiClient(apiClient);
            CoreV1Api api = new CoreV1Api(apiClient);
            V1NamespaceList namespaceList = api.listNamespace(null, null, null, null, null, null, null, null, null, null);
            for (V1Namespace namespace : namespaceList.getItems()) {
                namespaceNames.add(namespace.getMetadata().getName());
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return namespaceNames;
    }

    public List<String> getHelmReleasesInNamespaces() {
        List<String> releases = new ArrayList<>();
        try {

            AppsV1Api api = new AppsV1Api(apiClient);
            V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
            deploymentList.getItems().forEach(deployment -> {
                Map<String, String> annotations = deployment.getMetadata().getAnnotations();
                if (annotations != null && annotations.containsKey("meta.helm.sh/release-name")) {
                    String releaseName = annotations.get("meta.helm.sh/release-name");
                    String namespace = deployment.getMetadata().getNamespace();
                    String releaseInfo = "Release Name: " + releaseName + ", Namespace: " + namespace;
                    releases.add(releaseInfo);
                }
            });

        } catch (ApiException e) {
            e.printStackTrace();
        }
        return releases;
    }

    public List<String> getHelmReleasesByNamespaces(String namespace) {
        List<String> releases = new ArrayList<>();
        try {
            AppsV1Api api = new AppsV1Api(apiClient);
            V1DeploymentList deploymentList = api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

            deploymentList.getItems().forEach(deployment -> {
                Map<String, String> annotations = deployment.getMetadata().getAnnotations();
                if (annotations != null && annotations.containsKey("meta.helm.sh/release-name") && deployment.getMetadata().getNamespace().equals(namespace)) {
                    String releaseName = annotations.get("meta.helm.sh/release-name");
                    String namespacee = deployment.getMetadata().getNamespace();
                    String revision = annotations.get("deployment.kubernetes.io/revision");
                    String updated = deployment.getMetadata().getCreationTimestamp().toString();
                    String status = deployment.getStatus().getConditions().get(0).getType();
                    String chart = deployment.getApiVersion();
                    String appVersion = deployment.getMetadata().getResourceVersion();
                    releases.add("name: " + releaseName + " namespace: " + namespace + "  revision: " + revision +
                            " updated: " + updated + " status: " + status + " chart: " + chart + " appVersion: " + appVersion);


                }
            });
        } catch (ApiException e) {
            e.printStackTrace();
            String errorInfo = "ERROR: Failed to retrieve helm releases.";
            releases.add(errorInfo);
        }
        return releases;
    }

    public void restartDeployment(String deploymentName, String namespace) {
        try {
            // Create an instance of the Kubernetes client
            AppsV1Api api = new AppsV1Api(apiClient);

            // Get the current deployment
            V1Deployment deployment = api.readNamespacedDeployment(deploymentName, namespace, null);

            // Add or update an annotation to trigger a restart
            V1DeploymentSpec spec = deployment.getSpec();
            spec.getTemplate().getMetadata().putAnnotationsItem("kubectl.kubernetes.io/restartedAt", String.valueOf(System.currentTimeMillis()));

            // Update the deployment to apply the changes
            api.replaceNamespacedDeployment(deploymentName, namespace, deployment, null, null, null, null);

            System.out.println("Deployment restarted successfully.");
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}


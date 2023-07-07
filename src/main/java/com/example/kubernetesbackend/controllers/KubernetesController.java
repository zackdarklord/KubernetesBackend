package com.example.kubernetesbackend.controllers;

import com.example.kubernetesbackend.entities.DepInfo;
import com.example.kubernetesbackend.entities.PodInfo;
import com.example.kubernetesbackend.services.KubernetesService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kubernetes")
public class KubernetesController {

    private final KubernetesService kubernetesService;

    @Autowired
    public KubernetesController(KubernetesService kubernetesService) {
        this.kubernetesService = kubernetesService;
    }

    @GetMapping("/namespaces")
    public List<String> getNamespaces() {
        return kubernetesService.getNamespaceNames();
    }

    @GetMapping("/releases")
    public List<String> getHelmReleasesInNamespaces() {
        return kubernetesService.getHelmReleasesInNamespaces();
    }

    @GetMapping("/{namespace}")
    public ResponseEntity<List<String>> getHelmReleasesByNamespace(@PathVariable("namespace") String namespace) {
        List<String> releases = kubernetesService.getHelmReleasesByNamespaces(namespace);
        return new ResponseEntity<>(releases, HttpStatus.OK);
    }
/*
    @PostMapping("/{namespace}/{deploymentName}/restart")
    public ResponseEntity<String> restartDeployment(@PathVariable String namespace, @PathVariable String deploymentName) {
        try {
            kubernetesService.restartDeployment(deploymentName, namespace);
            return ResponseEntity.ok("Deployment restarted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to restart deployment.");
        }
    }
*/
    @PostMapping("/{namespace}/{deploymentName}/restart")
    public ResponseEntity<String> restartDeployment(@PathVariable String namespace, @PathVariable String deploymentName) {
        try {
            kubernetesService.restartDeployment(deploymentName, namespace);
            return ResponseEntity.ok("Deployment restarted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to restart deployment.");
        }
    }

    @GetMapping("/pods")
    public List<PodInfo> getAllPods()
    {
        return kubernetesService.getAllPods();
    }
    @GetMapping("/deployments")
    public List<DepInfo> getAllDeployments(){
        return kubernetesService.getAllDeployments();
    }
}

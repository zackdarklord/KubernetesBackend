package com.example.kubernetesbackend.controllers;

import com.example.kubernetesbackend.services.KubernetesService;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

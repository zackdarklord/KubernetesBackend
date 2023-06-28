package com.example.kubernetesbackend.configurations;


import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;

@Configuration
public class KubernetesConfig {

    @Bean
    public ApiClient apiClient() throws IOException {
        return ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader("src/main/resources/admin.kubeconfig")))
                .build();
    }
}






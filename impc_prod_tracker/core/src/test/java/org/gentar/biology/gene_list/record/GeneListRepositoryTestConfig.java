package org.gentar.biology.gene_list.record;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.Entity;

@Configuration
@EnableJpaRepositories(basePackages={"org.gentar"})
@EntityScan(basePackages={"org.gentar"})
@ComponentScan(basePackages={"org.gentar"})
public class GeneListRepositoryTestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}

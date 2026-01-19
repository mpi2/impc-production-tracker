package org.gentar.spring_configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {


    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .defaultContentType(MediaTypes.HAL_JSON)
            .mediaType("json", MediaTypes.HAL_JSON)
            .mediaType("hal+json", MediaTypes.HAL_JSON);
    }
}
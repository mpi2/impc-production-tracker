package org.gentar.spring_configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class HateoasConfig implements WebMvcConfigurer {

    @Override
    @SuppressWarnings("removal")
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(c ->
                c.getSupportedMediaTypes().contains(MediaType.valueOf("application/schema+json"))
        );
    }
}
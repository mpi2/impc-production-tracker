package org.gentar.spring_configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import jakarta.servlet.Filter;

@Configuration
public class RestConfiguration
{
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    /**
     * Defines how are the links with collections are going to be handled.
     * The general configuration sets that all the links, even if they are single elements, are
     * going to be shown as arrays. Later we find the exceptions. "self" links are always going to
     * be single, but the general configuration would set it as an array as well, so we need to override
     * that.
     * Lastly we have specific relations that we need to set as single links, like "project" because
     * it refers to a single element.
     * @return
     */
    @Bean
    public HalConfiguration linkRelationBasedPolicy() {
        return new HalConfiguration()
            .withRenderSingleLinks(HalConfiguration.RenderSingleLinks.AS_ARRAY)
            .withRenderSingleLinksFor(
                LinkRelation.of("self"), HalConfiguration.RenderSingleLinks.AS_SINGLE)
            .withRenderSingleLinksFor(
                LinkRelation.of("project"), HalConfiguration.RenderSingleLinks.AS_SINGLE)
            .withRenderSingleLinksFor(
                LinkRelation.of("plan"), HalConfiguration.RenderSingleLinks.AS_SINGLE)
            .withRenderSingleLinksFor(
                LinkRelation.of("outcome"), HalConfiguration.RenderSingleLinks.AS_SINGLE)
            .withRenderSingleLinksFor(
                LinkRelation.of("productionPlan"), HalConfiguration.RenderSingleLinks.AS_SINGLE);
    }

}

package org.gentar.biology.gene_list;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TomcatCustomizer  implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>
{
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            AbstractHttp11Protocol protocol = (AbstractHttp11Protocol) connector.getProtocolHandler();

            int originMaxKeepAliveRequests = protocol.getMaxKeepAliveRequests();
            protocol.setMaxKeepAliveRequests(-1);
            int originKeepAliveTimeout = protocol.getKeepAliveTimeout();
            //protocol.setKeepAliveTimeout(60000);

            log.info(
                "####################################################################################");
            log.info("#");
            log.info("# TomcatCustomizer");
            log.info("#");
            log.info("# origin maxKeepAliveRequests {}", originMaxKeepAliveRequests);
            log.info("# custom maxKeepAliveRequests {}", protocol.getMaxKeepAliveRequests());
            log.info("# origin keepalive timeout: {} ms", originKeepAliveTimeout);
            log.info("# keepalive timeout: {} ms", protocol.getKeepAliveTimeout());
            log.info("# connection timeout: {} ms", protocol.getConnectionTimeout());
            log.info("# max connections: {}", protocol.getMaxConnections());
            log.info("#");
            log.info(
                "####################################################################################");

        });
    }
}

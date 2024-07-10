package com.joaopimenta.busca_cnpj.config;

import com.joaopimenta.busca_cnpj.clients.MinhaReceitaClient;
import com.joaopimenta.busca_cnpj.clients.MinhaReceitaMirrorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class MinhaReceitaMirrorConfig {

        private final String url;

        public MinhaReceitaMirrorConfig(@Value("${urls.minha-receita-mirror-client}") String url) {
            this.url = url;
        }

        @Bean
        MinhaReceitaMirrorClient mirrorClient() {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                    .build();

            ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.newConnection()
                    .compress(true)
            );

            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .uriBuilderFactory(new DefaultUriBuilderFactory(uriComponentsBuilder()))
                    .exchangeStrategies(exchangeStrategies)
                    .clientConnector(connector)
                    .build();

            WebClientAdapter webClientAdapter = WebClientAdapter.create(client);
            webClientAdapter.setBlockTimeout(Duration.ofSeconds(7));

            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                    .exchangeAdapter(webClientAdapter)
                    .build();
            return factory.createClient(MinhaReceitaMirrorClient.class);
        }

        private UriComponentsBuilder uriComponentsBuilder() {
            return UriComponentsBuilder.fromHttpUrl(url);
        }

}

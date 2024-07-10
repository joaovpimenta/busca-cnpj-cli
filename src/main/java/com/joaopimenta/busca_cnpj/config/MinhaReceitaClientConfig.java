package com.joaopimenta.busca_cnpj.config;

import com.joaopimenta.busca_cnpj.clients.MinhaReceitaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

@Configuration
public class MinhaReceitaClientConfig {

        private final String url;

        public MinhaReceitaClientConfig(@Value("${urls.minha-receita-client}") String url) {
            this.url = url;
        }

        @Bean
        MinhaReceitaClient receitaClient() {
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
            return factory.createClient(MinhaReceitaClient.class);
        }

        private UriComponentsBuilder uriComponentsBuilder() {
            return UriComponentsBuilder.fromHttpUrl(url);
        }

}

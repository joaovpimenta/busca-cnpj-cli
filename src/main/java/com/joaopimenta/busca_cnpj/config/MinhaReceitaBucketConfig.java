package com.joaopimenta.busca_cnpj.config;

import com.joaopimenta.busca_cnpj.clients.MinhaReceitaBucketClient;
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
public class MinhaReceitaBucketConfig {

        private final String url;

        public MinhaReceitaBucketConfig(@Value("${urls.minha-receita-bucket-client}") String url) {
            this.url = url;
        }

        @Bean
        MinhaReceitaBucketClient bucketClient() {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize((int) (2L * 1024L * 1024L * 1024L)))
                    .build();

            ClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.newConnection()
                    .compress(true).responseTimeout(Duration.ofSeconds(240))
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
            return factory.createClient(MinhaReceitaBucketClient.class);
        }

        private UriComponentsBuilder uriComponentsBuilder() {
            return UriComponentsBuilder.fromHttpUrl(url);
        }

}

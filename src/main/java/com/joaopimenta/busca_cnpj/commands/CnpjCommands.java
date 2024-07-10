package com.joaopimenta.busca_cnpj.commands;

import com.jayway.jsonpath.JsonPath;
import com.joaopimenta.busca_cnpj.clients.MinhaReceitaBucketClient;
import com.joaopimenta.busca_cnpj.clients.MinhaReceitaClient;
import com.joaopimenta.busca_cnpj.clients.MinhaReceitaMirrorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@ShellComponent
public class CnpjCommands {

    private final MinhaReceitaClient receitaClient;
    private final MinhaReceitaMirrorClient receitaMirrorClient;
    private final MinhaReceitaBucketClient receitaBucketClient;
    private final String receitaBucketUrl;
//    private final CsvService csvService;
//    private List<Empresa> empresas;

    public CnpjCommands(MinhaReceitaClient receitaClient, MinhaReceitaMirrorClient receitaMirrorClient, MinhaReceitaBucketClient receitaBucketClient, @Value("${urls.minha-receita-bucket-client}") String receitaBucketUrl
//                        CsvService csvService
                        ) {
        this.receitaClient = receitaClient;
        this.receitaMirrorClient = receitaMirrorClient;
        this.receitaBucketClient = receitaBucketClient;
        this.receitaBucketUrl = receitaBucketUrl;
//        this.csvService = csvService;
    }

    @Cacheable
    @ShellMethod("Esse método carrega dados cnpj da web")
    public String loadCnpj() {

        String dataAtualizacao = "2024-06-13";
        //new ObjectMapper().readValue(receitaClient.getDataDeAtualizacao().block(), DataAtualizacao.class).getData();

        String jsonPath = "$.data[?(@.name == \'" + dataAtualizacao + "\')].urls[*].url";
        String urlsResponse = receitaMirrorClient.getUrlsDownload();

        List<String> urlsDownload = JsonPath.read(urlsResponse, jsonPath);

        urlsDownload.forEach(System.out::println);

        Stream<Flux<byte[]>> downloads = urlsDownload.parallelStream()
                .map(url -> url.replace(receitaBucketUrl, ""))
                .map(receitaBucketClient::downloadFile)
                ;

        Flux<byte[]> mergedFlux = Flux.fromStream(downloads)
                .flatMap(flux -> flux.onErrorResume(throwable -> Flux.empty()))
                .parallel()
                .runOn(Schedulers.parallel())
                .sequential()
                .collectList()
                .flatMapMany(Flux::fromIterable);

        List<byte[]> downloadList = mergedFlux.collectList().blockOptional().orElse(Collections.emptyList());

//                .map(Mono::block)
//                .filter(Objects::nonNull)
//                .map(String::new)
//                .toList();

//        csvService.extractEmpresaList()
//        if (empresas == null) {
//            byte[] zipFile = receitaClient.getDadosCNPJ().block();
//            empresas = csvService.extractEmpresaList(zipFile);
//
//        }

        return "Dados de CNPJ atualizado até " + dataAtualizacao + " carregados, " + downloadList.size() + " arquivos a serem baixados.";
    }

//    @ShellMethod("Esse método filtra os resultados por um parametro e um predicado")
//    public String filtrarCnpj(@ShellOption String parametro, @ShellOption String valorEsperado) {
//        List<Empresa> empresas = this.empresas.stream().filter(empresa -> {
//            try {
//                String valor = String.valueOf(empresa.getClass().getMethod(parametro).invoke(empresa));
//                return valorEsperado.equals(valor);
//            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//                return false;
//            }
//        }).toList();
//
//        empresas.forEach(System.out::println);
//        return empresas.size() + " CNPJs filtrados";
//    }

//    @ShellMethod("Filter results by specified fields and predicates")
//    public List<Empresa> filterResults(@ShellOption Map<String, String> filters) {
//        List<Empresa> empresasFiltradas = new ArrayList<>();
//
//        Map<String, String> fieldPredicates = new HashMap<>();
//        for (Map.Entry<String, String> entry : filters.entrySet()) {
//            fieldPredicates.put(entry.getKey(), value -> value.contains(entry.getValue()));
//        }
//
//        empresas.stream()
//                .filter(empresa -> fieldPredicates.entrySet().stream()
//                        .allMatch(entry -> {
//                            try {
//                                String valor = String.valueOf(empresa.getClass().getMethod(entry.getKey()).invoke(empresa));
//                                return entry.getValue().test(valor);
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                        }))
//                .collect(Collectors.toList());
//        return empresasFiltradas;
//    }

}

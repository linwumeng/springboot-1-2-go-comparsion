package gateway.controller;

import com.google.common.util.concurrent.RateLimiter;
import gateway.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class PersonController {

    private WebClient webClient = WebClient.builder().baseUrl("http://localhost:8090").build();
    final RateLimiter rateLimiter = RateLimiter.create(100.0);

    @GetMapping(value = "/persons", produces = "application/json")
    public Mono<Flux<Person>> listPersons() throws InterruptedException {
        return Mono.fromCallable(new Callable<Flux<Person>>() {
            @Override
            public Flux<Person> call() throws Exception {
                rateLimiter.acquire();
                return webClient.get()
                        .uri("/persons")
                        .header(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .header(ACCEPT, APPLICATION_JSON_UTF8_VALUE)
                        .exchange()
                        .flatMapMany(it -> it.bodyToFlux(Person.class));
            }
        });
    }
}

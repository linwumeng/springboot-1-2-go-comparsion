package gateway.controller;

import gateway.model.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class PersonController {

    private WebClient webClient = WebClient.builder().baseUrl("http://localhost:8090").build();

    @GetMapping(value = "/persons", produces = "application/json")
    public Flux<Person> listPersons() {
        return webClient.get()
                .uri("/persons")
                .header(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .header(ACCEPT, APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .flatMapMany(it -> it.bodyToFlux(Person.class));
    }
}

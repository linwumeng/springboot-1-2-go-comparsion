package gateway.handler;

import gateway.model.Person;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Component
public class PersonHandler {

    private WebClient webClient = WebClient.builder().baseUrl("http://localhost:8090").build();

    public Mono<ServerResponse> persons(ServerRequest request) {
        Flux<Person> persons = webClient.get()
                .uri("/persons")
                .header(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                .header(ACCEPT, APPLICATION_JSON_UTF8_VALUE)
                .exchange()
                .flatMapMany(it -> it.bodyToFlux(Person.class));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromPublisher(persons, Person.class));
    }
}

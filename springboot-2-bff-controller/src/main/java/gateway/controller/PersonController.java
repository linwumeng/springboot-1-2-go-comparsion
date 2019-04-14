package gateway.controller;

import com.google.common.util.concurrent.RateLimiter;
import gateway.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
public class PersonController {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    final RateLimiter rateLimiter = RateLimiter.create(100.0);

    @GetMapping(value = "/persons", produces = "application/json")
    public DeferredResult<List<Person>> listPersons() {
        DeferredResult<List<Person>> output = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            rateLimiter.acquire();
            ResponseEntity<List> personList = restTemplateBuilder.build().getForEntity("http://localhost:8090/persons", List.class);
            output.setResult(personList.getBody());
        });

        return output;
    }
}

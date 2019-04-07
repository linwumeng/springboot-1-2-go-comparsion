package com.lwm.personslowservice.controller;

import com.lwm.personslowservice.model.Person;
import net.andreinc.mockneat.MockNeat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @GetMapping(value = "/", produces = "application/json")
    public String status() {
        return "{\"status\":\"up\"}";
    }

    @GetMapping(value = "/persons", produces = "application/json")
    public List<Person> listPersons() throws InterruptedException {
        MockNeat mock = MockNeat.threadLocal();

        Thread.sleep(2000);

        return mock.reflect(Person.class)
                        .field("id", mock.longSeq().valStr())
                        .field("name", mock.names().full())
                        .field("email", mock.emails())
                        .field("currency", mock.currencies().name())
                        .list(100)
                        .val();
    }
}

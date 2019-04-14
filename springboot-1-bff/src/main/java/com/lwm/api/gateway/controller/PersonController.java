package com.lwm.api.gateway.controller;

import com.lwm.api.gateway.model.Person;
import com.weddini.throttling.Throttling;
import com.weddini.throttling.ThrottlingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class PersonController {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @GetMapping(value = "/persons", produces = "application/json")
    @Throttling(type = ThrottlingType.RemoteAddr, limit = 1, timeUnit = TimeUnit.SECONDS)
    public List<Person> listPersons() {
        ResponseEntity<List> personList = restTemplateBuilder.build().getForEntity("http://localhost:8090/persons", List.class);

        return  personList.getBody();
    }
}

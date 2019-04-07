package com.lwm.api.gateway.controller;

import com.lwm.api.gateway.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @GetMapping(value = "/persons", produces = "application/json")
    public List<Person> listPersons() {
        ResponseEntity<List> personList = restTemplateBuilder.build().getForEntity("http://localhost:8090/persons", List.class);

        return  personList.getBody();
    }
}

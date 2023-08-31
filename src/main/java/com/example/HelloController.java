package com.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/api")
public class HelloController {

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public String index() {
        return "OK";
    }
}

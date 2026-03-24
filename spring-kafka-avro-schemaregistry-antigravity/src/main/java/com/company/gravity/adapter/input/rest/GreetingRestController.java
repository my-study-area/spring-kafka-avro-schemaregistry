package com.company.gravity.adapter.input.rest;

import com.company.gravity.domain.model.Greeting;
import com.company.gravity.domain.port.input.GetGreetingInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class GreetingRestController {

    private final GetGreetingInputPort getGreetingInputPort;

    @GetMapping
    public Greeting hello() {
        return getGreetingInputPort.execute();
    }
}

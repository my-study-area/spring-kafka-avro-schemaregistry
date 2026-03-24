package com.company.gravity.application.usecase;

import com.company.gravity.domain.model.Greeting;
import com.company.gravity.domain.port.input.GetGreetingInputPort;
import org.springframework.stereotype.Service;

@Service
public class GetGreetingUseCase implements GetGreetingInputPort {

    @Override
    public Greeting execute() {
        return new Greeting("Olá Mundo");
    }
}

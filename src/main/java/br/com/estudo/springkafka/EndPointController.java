package br.com.estudo.springkafka;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EndPointController {

    private final TopicoProducer producer;

    public EndPointController(TopicoProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/enviar/{mensagem}")
    public ResponseEntity<?> enviarMensagem(@PathVariable String mensagem) {
        producer.enviarMensagem(mensagem);
        return ResponseEntity.ok("Mensagem enviada");
    }
}

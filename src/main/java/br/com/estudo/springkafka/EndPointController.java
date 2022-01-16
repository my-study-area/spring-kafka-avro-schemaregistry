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

    @GetMapping("/enviar/")
    public ResponseEntity<?> enviarMensagemAvro(@RequestParam String remetente,
                                                @RequestParam String destinatario,
                                                @RequestParam String mensagem) {
        Mensagem mensagemAvro = Mensagem.newBuilder()
                .setRemetente(remetente)
                .setDestinatario(destinatario)
                .setCorpo(mensagem)
                .build();
        producer.enviarMensagemAvro(mensagemAvro);
        return ResponseEntity.ok("Mensagem avro enviada");
    }
}

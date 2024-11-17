package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.AtualizarBairro;
import Java.Api.ApiJava.Controle.Dto.InserirBairro;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.service.BairroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bairro")
public class ControlBairro {

    private final BairroService bairroService;

    public ControlBairro(BairroService bairroService) {
        this.bairroService = bairroService;
    }



}

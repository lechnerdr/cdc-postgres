package com.examplo.cdc.cdcpostgres.controller;

import com.examplo.cdc.cdcpostgres.controller.request.TesteRequest;
import com.examplo.cdc.cdcpostgres.service.TesteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/testes")
@RequiredArgsConstructor
@Slf4j
class TesteController {

    private final TesteService testeService;

    @Operation(summary = "Método para salvar teste", description = "Método para salvar teste",
        responses = {
            @ApiResponse(responseCode = "201", description = "Teste criado com sucesso."),
            @ApiResponse(responseCode = "500", description = "Não foi possível criar o teste")
        })
    @PostMapping
    public ResponseEntity<Void> criarUsuarioOutbox(@RequestBody TesteRequest teste) {

        log.info("Criando teste. Request: {}", teste);

        var usuarioSalvo = testeService.salvarTeste(teste);

        var location = ServletUriComponentsBuilder
            .fromCurrentRequestUri().path("/{id}")
            .buildAndExpand(usuarioSalvo.getId()).toUri();

        log.info("Teste criado com sucesso");

        return ResponseEntity
            .created(location)
            .build();

    }

}

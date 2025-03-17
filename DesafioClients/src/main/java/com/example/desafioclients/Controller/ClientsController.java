package com.example.desafioclients.Controller;

import com.example.desafioclients.Model.Clients;
import com.example.desafioclients.Model.Colors;
import com.example.desafioclients.Model.ClientDTO;
import com.example.desafioclients.Service.ClientsService;
import com.example.desafioclients.Views.ClientDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientsController {
    private final ClientsService clientsService;
    @Autowired
    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }
    @GetMapping ("/GetAllClients")
    public List<ClientDetails> getAllClients() {
        return clientsService.getAllClients();
    }
    @PostMapping("/CreateOrUpdateClient")
    public ResponseEntity<?> createOrUpdateClient(@RequestBody ClientDTO clientDTO) {
        try {
            clientsService.saveOrUpdateClient(clientDTO);
            return ResponseEntity.ok("Cliente salvo com sucesso!");
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Erro ao processar a requisição.";
            if (e.getMostSpecificCause().getMessage().contains("CPF já cadastrado")) {
                errorMessage = "CPF já cadastrado.";
            } else if (e.getMostSpecificCause().getMessage().contains("E-mail já cadastrado")) {
                errorMessage = "E-mail já cadastrado.";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @GetMapping("/GetClientById/{id}")
    public Clients getClientById(@PathVariable Long id) {
        return clientsService.getClientById(id);
    }
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateClient(@PathVariable Long id) {
        clientsService.deactivateClient(id);
        return ResponseEntity.ok("Cliente desativado com sucesso!");
    }
    @GetMapping("/colors")
    public List<Colors> getAllColors() {
        return clientsService.getAllColors();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException e) {
        return "Erro interno: " + e.getMessage();
    }
}

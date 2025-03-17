package com.example.desafioclients.Service;
import com.example.desafioclients.Model.Clients;
import com.example.desafioclients.Model.Colors;
import com.example.desafioclients.Model.ClientDTO;
import com.example.desafioclients.Repository.ClientsRepository;
import com.example.desafioclients.Views.ClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;
    public List<ClientDetails> getAllClients() {
        return clientsRepository.findAllClients();
    }
    public void saveOrUpdateClient(ClientDTO clientDTO) {
        try {
            clientsRepository.upsertClient(
                    clientDTO.getId(),
                    clientDTO.getClientName(),
                    clientDTO.getCpf(),
                    clientDTO.getIsActive() != null ? clientDTO.getIsActive() : true,
                    clientDTO.getColorId(),
                    clientDTO.getEmail()
            );
        } catch (DataIntegrityViolationException e) {
            String sqlErrorMessage = e.getMostSpecificCause().getMessage();
            if (sqlErrorMessage.contains("CPF já cadastrado")) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }
            if (sqlErrorMessage.contains("E-mail já cadastrado")) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
            throw new RuntimeException("Erro ao processar a requisição.");
        }
    }
    public Clients getClientById(Long id) {
        return clientsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }
    public void deactivateClient(Long id) {
        Clients client = clientsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
                //implementação para desativar o client inves de excluilo da tabela, mas precisaria
                // de mais tratativas de errorText no front e o tempo esta acabando
                //client.setIsActive(false);
        clientsRepository.delete(client);
        clientsRepository.save(client);
    }
    public List<Colors> getAllColors() {
        return clientsRepository.findAllColors();
    }
}

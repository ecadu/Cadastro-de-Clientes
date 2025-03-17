package com.example.desafioclients.Model; // Se for mover, troque para DTO
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Integer id;

    private String clientName;

    private String cpf;

    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Boolean isActive = true;

    private Integer colorId;

    private String email;

}

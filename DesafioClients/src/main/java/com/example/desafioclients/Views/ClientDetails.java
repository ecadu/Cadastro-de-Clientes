package com.example.desafioclients.Views;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "clientdetails")
@Getter
@Setter
public class ClientDetails {
    @Id
    private Long id;
    private String email;
    private String clientname;
    private String cpf;
    private Boolean isactive;
    private Integer colorid;
    private String favoritecolor;
}

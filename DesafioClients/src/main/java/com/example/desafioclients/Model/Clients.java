package com.example.desafioclients.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "Clients")
@Getter
@Setter
@NoArgsConstructor
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "clientname", nullable = false)
    private String clientName;

    @Column(name = "favoritecolor")
    private String favoriteColor;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, unique = true)
    private String email;

}

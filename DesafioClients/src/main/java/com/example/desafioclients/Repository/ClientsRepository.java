package com.example.desafioclients.Repository;

import com.example.desafioclients.Model.Clients;
import com.example.desafioclients.Views.ClientDetails;
import com.example.desafioclients.Model.Colors; // Certifique-se de que essa entidade existe!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import java.util.List;
@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    @Query("SELECT c FROM ClientDetails c")
    List<ClientDetails> findAllClients();
    @Modifying
    @Transactional
    @Query(value = "CALL upsert_client(:id, :clientname, :cpf, :isactive, :colorId,:email)", nativeQuery = true)
    void upsertClient(
            @Param("id") Integer id,
            @Param("clientname") String clientName,
            @Param("cpf") String cpf,
            @Param("isactive") Boolean isActive,
            @Param("colorId") Integer colorId,
            @Param("email") String email
    );
    @Query("SELECT c FROM Colors c")
    List<Colors> findAllColors();
}

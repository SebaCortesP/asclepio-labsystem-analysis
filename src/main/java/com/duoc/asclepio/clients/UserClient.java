package com.duoc.asclepio.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.duoc.asclepio.dto.RoleDTO;
import com.duoc.asclepio.dto.UserDTO;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081/users").build();
    }

    public boolean userExists(Long userId) {
        try {
            webClient.get()
                    .uri("/{id}", userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error comunicándose con el servicio de usuarios: " + e.getMessage());
        }
    }

      public UserDTO createUserForPacient(Long userId, String name, String lastname, String email) {
        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setName(name);
        dto.setLastname(lastname);
        dto.setEmail(email);

        // rol paciente = 3
        RoleDTO role = new RoleDTO();
        role.setId(3L);
        role.setName("paciente");
        dto.setRole(role);

        return webClient.post()
                .uri("")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }
}

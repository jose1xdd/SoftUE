package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "SinginToken")
@JsonIgnoreProperties({"usuario_codigo"})
public class SingInToken {
    @Id
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'valor_predeterminado'")
    @NotBlank(message = "Error: El campo 'token' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el token del SingInToken.")
    @NotNull(message = "Error: El campo 'token' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el token del SingInToken.")
    private String token;

    @Column(nullable = false)
    @NotNull(message = "Error: El campo 'fecha_caducidad' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la fecha de caducidad del SingInToken.")
    private LocalDateTime fecha_caducidad;

    @OneToOne()
    @JoinColumn(name = "usuario_codigo")
    private User usuario_codigo;
}

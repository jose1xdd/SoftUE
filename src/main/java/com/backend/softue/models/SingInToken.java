package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SinginToken")
@JsonIgnoreProperties({"usuario_codigo"})
public class SingInToken {
    @Id
    @Column(nullable = false)
    @ColumnDefault(value="CURRENT_TIMESTAMP")
    @NotBlank(message = "")
    @NotNull(message = "")
    private String token;

    @Column(nullable = false)
    @NotNull(message = "")
    private LocalDateTime fecha_caducidad;

    @OneToOne()
    @JoinColumn(name = "usuario_codigo")
    private User usuario_codigo;
}

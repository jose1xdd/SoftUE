package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
    private String token;
    @Column(nullable = false)
    private LocalDateTime fecha_caducidad;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_codigo")
    private User usuario_codigo;
    @Transient
    private Integer userCod;
}

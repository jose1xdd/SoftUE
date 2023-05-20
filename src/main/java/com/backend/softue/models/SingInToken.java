package com.backend.softue.models;

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
}

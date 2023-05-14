package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

import javax.naming.Name;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "SinginToken")
public class SingInToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    private LocalDate fecha_caducidad;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_codigo")
    private User usuario_codigo;
}

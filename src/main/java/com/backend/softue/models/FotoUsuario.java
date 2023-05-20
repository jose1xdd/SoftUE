package com.backend.softue.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="Foto_usuario")
public class FotoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Blob foto;
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "foto_usuario")
    private User usuario_codigo;
}

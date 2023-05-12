package com.backend.softue.models;

import java.sql.Blob;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Foto_usuario")
public class fotoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Blob foto;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "foto_usuario")
    private usuario usuario_codigo;
}

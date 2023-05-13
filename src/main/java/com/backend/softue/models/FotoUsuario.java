package com.backend.softue.models;
import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name="Foto_usuario")
public class FotoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Blob foto;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "foto_usuario")
    private Usuario usuario_codigo;
}

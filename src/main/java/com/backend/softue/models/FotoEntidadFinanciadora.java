package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Blob;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Foto_entidad_financiadora")
public class FotoEntidadFinanciadora {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Blob foto;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "fotoEntidadFinanciadoraId")
    private EntidadFinanciadora entidadFinanciadoraId;

}

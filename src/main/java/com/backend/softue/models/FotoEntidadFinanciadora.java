package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("entidadFinanciadoraId")
@Table(name = "Foto_entidad_financiadora")
public class FotoEntidadFinanciadora {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] foto;

    @NotNull
    @NotBlank
    private String extension;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "entidad_fnanciadora_id")
    private EntidadFinanciadora entidadFinanciadoraId;

}

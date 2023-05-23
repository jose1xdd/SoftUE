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
@Table(name = "Documento_idea")
public class DocumentoIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "documento_idea_id")
    private Integer documentoIdeaId;
    
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] documento;

    @OneToOne(mappedBy = "documentoIdea")
    private IdeaNegocio ideaNegocio;

}

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
@Table(name = "Documento_plan")
public class DocumentoPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "documento_plan_id")
    private Integer documentoPlanId;

    @Lob
    @NotNull
    @NotBlank
    private Blob documento;

    @OneToOne(mappedBy = "documentoPlan", fetch = FetchType.LAZY)
    private PlanNegocio planNegocio;
}

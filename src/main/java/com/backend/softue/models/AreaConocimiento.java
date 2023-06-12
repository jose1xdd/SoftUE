package com.backend.softue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"ideasNegocio", "planesNegocio"})
@Table(name = "Area_conocimiento")
public class AreaConocimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<IdeaNegocio> ideasNegocio;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<PlanNegocio> planesNegocio;

}

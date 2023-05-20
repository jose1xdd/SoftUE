package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Formato")
public class Formato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(nullable = false)
    private String modulo;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCreacion;

    @Lob
    private Blob documento;
}

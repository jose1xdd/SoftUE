package com.backend.softue.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Entidad_financiadora")
public class EntidadFinanciadora {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Error: El campo 'nombre' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el nombre de la Entidad financiadora.")
    @NotNull(message = "Error: El campo 'nombre' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el nombre de la Entidad financiadora.")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Error: El campo 'telefono' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el telefono de la Entidad financiadora.")
    @NotNull(message = "Error: El campo 'telefono' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el telefono de la Entidad financiadora.")
    @Column(nullable = false)
    private String telefono;

    @NotBlank(message = "Error: El campo 'sitioWeb' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el sitioWeb de la Entidad financiadora.")
    @NotNull(message = "Error: El campo 'sitioWeb' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para el sitioWeb de la Entidad financiadora.")
    @Column(nullable = false)
    private String sitioWeb;

    @Email(message = "Error: El campo 'correo' no es valido. Asegúrese de proporcionar una direccion de correo electronico valida para la Entidad financiadora.")
    @NotBlank(message = "Error: El campo 'correo' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para el correo de la Entidad financiadora.")
    @Column(nullable = false)
    private String correo;

    @NotBlank(message = "Error: El campo 'descripcion' no puede estar en blanco. Por favor, asegurese de proporcionar un valor valido para la descripcion de la Entidad financiadora.")
    @NotNull(message = "Error: El campo 'descripcion' no puede ser nulo. Por favor, asegurese de proporcionar un valor valido para la descripcion de la Entidad financiadora.")
    @Column(nullable = false)
    private String descripcion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_entidad_financiadora_id", referencedColumnName = "id")
    private FotoEntidadFinanciadora fotoEntidadFinanciadoraId;
    @Transient
    private Integer fotoId;
}

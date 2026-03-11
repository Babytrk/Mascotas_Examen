package com.example.mascotas.mascotasServicios;

import com.example.mascotas.mascotas.Mascota;
import com.example.mascotas.servicios.Servicio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mascota_servicio")
public class MascotaServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private String nota;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    @JsonIgnoreProperties("mascotaServicios")
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    @JsonIgnoreProperties("mascotaServicios")
    private Servicio servicio;
}
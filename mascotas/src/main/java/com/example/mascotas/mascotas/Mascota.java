package com.example.mascotas.mascotas;

import com.example.mascotas.clientes.Cliente;
import com.example.mascotas.mascotasServicios.MascotaServicio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mascota")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMascota;

    @Column(nullable = false, length = 100)
    private String nombre;

    private char sexo;

    @Column(nullable = false, length = 100)
    private String tipo;

    private byte edad;

    private boolean enPeligro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    @JsonIgnoreProperties("mascotas") // CORTE DE BUCLE: No cargues mascotas desde aquí
    private Cliente cliente;

    @OneToMany(mappedBy = "mascota")
    @JsonIgnoreProperties("mascota")
    private List<MascotaServicio> mascotaServicios;
}
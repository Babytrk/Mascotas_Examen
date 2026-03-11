package com.example.mascotas.clientes;

import com.example.mascotas.direccion.Direccion;
import com.example.mascotas.mascotas.Mascota;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String email;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cliente")
    private Direccion direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cliente") // CORTE DE BUCLE: No cargues al dueño desde la mascota
    private List<Mascota> mascotas = new ArrayList<>();
}
package com.example.mascotas.mascotasServicios;

import com.example.mascotas.mascotas.Mascota;
import com.example.mascotas.mascotas.MascotaRepository;
import com.example.mascotas.servicios.Servicio;
import com.example.mascotas.servicios.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mascota-servicio")
public class MascotaServicioController {

    @Autowired
    private MascotaServicioRepository mascotaServicioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public ResponseEntity<List<MascotaServicio>> findAll() {
        return ResponseEntity.ok((List<MascotaServicio>) mascotaServicioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaServicio> findById(@PathVariable Long id) {
        return mascotaServicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MascotaServicio> create(@RequestBody MascotaServicio mascotaServicio, UriComponentsBuilder uriBuilder) {
        if (mascotaServicio.getMascota() == null || mascotaServicio.getServicio() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Mascota> mascotaOpcional = mascotaRepository.findById(mascotaServicio.getMascota().getIdMascota());
        Optional<Servicio> servicioOpcional = servicioRepository.findById(mascotaServicio.getServicio().getIdServicio());

        if (mascotaOpcional.isEmpty() || servicioOpcional.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        mascotaServicio.setMascota(mascotaOpcional.get());
        mascotaServicio.setServicio(servicioOpcional.get());

        MascotaServicio guardado = mascotaServicioRepository.save(mascotaServicio);

        URI url = uriBuilder.path("/mascota-servicio/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity.created(url).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaServicio> update(@PathVariable Long id, @RequestBody MascotaServicio detalles) {
        return mascotaServicioRepository.findById(id).map(existente -> {
            existente.setFecha(detalles.getFecha());
            existente.setNota(detalles.getNota());
            return ResponseEntity.ok(mascotaServicioRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!mascotaServicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mascotaServicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
package com.example.mascotas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/servicio")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    /* 1. OBTENER TODOS LOS SERVICIOS */
    @GetMapping
    public ResponseEntity<Iterable<Servicio>> findAll() {
        return ResponseEntity.ok(servicioRepository.findAll());
    }

    /* 2. OBTENER SERVICIO POR ID (Nuevo) */
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findById(@PathVariable Long id) {
        Optional<Servicio> servicioOpcional = servicioRepository.findById(id);

        // Si existe devuelve 200 OK con el objeto, si no, 404 Not Found
        return servicioOpcional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* 3. CREAR SERVICIO */
    @PostMapping
    public ResponseEntity<Servicio> create(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioRepository.save(servicio));
    }

    /* 4. ACTUALIZAR SERVICIO POR ID */
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Long id, @RequestBody Servicio detallesServicio) {
        return servicioRepository.findById(id).map(servicioExistente -> {
            servicioExistente.setDescripcion(detallesServicio.getDescripcion());
            servicioExistente.setPrecio(detallesServicio.getPrecio());
            return ResponseEntity.ok(servicioRepository.save(servicioExistente));
        }).orElse(ResponseEntity.notFound().build());
    }

    /* 5. ELIMINAR SERVICIO */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!servicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        servicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
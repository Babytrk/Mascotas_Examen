package com.example.mascotas.mascotas;

import com.example.mascotas.clientes.Cliente;
import com.example.mascotas.clientes.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mascota")
@CrossOrigin(origins = "http://localhost:5173") // Permite la conexión con tu React
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // 1. OBTENER TODAS LAS MASCOTAS
    @GetMapping
    public ResponseEntity<List<Mascota>> findAll() {
        List<Mascota> mascotas = (List<Mascota>) mascotaRepository.findAll();
        return ResponseEntity.ok(mascotas);
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> findById(@PathVariable Long id) {
        return mascotaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR NUEVA MASCOTA (Con validación de Cliente)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Mascota mascota) {
        // Verificamos si el cliente existe antes de guardar
        if (mascota.getCliente() != null && mascota.getCliente().getIdCliente() != null) {
            Optional<Cliente> clienteOpt = clienteRepository.findById(mascota.getCliente().getIdCliente());
            if (clienteOpt.isPresent()) {
                mascota.setCliente(clienteOpt.get());
                Mascota nuevaMascota = mascotaRepository.save(mascota);
                return ResponseEntity.ok(nuevaMascota);
            }
        }
        return ResponseEntity.badRequest().body("Error: Debes asignar un dueño (Cliente) válido a la mascota.");
    }

    // 4. ACTUALIZAR MASCOTA
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> update(@PathVariable Long id, @RequestBody Mascota mascotaActualizada) {
        return mascotaRepository.findById(id)
                .map(mascotaExistente -> {
                    mascotaExistente.setNombre(mascotaActualizada.getNombre());
                    mascotaExistente.setTipo(mascotaActualizada.getTipo());
                    mascotaExistente.setSexo(mascotaActualizada.getSexo());
                    mascotaExistente.setEdad(mascotaActualizada.getEdad());
                    mascotaExistente.setEnPeligro(mascotaActualizada.isEnPeligro());

                    // Si cambias el dueño en la edición
                    if (mascotaActualizada.getCliente() != null) {
                        clienteRepository.findById(mascotaActualizada.getCliente().getIdCliente())
                                .ifPresent(mascotaExistente::setCliente);
                    }

                    return ResponseEntity.ok(mascotaRepository.save(mascotaExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR MASCOTA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (mascotaRepository.existsById(id)) {
            mascotaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
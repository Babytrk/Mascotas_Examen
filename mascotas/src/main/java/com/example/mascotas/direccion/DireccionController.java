package com.example.mascotas.direccion;

import com.example.mascotas.clientes.Cliente;
import com.example.mascotas.clientes.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/direccion")
public class DireccionController {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<Iterable<Direccion>> findAll() {
        return ResponseEntity.ok(direccionRepository.findAll());
    }

    // GET por ID (Corrige el error 405 en búsquedas)
    @GetMapping("/{id}")
    public ResponseEntity<Direccion> findById(@PathVariable Long id) {
        return direccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crea o Actualiza (Evita el error 500 por duplicados)
    @PostMapping
    public ResponseEntity<Direccion> create(@RequestBody Direccion direccion) {
        Optional<Cliente> clienteOpcional = clienteRepository.findById(direccion.getCliente().getIdCliente());
        if (clienteOpcional.isEmpty()) return ResponseEntity.unprocessableEntity().build();

        Cliente cliente = clienteOpcional.get();
        // Si el cliente ya tiene dirección, le asignamos ese ID para sobreescribirla
        if (cliente.getDireccion() != null) {
            direccion.setIdDireccion(cliente.getDireccion().getIdDireccion());
        }
        direccion.setCliente(cliente);
        return ResponseEntity.ok(direccionRepository.save(direccion));
    }

    // PUT: Actualizar por ID (Corrige el error 405 en actualizarDireccionID)
    @PutMapping("/{id}")
    public ResponseEntity<Direccion> update(@PathVariable Long id, @RequestBody Direccion detalles) {
        return direccionRepository.findById(id).map(existente -> {
            existente.setCalle(detalles.getCalle());
            existente.setNumero(detalles.getNumero());
            return ResponseEntity.ok(direccionRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!direccionRepository.existsById(id)) return ResponseEntity.notFound().build();
        direccionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
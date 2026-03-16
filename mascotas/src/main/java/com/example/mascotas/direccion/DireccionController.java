package com.example.mascotas.direccion;

import com.example.mascotas.clientes.Cliente;
import com.example.mascotas.clientes.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
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

    @GetMapping("/{id}")
    public ResponseEntity<Direccion> findById(@PathVariable Long id) {
        return direccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Direccion> create(@RequestBody Direccion direccion) {
        Optional<Cliente> clienteOpcional = clienteRepository.findById(direccion.getCliente().getIdCliente());
        if (clienteOpcional.isEmpty()) return ResponseEntity.unprocessableEntity().build();

        Cliente cliente = clienteOpcional.get();
        if (cliente.getDireccion() != null) {
            direccion.setIdDireccion(cliente.getDireccion().getIdDireccion());
        }
        direccion.setCliente(cliente);
        return ResponseEntity.ok(direccionRepository.save(direccion));
    }

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
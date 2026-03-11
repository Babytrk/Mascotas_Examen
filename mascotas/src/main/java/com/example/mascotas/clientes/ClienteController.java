package com.example.mascotas.clientes;

import com.example.mascotas.mascotas.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    /// metodos para obtener todos los clientes
    @GetMapping()
    public ResponseEntity<Iterable<Cliente>> findAll(){
        return ResponseEntity.ok(this.clienteRepository.findAll());
    }

    /// metodo crear cliente
    @PostMapping()
    public ResponseEntity<Cliente> create(@RequestBody Cliente cliente, UriComponentsBuilder uriBuilder){
        if (cliente.getDireccion()!=null){
            cliente.getDireccion().setCliente(cliente);
        }
        if (cliente.getNombre()!=null && !cliente.getMascotas().isEmpty()){
            cliente.getMascotas().forEach(mascota->mascota.setCliente(cliente));
        }

        Cliente create = clienteRepository.save(cliente);
        URI uri = uriBuilder.path("/cliente/{idCliente}").buildAndExpand(
                create.getIdCliente()
        ).toUri();
        return ResponseEntity.created(uri).body(create);
    }

    ///  buscar cliente por Id
    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> findById(@PathVariable Long idCliente){
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        if(clienteOptional.isPresent()){
            return ResponseEntity.ok(clienteOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //actualizar cliente
    @PutMapping("/{idCliente}")
    public ResponseEntity<Cliente> update(@PathVariable Long idCliente, @RequestBody Cliente clienteActualizado){
        Optional<Cliente> clienteOptional = clienteRepository.findById(idCliente);
        if(!clienteOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Cliente clienteExistente = clienteOptional.get();

        //actualizar los datos
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApPaterno(clienteActualizado.getApPaterno());
        clienteExistente.setApMaterno(clienteActualizado.getApMaterno());
        clienteExistente.setEmail(clienteActualizado.getEmail());

        if(clienteActualizado.getDireccion()!=null){
            clienteActualizado.getDireccion().setCliente(clienteExistente);
            clienteExistente.setDireccion(clienteActualizado.getDireccion());
        }

        Cliente clienteUpdate = clienteRepository.save(clienteExistente);
        return ResponseEntity.ok(clienteUpdate);
    }
    ///  metodo para eliminarClientes
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> delete(@PathVariable Long idCliente){
        if(clienteRepository.existsById(idCliente)){
            clienteRepository.deleteById(idCliente);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.noContent().build();
        }
    }
}

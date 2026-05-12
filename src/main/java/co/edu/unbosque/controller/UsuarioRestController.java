package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioRestController {
    
    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;
    
    @GetMapping(value = "/getAll")
    public List<Usuario> getAll() {
        return usuarioServiceAPI.getAll();
    }
    
    @PostMapping(value = "/saveUsuario")
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        Usuario obj = usuarioServiceAPI.save(usuario);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) throws ResourceNotFoundException {
        Usuario usuario = usuarioServiceAPI.get(id);
        if (usuario == null) {
            throw new ResourceNotFoundException("Record not found for Usuario: " + id);
        }
        return ResponseEntity.ok().body(usuario);
    }
    
    @DeleteMapping(value = "/deleteUsuario/{id}")
    public ResponseEntity<Usuario> delete(@PathVariable Integer id) {
        Usuario usuario = usuarioServiceAPI.get(id);
        if (usuario != null) {
            usuarioServiceAPI.delete(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/proyecto")
public class UsuarioRestController {
    
    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioServiceAPI.getAll());
    }
    
    @PostMapping(value = "/saveUsuario")
    public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(usuarioServiceAPI.save(usuario));
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioServiceAPI.get(id));
    }
    
    @DeleteMapping(value = "/deleteUsuario/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
       usuarioServiceAPI.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateUsuario")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario){
        Usuario user = usuarioServiceAPI.get(usuario.getIdUsuario());
        String passwordActual = user.getPassword();
        user.setUsername(usuario.getUsername());
        user.setPassword(usuario.getPassword());
        user.setNombreApellido(usuario.getNombreApellido());
        user.setIdRol(usuario.getIdRol());
        user.setEstado(usuario.getEstado());
        if (!usuario.getPassword().equals(passwordActual)){
            user.setFechaUltClave(LocalDateTime.now());
        }
        return ResponseEntity.ok(usuarioServiceAPI.update(user));
    }

}
package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;

    @GetMapping("/getAll")
    public List<Usuario> getAll() {
        return usuarioServiceAPI.getAll();
    }

    @PostMapping("/saveUsuario")
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        Usuario obj = usuarioServiceAPI.save(usuario);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @PutMapping("/updateUsuario")
    public ResponseEntity<Usuario> update(@RequestBody Usuario usuario) {
        if (usuario.getIdUsuario() == null || usuarioServiceAPI.get(usuario.getIdUsuario()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Usuario updated = usuarioServiceAPI.update(usuario);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/findRecord/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) throws ResourceNotFoundException {
        Usuario usuario = usuarioServiceAPI.get(id);
        if (usuario == null) {
            throw new ResourceNotFoundException("Record not found for Usuario: " + id);
        }
        return ResponseEntity.ok().body(usuario);
    }

    @DeleteMapping("/deleteUsuario/{id}")
    public ResponseEntity<Usuario> delete(@PathVariable Integer id) {
        Usuario usuario = usuarioServiceAPI.get(id);
        if (usuario != null) {
            usuarioServiceAPI.delete(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario credenciales) {
        Optional<Usuario> resultado = usuarioServiceAPI.findByUsernameAndPassword(
                credenciales.getUsername(),
                credenciales.getPassword()
        );
        return resultado
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}

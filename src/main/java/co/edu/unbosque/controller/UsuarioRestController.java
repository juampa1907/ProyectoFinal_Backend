package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import co.edu.unbosque.utils.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioRestController {
    
    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioServiceAPI.getAll());
    }
    
    @PostMapping(value = "/saveUsuario")
    public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) throws GeneralException, ResourceDuplicateException {
        try{
            boolean existeUsuario = usuarioServiceAPI.existsByUsername(usuario.getUsername());
            boolean existeNombreApellido = usuarioServiceAPI.existsByNombreApellido(usuario.getNombreApellido());
            if(existeUsuario){
                throw new ResourceDuplicateException("Usuario " +  usuario.getUsername() + " ya existente");
            }
            if (existeNombreApellido){
                throw new ResourceDuplicateException("Nombre: " + usuario.getNombreApellido() + " ya existente");
            }
           return ResponseEntity.status(HttpStatus.CREATED).body(usuarioServiceAPI.save(usuario));
        } catch (ResourceDuplicateException e) {
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al guardar el usuario: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) throws ResourceNotFoundException{
        Usuario usuario = usuarioServiceAPI.get(id);
        if(usuario == null){
            throw new ResourceNotFoundException("usuario no encontrado con id: " + id);
        }
        return ResponseEntity.ok(usuario);
    }
    
    @DeleteMapping(value = "/deleteUsuario/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) throws ResourceNotFoundException {
        Usuario usuario = usuarioServiceAPI.get(id);
        if (usuario == null){
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioServiceAPI.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateUsuario")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario) throws ResourceNotFoundException, GeneralException{
        try{

            Usuario existente = usuarioServiceAPI.get(usuario.getIdUsuario());

            if(existente == null){
                throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuario.getIdUsuario());
            }

            if (usuario.getUsername() != null) existente.setUsername(usuario.getUsername());
            if (usuario.getNombreApellido() != null) existente.setNombreApellido(usuario.getNombreApellido());
            if (usuario.getCorreo() != null) existente.setCorreo(usuario.getCorreo());
            if (usuario.getPassword() != null) {
                existente.setPassword(usuario.getPassword());
                existente.setFechaUltClave(LocalDateTime.now());
            }
            if (usuario.getIdRol() != null) existente.setIdRol(usuario.getIdRol());
            if (usuario.getEstado() != null) existente.setEstado(usuario.getEstado());

            Usuario resultado = usuarioServiceAPI.update(existente);

            return ResponseEntity.ok(resultado);

        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> loginUsuario(@RequestBody Usuario request) throws UnauthorizedException{
        Optional<Usuario> resultado = usuarioServiceAPI.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(resultado.isPresent()){
            return ResponseEntity.ok(resultado.get());
        } else {
            throw new UnauthorizedException("Usuario o contraseña incorrectos");
        }
    }

}
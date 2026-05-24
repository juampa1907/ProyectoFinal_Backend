package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.CodigoVerificacionServiceAPI;
import co.edu.unbosque.service.api.EmailServiceAPI;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.utils.HashUtil;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import co.edu.unbosque.utils.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioRestController {

    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;

    @Autowired
    private EmailServiceAPI emailServiceAPI;

    @Autowired
    private CodigoVerificacionServiceAPI codigoVerificacionServiceAPI;

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
            Usuario guardado = usuarioServiceAPI.save(usuario);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(guardado.getIdUsuario());
            audit.setAccion("CREATE");
            audit.setTablaAfectada("USUARIOS");
            audit.setIdRegistroAfectado(guardado.getIdUsuario());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
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
        Auditoria audit = new Auditoria();
        audit.setIdUsuario(id);
        audit.setAccion("DELETE");
        audit.setTablaAfectada("USUARIOS");
        audit.setIdRegistroAfectado(id);
        audit.setIpCliente("127.0.0.1");
        auditoriaServiceAPI.save(audit);
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
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                existente.setPassword(HashUtil.hashSHA1(usuario.getPassword()));
                existente.setFechaUltClave(LocalDateTime.now());
            }
            if (usuario.getIdRol() != null) existente.setIdRol(usuario.getIdRol());
            if (usuario.getEstado() != null) existente.setEstado(usuario.getEstado());

            Usuario resultado = usuarioServiceAPI.update(existente);

            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdUsuario());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("USUARIOS");
            audit.setIdRegistroAfectado(resultado.getIdUsuario());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);

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
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.get().getIdUsuario());
            audit.setAccion("LOGIN");
            audit.setTablaAfectada("USUARIOS");
            audit.setIdRegistroAfectado(resultado.get().getIdUsuario());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.ok(resultado.get());
        } else {
            throw new UnauthorizedException("Usuario o contraseña incorrectos");
        }
    }

    @PostMapping("/enviarCodigoVerificacion")
    public ResponseEntity<Void> enviarCodigoVerificacion(@RequestBody Usuario request)
            throws ResourceDuplicateException {
        String correo = request.getCorreo();
        if (correo == null || correo.isBlank()) {
            throw new ResourceDuplicateException("El correo es requerido");
        }
        if (usuarioServiceAPI.existsByCorreo(correo)) {
            throw new ResourceDuplicateException("El correo " + correo + " ya está registrado");
        }
        if (request.getUsername() != null && usuarioServiceAPI.existsByUsername(request.getUsername())) {
            throw new ResourceDuplicateException("Usuario " + request.getUsername() + " ya existente");
        }
        if (request.getNombreApellido() != null && usuarioServiceAPI.existsByNombreApellido(request.getNombreApellido())) {
            throw new ResourceDuplicateException("Nombre: " + request.getNombreApellido() + " ya existente");
        }
        String codigo = codigoVerificacionServiceAPI.generarCodigo();
        codigoVerificacionServiceAPI.guardarCodigo(correo, codigo);
        emailServiceAPI.enviarCodigoVerificacion(correo, codigo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario,
                                              @RequestParam("codigoVerificacion") String codigoVerificacion)
            throws ResourceDuplicateException, GeneralException {
        try {
            String correo = usuario.getCorreo();

            if (codigoVerificacion == null || !codigoVerificacionServiceAPI.validarCodigo(correo, codigoVerificacion)) {
                throw new GeneralException("Código de verificación inválido o expirado");
            }

            String passwordOriginal = usuario.getPassword();
            Usuario guardado = usuarioServiceAPI.save(usuario);

            codigoVerificacionServiceAPI.eliminarCodigo(correo);

            emailServiceAPI.enviarCredenciales(guardado, passwordOriginal);

            Auditoria audit = new Auditoria();
            audit.setIdUsuario(guardado.getIdUsuario());
            audit.setAccion("CREATE");
            audit.setTablaAfectada("USUARIOS");
            audit.setIdRegistroAfectado(guardado.getIdUsuario());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        } catch (ResourceDuplicateException e) {
            throw e;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/solicitarCambioClave")
    public ResponseEntity<Map<String, String>> solicitarCambioClave(@RequestBody Usuario request)
            throws ResourceNotFoundException, GeneralException {
        try {
            String username = request.getUsername();
            if (username == null || username.isBlank()) {
                throw new GeneralException("El username es requerido");
            }

            Optional<Usuario> usuarioOpt = usuarioServiceAPI.findByUsername(username);
            if (usuarioOpt.isEmpty()) {
                throw new ResourceNotFoundException("Usuario no encontrado: " + username);
            }

            String correo = usuarioOpt.get().getCorreo();
            String correoMask = enmascararCorreo(correo);

            String codigo = codigoVerificacionServiceAPI.generarCodigo();
            codigoVerificacionServiceAPI.guardarCodigo(correo, codigo);
            emailServiceAPI.enviarCodigoVerificacion(correo, codigo);

            Map<String, String> response = new HashMap<>();
            response.put("correo", correo);
            response.put("correoMask", correoMask);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al solicitar cambio de clave: " + e.getMessage());
        }
    }

    @PostMapping("/validarCodigoCambio")
    public ResponseEntity<Void> validarCodigoCambio(@RequestBody Usuario request)
            throws ResourceNotFoundException, GeneralException {
        try {
            String username = request.getUsername();
            String codigoVerificacion = request.getPassword();

            if (username == null || username.isBlank()) {
                throw new GeneralException("El username es requerido");
            }
            if (codigoVerificacion == null || codigoVerificacion.isBlank()) {
                throw new GeneralException("El código de verificación es requerido");
            }

            Optional<Usuario> usuarioOpt = usuarioServiceAPI.findByUsername(username);
            if (usuarioOpt.isEmpty()) {
                throw new ResourceNotFoundException("Usuario no encontrado: " + username);
            }

            String correo = usuarioOpt.get().getCorreo();

            if (!codigoVerificacionServiceAPI.verificarCodigoSinConsumir(correo, codigoVerificacion)) {
                throw new GeneralException("Código de verificación inválido o expirado");
            }

            codigoVerificacionServiceAPI.marcarValidado(username);

            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al validar el código: " + e.getMessage());
        }
    }

    @PutMapping("/cambiarClave")
    public ResponseEntity<Usuario> cambiarClave(@RequestBody Usuario request)
            throws ResourceNotFoundException, GeneralException {
        try {
            String username = request.getUsername();
            String newPassword = request.getPassword();

            if (username == null || username.isBlank()) {
                throw new GeneralException("El username es requerido");
            }
            if (!codigoVerificacionServiceAPI.estaValidado(username)) {
                throw new GeneralException("Debe validar el código de verificación primero");
            }
            if (newPassword == null || newPassword.isEmpty()) {
                throw new GeneralException("La nueva contraseña es requerida");
            }

            Optional<Usuario> usuarioOpt = usuarioServiceAPI.findByUsername(username);
            if (usuarioOpt.isEmpty()) {
                codigoVerificacionServiceAPI.limpiarValidacion(username);
                throw new ResourceNotFoundException("Usuario no encontrado: " + username);
            }

            Usuario existente = usuarioOpt.get();
            String correo = existente.getCorreo();

            existente.setPassword(HashUtil.hashSHA1(newPassword));
            existente.setFechaUltClave(LocalDateTime.now());

            Usuario resultado = usuarioServiceAPI.update(existente);

            codigoVerificacionServiceAPI.eliminarCodigo(correo);
            codigoVerificacionServiceAPI.limpiarValidacion(username);

            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdUsuario());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("USUARIOS");
            audit.setIdRegistroAfectado(resultado.getIdUsuario());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);

            return ResponseEntity.ok(resultado);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al cambiar la clave: " + e.getMessage());
        }
    }

    private String enmascararCorreo(String correo) {
        int arrobaIndex = correo.indexOf('@');
        if (arrobaIndex <= 1) return correo;
        String parteLocal = correo.substring(0, arrobaIndex);
        String dominio = correo.substring(arrobaIndex);
        String mask = parteLocal.charAt(0) + "***" + parteLocal.charAt(parteLocal.length() - 1);
        return mask + dominio;
    }

}
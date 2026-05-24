package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Rol;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.RolServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rol")
@CrossOrigin(origins = "http://localhost:4200")
public class RolRestController {

    @Autowired
    private RolServiceAPI rolServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Rol>> getAllRoles() {
        log.info("Listando todos los roles");
        return ResponseEntity.ok(rolServiceAPI.getAll());
    }


    @PostMapping(value = "/saveRol")
    public ResponseEntity<Rol> saveRol(@RequestBody Rol rol) throws GeneralException, ResourceDuplicateException {
        try {
            boolean existeRol = rolServiceAPI.existsByNombreRol(rol.getNombreRol());
            if (existeRol) {
                log.warn("Duplicado: {}", rol.getNombreRol());
                throw new ResourceDuplicateException("Rol " + rol.getNombreRol() + " ya existente");
            }
            Rol guardado = rolServiceAPI.save(rol);
            log.info("Rol guardado: {}", guardado.getNombreRol());
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (ResourceDuplicateException e) {
            log.warn("Duplicado: {}", rol.getNombreRol());
            throw e;
        } catch (Exception e) {
            log.error("Error al guardar el rol: {}", e.getMessage());
            throw new GeneralException("Error al guardar el rol: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteRol/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Integer id) throws ResourceNotFoundException {
        Rol rol = rolServiceAPI.get(id);
        if (rol == null) {
            log.warn("No encontrado: {}", id);
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        rolServiceAPI.delete(id);
        log.info("Rol eliminado: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Rol> getRolById(@PathVariable Integer id) throws ResourceNotFoundException {
        Rol rol = rolServiceAPI.get(id);
        if (rol == null) {
            log.warn("No encontrado: {}", id);
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        log.info("Rol encontrado: {}", id);
        return ResponseEntity.ok(rol);
    }

    @PutMapping("/updateRol")
    public ResponseEntity<Rol> updateRol(@RequestBody Rol rol) throws ResourceNotFoundException, GeneralException{
        try{
            Rol existente = rolServiceAPI.get(rol.getIdRol());
            if(existente == null){
                log.warn("No encontrado: {}", rol.getIdRol());
                throw new ResourceNotFoundException("Rol no encontrado con id: " + rol.getIdRol());
            }
            Rol resultado = rolServiceAPI.update(rol);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdRol());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("ROLES");
            audit.setIdRegistroAfectado(resultado.getIdRol());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            log.info("Rol actualizado: {}", resultado.getIdRol());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            log.warn("No encontrado: {}", rol.getIdRol());
            throw e;
        } catch (Exception e){
            log.error("Error al actualizar el rol: {}", e.getMessage());
            throw new GeneralException("Error al actualizar el rol: " + e.getMessage());
        }
    }
}

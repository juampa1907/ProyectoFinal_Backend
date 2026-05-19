package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Rol;
import co.edu.unbosque.service.api.RolServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol")
@CrossOrigin(origins = "http://localhost:4200")
public class RolRestController {

    @Autowired
    private RolServiceAPI rolServiceAPI;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Rol>> getAllRoles() {
        return ResponseEntity.ok(rolServiceAPI.getAll());
    }


    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Rol> getRolById(@PathVariable Integer id) throws ResourceNotFoundException {
        Rol rol = rolServiceAPI.get(id);
        if (rol == null) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        return ResponseEntity.ok(rol);
    }

    @PutMapping("/updateRol")
    public ResponseEntity<Rol> updateRol(@RequestBody Rol rol) throws ResourceNotFoundException, GeneralException{
        try{
            Rol existente = rolServiceAPI.get(rol.getIdRol());
            if(existente == null){
                throw new ResourceNotFoundException("Rol no encontrado con id: " + rol.getIdRol());
            }
            if (rol.getNombreRol() != null) existente.setNombreRol(rol.getNombreRol());
            if (rol.getEstado() != null) existente.setEstado(rol.getEstado());
            Rol resultado = rolServiceAPI.update(existente);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el rol: " + e.getMessage());
        }
    }
}

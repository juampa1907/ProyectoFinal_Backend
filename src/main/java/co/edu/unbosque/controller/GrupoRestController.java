package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.GrupoServiceAPI;
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
@RequestMapping("/api/grupo")
@CrossOrigin(origins = "http://localhost:4200")
public class GrupoRestController {
    
    @Autowired
    private GrupoServiceAPI grupoServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        log.info("Listando todos los grupos");
        return ResponseEntity.ok(grupoServiceAPI.getAll());
    }

    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable String id) throws ResourceNotFoundException{
        Grupo grupo = grupoServiceAPI.get(id);
        if(grupo == null){
            log.warn("No encontrado: {}", id);
            throw new ResourceNotFoundException("Grupo no encontrado con id: " + id);
        }
        log.info("Grupo encontrado: {}", id);
        return ResponseEntity.ok(grupo);
    }

    @PutMapping("/updateGrupo")
    public ResponseEntity<Grupo> updateGrupo(@RequestBody Grupo grupo) throws ResourceNotFoundException, GeneralException{
        try{
            Grupo existente = grupoServiceAPI.get(grupo.getIdGrupo());
            if(existente == null){
                log.warn("No encontrado: {}", grupo.getIdGrupo());
                throw new ResourceNotFoundException("Grupo no encontrado con id: " + grupo.getIdGrupo());
            }
            if (grupo.getDescripcion() != null) existente.setDescripcion(grupo.getDescripcion());
            if (grupo.getEstado() != null) existente.setEstado(grupo.getEstado());
            Grupo resultado = grupoServiceAPI.update(existente);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(1);
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("GRUPOS");
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            log.info("Grupo actualizado: {}", resultado.getIdGrupo());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            log.warn("No encontrado: {}", grupo.getIdGrupo());
            throw e;
        } catch (Exception e){
            log.error("Error al actualizar el grupo: {}", e.getMessage());
            throw new GeneralException("Error al actualizar el grupo: " + e.getMessage());
        }
    }
}

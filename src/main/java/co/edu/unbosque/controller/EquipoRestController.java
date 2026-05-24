package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.EquipoServiceAPI;
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
@RequestMapping("/api/equipo")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipoRestController {
    
    @Autowired
    private EquipoServiceAPI equipoServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Equipo>> getAllEquipos() {
        log.info("Listando todos los equipos");
        return ResponseEntity.ok(equipoServiceAPI.getAll());
    }

    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Equipo> getEquipoById(@PathVariable Integer id) throws ResourceNotFoundException{

        Equipo equipo = equipoServiceAPI.get(id);
        if(equipo == null){
            log.warn("No encontrado: {}", id);
            throw new ResourceNotFoundException("Equipo no encontrado con id: " + id);
        }
        log.info("Equipo encontrado: {}", id);
        return ResponseEntity.ok(equipo);
    }

    @PutMapping("/updateEquipo")
    public ResponseEntity<Equipo> updateEquipo(@RequestBody Equipo equipo) throws ResourceNotFoundException, GeneralException{
        try{
            Equipo existente = equipoServiceAPI.get(equipo.getIdEquipo());
            if(existente == null){
                log.warn("No encontrado: {}", equipo.getIdEquipo());
                throw new ResourceNotFoundException("Equipo no encontrado con id: " + equipo.getIdEquipo());
            }
            Equipo resultado = equipoServiceAPI.update(equipo);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdEquipo());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("EQUIPOS");
            audit.setIdRegistroAfectado(resultado.getIdEquipo());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            log.info("Equipo actualizado: {}", resultado.getIdEquipo());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            log.warn("No encontrado: {}", equipo.getIdEquipo());
            throw e;
        } catch (Exception e){
            log.error("Error al actualizar el equipo: {}", e.getMessage());
            throw new GeneralException("Error al actualizar el equipo: " + e.getMessage());
        }
    }

    @GetMapping("/findByGrupo/{idGrupo}")
    public ResponseEntity<List<Equipo>> getEquiposByGrupo(@PathVariable String idGrupo) {
        log.info("Buscando equipos por grupo: {}", idGrupo);
        return ResponseEntity.ok(equipoServiceAPI.findByIdGrupo(idGrupo));
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Equipo>> getEquiposByEstado(@PathVariable String estado) {
        log.info("Buscando equipos por estado: {}", estado);
        return ResponseEntity.ok(equipoServiceAPI.findByEstado(estado));
    }
}

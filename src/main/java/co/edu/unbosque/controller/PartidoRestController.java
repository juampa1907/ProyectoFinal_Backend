package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.PartidoServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partido")
@CrossOrigin(origins = "http://localhost:4200")
public class PartidoRestController {
    
    @Autowired
    private PartidoServiceAPI partidoServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Partido>> getAllPartidos() {
        return ResponseEntity.ok(partidoServiceAPI.getAll());
    }
    
    @PostMapping(value = "/savePartido")
    public ResponseEntity<Partido> savePartido(@RequestBody Partido partido) throws GeneralException {
        try{
            if(partido.getIdEquipoLocal() == null || partido.getIdEquipoVisitante() == null){
                throw new GeneralException("El partido debe tener un equipo local y un equipo visitante");
            }
            if(partido.getIdEquipoLocal().equals(partido.getIdEquipoVisitante())){
                throw new GeneralException("El equipo local y el equipo visitante no pueden ser el mismo");
            }
            Partido guardado = partidoServiceAPI.save(partido);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(guardado.getIdPartido());
            audit.setAccion("CREATE");
            audit.setTablaAfectada("PARTIDOS");
            audit.setIdRegistroAfectado(guardado.getIdPartido());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e){
            throw new GeneralException("Error al guardar el partido: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Partido> getPartidoById(@PathVariable Integer id) throws ResourceNotFoundException{
        Partido partido = partidoServiceAPI.get(id);
        if(partido == null){
            throw new ResourceNotFoundException("Partido no encontrado con id: " + id);
        }
        return ResponseEntity.ok(partido);
    }
    
    @DeleteMapping(value = "/deletePartido/{id}")
    public ResponseEntity<Void> deletePartido(@PathVariable Integer id) throws ResourceNotFoundException {
        Partido partido = partidoServiceAPI.get(id);
        if (partido == null){
            throw new ResourceNotFoundException("Partido no encontrado con id: " + id);
        }
        partidoServiceAPI.delete(id);
        Auditoria audit = new Auditoria();
        audit.setIdUsuario(id);
        audit.setAccion("DELETE");
        audit.setTablaAfectada("PARTIDOS");
        audit.setIdRegistroAfectado(id);
        audit.setIpCliente("127.0.0.1");
        auditoriaServiceAPI.save(audit);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updatePartido")
    public ResponseEntity<Partido> updatePartido(@RequestBody Partido partido) throws ResourceNotFoundException, GeneralException{
        try{
            Partido existente = partidoServiceAPI.get(partido.getIdPartido());
            if(existente == null){
                throw new ResourceNotFoundException("Partido no encontrado con id: " + partido.getIdPartido());
            }
            if (partido.getIdEquipoLocal() != null) existente.setIdEquipoLocal(partido.getIdEquipoLocal());
            if (partido.getIdEquipoVisitante() != null) existente.setIdEquipoVisitante(partido.getIdEquipoVisitante());
            if (partido.getFase() != null) existente.setFase(partido.getFase());
            if (partido.getGolesLocal() != null) existente.setGolesLocal(partido.getGolesLocal());
            if (partido.getGolesVisitante() != null) existente.setGolesVisitante(partido.getGolesVisitante());
            if (partido.getFechaHora() != null) existente.setFechaHora(partido.getFechaHora());
            if (partido.getEstado() != null) existente.setEstado(partido.getEstado());
            Partido resultado = partidoServiceAPI.update(existente);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdPartido());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("PARTIDOS");
            audit.setIdRegistroAfectado(resultado.getIdPartido());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el partido: " + e.getMessage());
        }
    }

    @GetMapping("/findByFase/{fase}")
    public ResponseEntity<List<Partido>> getPartidosByFase(@PathVariable String fase) {
        return ResponseEntity.ok(partidoServiceAPI.findByFase(fase));
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Partido>> getPartidosByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(partidoServiceAPI.findByEstado(estado));
    }
}

package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.service.api.EquipoServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipo")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipoRestController {
    
    @Autowired
    private EquipoServiceAPI equipoServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Equipo>> getAllEquipos() {
        return ResponseEntity.ok(equipoServiceAPI.getAll());
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Equipo> getEquipoById(@PathVariable Integer id) throws ResourceNotFoundException{
        Equipo equipo = equipoServiceAPI.get(id);
        if(equipo == null){
            throw new ResourceNotFoundException("Equipo no encontrado con id: " + id);
        }
        return ResponseEntity.ok(equipo);
    }

    @PutMapping("/updateEquipo")
    public ResponseEntity<Equipo> updateEquipo(@RequestBody Equipo equipo) throws ResourceNotFoundException, GeneralException{
        try{
            Equipo existente = equipoServiceAPI.get(equipo.getIdEquipo());
            if(existente == null){
                throw new ResourceNotFoundException("Equipo no encontrado con id: " + equipo.getIdEquipo());
            }
            if (equipo.getNombre() != null) existente.setNombre(equipo.getNombre());
            if (equipo.getIdGrupo() != null) existente.setIdGrupo(equipo.getIdGrupo());
            if (equipo.getEntrenador() != null) existente.setEntrenador(equipo.getEntrenador());
            if (equipo.getBandera() != null) existente.setBandera(equipo.getBandera());
            if (equipo.getEstado() != null) existente.setEstado(equipo.getEstado());
            Equipo resultado = equipoServiceAPI.update(existente);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el equipo: " + e.getMessage());
        }
    }

    @GetMapping("/findByGrupo/{idGrupo}")
    public ResponseEntity<List<Equipo>> getEquiposByGrupo(@PathVariable String idGrupo) {
        return ResponseEntity.ok(equipoServiceAPI.findByIdGrupo(idGrupo));
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Equipo>> getEquiposByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoServiceAPI.findByEstado(estado));
    }
}

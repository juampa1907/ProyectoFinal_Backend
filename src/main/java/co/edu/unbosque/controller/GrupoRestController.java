package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.service.api.GrupoServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupo")
@CrossOrigin(origins = "http://localhost:4200")
public class GrupoRestController {
    
    @Autowired
    private GrupoServiceAPI grupoServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        return ResponseEntity.ok(grupoServiceAPI.getAll());
    }
    
    @PostMapping(value = "/saveGrupo")
    public ResponseEntity<Grupo> saveGrupo(@RequestBody Grupo grupo) throws GeneralException, ResourceDuplicateException {
        try {
            boolean existeGrupo = grupoServiceAPI.existsByIdGrupo(grupo.getIdGrupo());
            if (existeGrupo) {
                throw new ResourceDuplicateException("Grupo " + grupo.getIdGrupo() + " ya existente");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(grupoServiceAPI.save(grupo));
        } catch (ResourceDuplicateException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al guardar el grupo: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteGrupo/{id}")
    public ResponseEntity<Void> deleteGrupo(@PathVariable String id) throws ResourceNotFoundException {
        Grupo grupo = grupoServiceAPI.get(id);
        if (grupo == null) {
            throw new ResourceNotFoundException("Grupo no encontrado con id: " + id);
        }
        grupoServiceAPI.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable String id) throws ResourceNotFoundException{
        Grupo grupo = grupoServiceAPI.get(id);
        if(grupo == null){
            throw new ResourceNotFoundException("Grupo no encontrado con id: " + id);
        }
        return ResponseEntity.ok(grupo);
    }

    @PutMapping("/updateGrupo")
    public ResponseEntity<Grupo> updateGrupo(@RequestBody Grupo grupo) throws ResourceNotFoundException, GeneralException{
        try{
            Grupo existente = grupoServiceAPI.get(grupo.getIdGrupo());
            if(existente == null){
                throw new ResourceNotFoundException("Grupo no encontrado con id: " + grupo.getIdGrupo());
            }
            if (grupo.getDescripcion() != null) existente.setDescripcion(grupo.getDescripcion());
            if (grupo.getEstado() != null) existente.setEstado(grupo.getEstado());
            Grupo resultado = grupoServiceAPI.update(existente);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el grupo: " + e.getMessage());
        }
    }
}

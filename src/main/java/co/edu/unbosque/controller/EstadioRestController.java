package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Estadio;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.EstadioServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadio")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadioRestController {

    @Autowired
    private EstadioServiceAPI estadioServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Estadio>> getAllEstadios() {
        return ResponseEntity.ok(estadioServiceAPI.getAll());
    }

    @PostMapping(value = "/saveEstadio")
    public ResponseEntity<Estadio> saveEstadio(@RequestBody Estadio estadio) throws GeneralException {
        try {
            Estadio guardado = estadioServiceAPI.save(estadio);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(guardado.getIdEstadio());
            audit.setAccion("CREATE");
            audit.setTablaAfectada("ESTADIOS");
            audit.setIdRegistroAfectado(guardado.getIdEstadio());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            throw new GeneralException("Error al guardar el estadio: " + e.getMessage());
        }
    }

    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Estadio> getEstadioById(@PathVariable Integer id) throws ResourceNotFoundException {
        Estadio estadio = estadioServiceAPI.get(id);
        if (estadio == null) {
            throw new ResourceNotFoundException("Estadio no encontrado con id: " + id);
        }
        return ResponseEntity.ok(estadio);
    }

    @DeleteMapping(value = "/deleteEstadio/{id}")
    public ResponseEntity<Void> deleteEstadio(@PathVariable Integer id) throws ResourceNotFoundException {
        Estadio estadio = estadioServiceAPI.get(id);
        if (estadio == null) {
            throw new ResourceNotFoundException("Estadio no encontrado con id: " + id);
        }
        estadioServiceAPI.delete(id);
        Auditoria audit = new Auditoria();
        audit.setIdUsuario(id);
        audit.setAccion("DELETE");
        audit.setTablaAfectada("ESTADIOS");
        audit.setIdRegistroAfectado(id);
        audit.setIpCliente("127.0.0.1");
        auditoriaServiceAPI.save(audit);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateEstadio")
    public ResponseEntity<Estadio> updateEstadio(@RequestBody Estadio estadio) throws ResourceNotFoundException, GeneralException {
        try {
            Estadio existente = estadioServiceAPI.get(estadio.getIdEstadio());
            if (existente == null) {
                throw new ResourceNotFoundException("Estadio no encontrado con id: " + estadio.getIdEstadio());
            }
            if (estadio.getDescripcion() != null) existente.setDescripcion(estadio.getDescripcion());
            if (estadio.getEstado() != null) existente.setEstado(estadio.getEstado());
            Estadio resultado = estadioServiceAPI.update(existente);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdEstadio());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("ESTADIOS");
            audit.setIdRegistroAfectado(resultado.getIdEstadio());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException("Error al actualizar el estadio: " + e.getMessage());
        }
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Estadio>> getEstadiosByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(estadioServiceAPI.findByEstado(estado));
    }
}

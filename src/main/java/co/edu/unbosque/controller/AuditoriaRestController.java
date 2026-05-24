package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "http://localhost:4200")
public class AuditoriaRestController {

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Auditoria>> getAllAuditorias() {
        log.info("Listando todas las auditorías");
        return ResponseEntity.ok(auditoriaServiceAPI.getAll());
    }

    @PutMapping("/updateAuditoria")
    public ResponseEntity<Auditoria> updateAuditoria(@RequestBody Auditoria auditoria) throws ResourceNotFoundException, GeneralException {
        try {
            Auditoria existente = auditoriaServiceAPI.get(auditoria.getIdLog());
            if (existente == null) {
                log.warn("No encontrada: {}", auditoria.getIdLog());
                throw new ResourceNotFoundException("Auditoría no encontrada con id: " + auditoria.getIdLog());
            }
            if (auditoria.getIdUsuario() != null) existente.setIdUsuario(auditoria.getIdUsuario());
            if (auditoria.getAccion() != null) existente.setAccion(auditoria.getAccion());
            if (auditoria.getTablaAfectada() != null) existente.setTablaAfectada(auditoria.getTablaAfectada());
            if (auditoria.getIdRegistroAfectado() != null) existente.setIdRegistroAfectado(auditoria.getIdRegistroAfectado());
            if (auditoria.getFechaHora() != null) existente.setFechaHora(auditoria.getFechaHora());
            if (auditoria.getIpCliente() != null) existente.setIpCliente(auditoria.getIpCliente());
            Auditoria resultado = auditoriaServiceAPI.update(existente);
            log.info("Auditoría actualizada: {}", resultado.getIdLog());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e) {
            log.warn("No encontrada: {}", auditoria.getIdLog());
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar la auditoría: {}", e.getMessage());
            throw new GeneralException("Error al actualizar la auditoría: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteAuditoria/{id}")
    public ResponseEntity<Void> deleteAuditoria(@PathVariable Long id) throws ResourceNotFoundException {
        Auditoria auditoria = auditoriaServiceAPI.get(id);
        if (auditoria == null) {
            log.warn("No encontrada: {}", id);
            throw new ResourceNotFoundException("Auditoría no encontrada con id: " + id);
        }
        auditoriaServiceAPI.delete(id);
        log.info("Auditoría eliminada: {}", id);
        return ResponseEntity.noContent().build();
    }
}

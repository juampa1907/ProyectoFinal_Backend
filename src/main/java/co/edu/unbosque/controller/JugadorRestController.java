package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.JugadorServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import co.edu.unbosque.utils.exception.ResourceDuplicateException;
import co.edu.unbosque.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jugador")
@CrossOrigin(origins = "http://localhost:4200")
public class JugadorRestController {
    
    @Autowired
    private JugadorServiceAPI jugadorServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Jugador>> getAllJugadores() {
        return ResponseEntity.ok(jugadorServiceAPI.getAll());
    }
    
    @PostMapping(value = "/saveJugador")
    public ResponseEntity<List<Jugador>> saveJugadores(@RequestBody List<Jugador> jugadores) throws GeneralException, ResourceDuplicateException {
        try{
            for (Jugador jugador : jugadores) {
                boolean existeJugador = jugadorServiceAPI.existsByNombre(jugador.getNombre());
                if(existeJugador){
                    throw new ResourceDuplicateException("Jugador " + jugador.getNombre() + " ya existente");
                }
            }
            List<Jugador> guardados = jugadorServiceAPI.saveAll(jugadores);
            for (Jugador jugador : guardados) {
                Auditoria audit = new Auditoria();
                audit.setIdUsuario(jugador.getIdJugador());
                audit.setAccion("CREATE");
                audit.setTablaAfectada("JUGADORES");
                audit.setIdRegistroAfectado(jugador.getIdJugador());
                audit.setIpCliente("127.0.0.1");
                auditoriaServiceAPI.save(audit);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(guardados);
        } catch (ResourceDuplicateException e) {
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al guardar los jugadores: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Integer id) throws ResourceNotFoundException{
        Jugador jugador = jugadorServiceAPI.get(id);
        if(jugador == null){
            throw new ResourceNotFoundException("Jugador no encontrado con id: " + id);
        }
        return ResponseEntity.ok(jugador);
    }
    
    @DeleteMapping(value = "/deleteJugador/{id}")
    public ResponseEntity<Void> deleteJugador(@PathVariable Integer id) throws ResourceNotFoundException {
        Jugador jugador = jugadorServiceAPI.get(id);
        if (jugador == null){
            throw new ResourceNotFoundException("Jugador no encontrado con id: " + id);
        }
        jugadorServiceAPI.delete(id);
        Auditoria audit = new Auditoria();
        audit.setIdUsuario(id);
        audit.setAccion("DELETE");
        audit.setTablaAfectada("JUGADORES");
        audit.setIdRegistroAfectado(id);
        audit.setIpCliente("127.0.0.1");
        auditoriaServiceAPI.save(audit);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateJugador")
    public ResponseEntity<Jugador> updateJugador(@RequestBody Jugador jugador) throws ResourceNotFoundException, GeneralException{
        try{
            Jugador existente = jugadorServiceAPI.get(jugador.getIdJugador());
            if(existente == null){
                throw new ResourceNotFoundException("Jugador no encontrado con id: " + jugador.getIdJugador());
            }
            if (jugador.getNombre() != null) existente.setNombre(jugador.getNombre());
            if (jugador.getIdEquipo() != null) existente.setIdEquipo(jugador.getIdEquipo());
            if (jugador.getPosicion() != null) existente.setPosicion(jugador.getPosicion());
            if (jugador.getNumeroCamiseta() != null) existente.setNumeroCamiseta(jugador.getNumeroCamiseta());
            if (jugador.getFechaNacimiento() != null) existente.setFechaNacimiento(jugador.getFechaNacimiento());
            if (jugador.getEstado() != null) existente.setEstado(jugador.getEstado());
            Jugador resultado = jugadorServiceAPI.update(existente);
            Auditoria audit = new Auditoria();
            audit.setIdUsuario(resultado.getIdJugador());
            audit.setAccion("UPDATE");
            audit.setTablaAfectada("JUGADORES");
            audit.setIdRegistroAfectado(resultado.getIdJugador());
            audit.setIpCliente("127.0.0.1");
            auditoriaServiceAPI.save(audit);
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new GeneralException("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    @GetMapping("/findByEquipo/{idEquipo}")
    public ResponseEntity<List<Jugador>> getJugadoresByEquipo(@PathVariable Integer idEquipo) {
        return ResponseEntity.ok(jugadorServiceAPI.findByIdEquipo(idEquipo));
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Jugador>> getJugadoresByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(jugadorServiceAPI.findByEstado(estado));
    }
}

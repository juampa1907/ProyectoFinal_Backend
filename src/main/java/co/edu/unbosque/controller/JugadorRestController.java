package co.edu.unbosque.controller;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.service.api.JugadorServiceAPI;
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
@RequestMapping("/api/jugador")
@CrossOrigin(origins = "http://localhost:4200")
public class JugadorRestController {
    
    @Autowired
    private JugadorServiceAPI jugadorServiceAPI;

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;
    
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Jugador>> getAllJugadores() {
        log.info("Listando todos los jugadores");
        return ResponseEntity.ok(jugadorServiceAPI.getAll());
    }
    
    @GetMapping(value = "/findRecord/{id}")
    public ResponseEntity<Jugador> getJugadorById(@PathVariable Integer id) throws ResourceNotFoundException{
        Jugador jugador = jugadorServiceAPI.get(id);
        if(jugador == null){
            log.warn("No encontrado: {}", id);
            throw new ResourceNotFoundException("Jugador no encontrado con id: " + id);
        }
        log.info("Jugador encontrado: {}", id);
        return ResponseEntity.ok(jugador);
    }

    @PutMapping("/updateJugador")
    public ResponseEntity<Jugador> updateJugador(@RequestBody Jugador jugador) throws ResourceNotFoundException, GeneralException{
        try{
            Jugador existente = jugadorServiceAPI.get(jugador.getIdJugador());
            if(existente == null){
                log.warn("No encontrado: {}", jugador.getIdJugador());
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
            log.info("Jugador actualizado: {}", resultado.getIdJugador());
            return ResponseEntity.ok(resultado);
        } catch (ResourceNotFoundException e){
            log.warn("No encontrado: {}", jugador.getIdJugador());
            throw e;
        } catch (Exception e){
            log.error("Error al actualizar el jugador: {}", e.getMessage());
            throw new GeneralException("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    @GetMapping("/findByEquipo/{idEquipo}")
    public ResponseEntity<List<Jugador>> getJugadoresByEquipo(@PathVariable Integer idEquipo) {
        log.info("Buscando jugadores por equipo: {}", idEquipo);
        return ResponseEntity.ok(jugadorServiceAPI.findByIdEquipo(idEquipo));
    }

    @GetMapping("/findByEstado/{estado}")
    public ResponseEntity<List<Jugador>> getJugadoresByEstado(@PathVariable String estado) {
        log.info("Buscando jugadores por estado: {}", estado);
        return ResponseEntity.ok(jugadorServiceAPI.findByEstado(estado));
    }
}

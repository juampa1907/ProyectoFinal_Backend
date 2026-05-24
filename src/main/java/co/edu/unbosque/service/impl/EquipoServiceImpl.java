package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.service.api.EquipoServiceAPI;
import co.edu.unbosque.service.api.JugadorServiceAPI;
import co.edu.unbosque.repository.EquipoRepository;
import java.util.List;

@Slf4j
@Service
public class EquipoServiceImpl extends GenericServiceImpl<Equipo, Integer> implements EquipoServiceAPI {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private JugadorServiceAPI jugadorServiceAPI;

    @Override
    public CrudRepository<Equipo, Integer> getDao() {
        log.debug("Obteniendo DAO de Equipo");
        return equipoRepository;
    }

    @Override
    public List<Equipo> findByIdGrupo(String idGrupo) {
        log.info("Buscando Equipos por idGrupo: {}", idGrupo);
        return equipoRepository.findByIdGrupo(idGrupo);
    }

    @Override
    public List<Equipo> findByEstado(String estado) {
        log.info("Buscando Equipos por estado: {}", estado);
        return equipoRepository.findByEstado(estado);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        log.info("Verificando si existe Equipo por nombre: {}", nombre);
        return equipoRepository.existsByNombre(nombre);
    }

    @Override
    public Equipo update(Equipo entity) {
        log.info("Actualizando Equipo con id: {}", entity.getIdEquipo());
        Equipo existente = get(entity.getIdEquipo());
        String estadoAnterior = existente.getEstado();

        if (entity.getNombre() != null) existente.setNombre(entity.getNombre());
        if (entity.getIdGrupo() != null) existente.setIdGrupo(entity.getIdGrupo());
        if (entity.getEntrenador() != null) existente.setEntrenador(entity.getEntrenador());
        if (entity.getEstado() != null) existente.setEstado(entity.getEstado());
        if (entity.getBandera() != null) existente.setBandera(entity.getBandera());

        Equipo equipoActualizado = super.update(existente);

        if (estadoAnterior != null && !estadoAnterior.equals(equipoActualizado.getEstado())) {
            List<Jugador> jugadores = jugadorServiceAPI.findByIdEquipo(entity.getIdEquipo());
            for (Jugador jugador : jugadores) {
                jugador.setEstado(equipoActualizado.getEstado());
                jugadorServiceAPI.update(jugador);
            }
        }

        return equipoActualizado;
    }
}
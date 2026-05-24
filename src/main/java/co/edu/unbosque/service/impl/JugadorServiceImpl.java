package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.service.api.JugadorServiceAPI;
import co.edu.unbosque.repository.JugadorRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class JugadorServiceImpl extends GenericServiceImpl<Jugador, Integer> implements JugadorServiceAPI {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    public CrudRepository<Jugador, Integer> getDao() {
        log.debug("Obteniendo DAO de Jugador");
        return jugadorRepository;
    }

    @Override
    public List<Jugador> findByIdEquipo(Integer idEquipo) {
        log.info("Buscando Jugadores por idEquipo: {}", idEquipo);
        return jugadorRepository.findByIdEquipo(idEquipo);
    }

    @Override
    public List<Jugador> findByEstado(String estado) {
        log.info("Buscando Jugadores por estado: {}", estado);
        return jugadorRepository.findByEstado(estado);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        log.info("Verificando si existe Jugador por nombre: {}", nombre);
        return jugadorRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional
    public List<Jugador> saveAll(List<Jugador> jugadores) {
        log.info("Guardando {} jugadores", jugadores.size());
        List<Jugador> result = new java.util.ArrayList<>();
        jugadorRepository.saveAll(jugadores).forEach(result::add);
        return result;
    }
}
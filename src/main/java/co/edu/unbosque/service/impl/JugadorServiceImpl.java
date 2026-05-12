package co.edu.unbosque.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.service.api.JugadorServiceAPI;
import co.edu.unbosque.repository.JugadorRepository;
import java.util.List;

@Service
public class JugadorServiceImpl extends GenericServiceImpl<Jugador, Integer> implements JugadorServiceAPI {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    public CrudRepository<Jugador, Integer> getDao() {
        return jugadorRepository;
    }

    @Override
    public List<Jugador> findByIdEquipo(Integer idEquipo) {
        return jugadorRepository.findByIdEquipo(idEquipo);
    }

    @Override
    public List<Jugador> findByEstado(String estado) {
        return jugadorRepository.findByEstado(estado);
    }
}
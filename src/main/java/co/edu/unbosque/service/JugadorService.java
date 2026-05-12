package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.repository.JugadorRepository;
import java.util.List;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    public List<Jugador> getAll() {
        List<Jugador> lista = new java.util.ArrayList<>();
        jugadorRepository.findAll().forEach(lista::add);
        return lista;
    }

    public List<Jugador> findByIdEquipo(Integer idEquipo) {
        return jugadorRepository.findByIdEquipo(idEquipo);
    }

    public List<Jugador> findByEstado(String estado) {
        return jugadorRepository.findByEstado(estado);
    }

    public Jugador save(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public void delete(Integer id) {
        jugadorRepository.deleteById(id);
    }

    public Jugador get(Integer id) {
        return jugadorRepository.findById(id).orElse(null);
    }
}
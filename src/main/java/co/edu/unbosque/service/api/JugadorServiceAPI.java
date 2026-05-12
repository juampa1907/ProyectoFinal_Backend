package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Jugador;
import java.util.List;

public interface JugadorServiceAPI extends GenericServiceAPI<Jugador, Integer> {
    List<Jugador> findByIdEquipo(Integer idEquipo);
    List<Jugador> findByEstado(String estado);
}
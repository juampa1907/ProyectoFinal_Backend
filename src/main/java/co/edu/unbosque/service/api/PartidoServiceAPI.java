package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Partido;
import java.util.List;

public interface PartidoServiceAPI extends GenericServiceAPI<Partido, Integer> {
    List<Partido> findByFase(String fase);
    List<Partido> findByEstado(String estado);
}
package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Estadio;
import java.util.List;

public interface EstadioServiceAPI extends GenericServiceAPI<Estadio, Integer> {
    List<Estadio> findByEstado(String estado);
}

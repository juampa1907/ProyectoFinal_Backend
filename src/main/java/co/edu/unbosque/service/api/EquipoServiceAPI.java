package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Equipo;
import java.util.List;

public interface EquipoServiceAPI extends GenericServiceAPI<Equipo, Integer> {
    List<Equipo> findByIdGrupo(String idGrupo);
    List<Equipo> findByEstado(String estado);
    boolean existsByNombre(String nombre);
}
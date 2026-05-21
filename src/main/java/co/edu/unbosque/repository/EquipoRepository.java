package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Equipo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipoRepository extends CrudRepository<Equipo, Integer> {
    List<Equipo> findByIdGrupo(String idGrupo);
    List<Equipo> findByEstado(String estado);
    boolean existsByNombre(String nombre);
}
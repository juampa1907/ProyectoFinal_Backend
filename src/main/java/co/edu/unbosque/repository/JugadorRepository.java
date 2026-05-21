package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Jugador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JugadorRepository extends CrudRepository<Jugador, Integer> {
    List<Jugador> findByIdEquipo(Integer idEquipo);
    List<Jugador> findByEstado(String estado);
    boolean existsByNombre(String nombre);
}
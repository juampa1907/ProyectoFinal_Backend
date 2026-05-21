package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Estadio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstadioRepository extends CrudRepository<Estadio, Integer> {
    List<Estadio> findByEstado(String estado);
}

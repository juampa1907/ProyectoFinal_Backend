package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Partido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PartidoRepository extends CrudRepository<Partido, Integer> {
    List<Partido> findByFase(String fase);
    List<Partido> findByEstado(String estado);
}
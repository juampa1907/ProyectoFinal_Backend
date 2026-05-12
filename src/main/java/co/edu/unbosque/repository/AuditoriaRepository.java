package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Auditoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends CrudRepository<Auditoria, Long> {
    List<Auditoria> findByIdUsuario(Integer idUsuario);
}
package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Auditoria;
import java.util.List;

public interface AuditoriaServiceAPI extends GenericServiceAPI<Auditoria, Long> {
    List<Auditoria> findByIdUsuario(Integer idUsuario);
}
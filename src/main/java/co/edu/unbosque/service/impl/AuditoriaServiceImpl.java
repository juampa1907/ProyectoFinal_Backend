package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.service.api.AuditoriaServiceAPI;
import co.edu.unbosque.repository.AuditoriaRepository;
import java.util.List;

@Slf4j
@Service
public class AuditoriaServiceImpl extends GenericServiceImpl<Auditoria, Long> implements AuditoriaServiceAPI {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Override
    public CrudRepository<Auditoria, Long> getDao() {
        log.debug("Obteniendo DAO de Auditoria");
        return auditoriaRepository;
    }

    @Override
    public List<Auditoria> findByIdUsuario(Integer idUsuario) {
        log.info("Buscando Auditoria por idUsuario: {}", idUsuario);
        return auditoriaRepository.findByIdUsuario(idUsuario);
    }
}
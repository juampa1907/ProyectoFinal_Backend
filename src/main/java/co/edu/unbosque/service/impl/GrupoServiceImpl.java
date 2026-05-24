package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.service.api.GrupoServiceAPI;
import co.edu.unbosque.repository.GrupoRepository;

@Slf4j
@Service
public class GrupoServiceImpl extends GenericServiceImpl<Grupo, String> implements GrupoServiceAPI {

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    public CrudRepository<Grupo, String> getDao() {
        log.debug("Obteniendo DAO de Grupo");
        return grupoRepository;
    }

    @Override
    public boolean existsByIdGrupo(String idGrupo) {
        log.info("Verificando si existe Grupo por idGrupo: {}", idGrupo);
        return grupoRepository.existsByIdGrupo(idGrupo);
    }
}
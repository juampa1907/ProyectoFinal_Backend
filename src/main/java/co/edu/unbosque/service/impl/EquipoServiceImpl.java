package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.service.api.EquipoServiceAPI;
import co.edu.unbosque.repository.EquipoRepository;
import java.util.List;

@Slf4j
@Service
public class EquipoServiceImpl extends GenericServiceImpl<Equipo, Integer> implements EquipoServiceAPI {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public CrudRepository<Equipo, Integer> getDao() {
        log.debug("Obteniendo DAO de Equipo");
        return equipoRepository;
    }

    @Override
    public List<Equipo> findByIdGrupo(String idGrupo) {
        log.info("Buscando Equipos por idGrupo: {}", idGrupo);
        return equipoRepository.findByIdGrupo(idGrupo);
    }

    @Override
    public List<Equipo> findByEstado(String estado) {
        log.info("Buscando Equipos por estado: {}", estado);
        return equipoRepository.findByEstado(estado);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        log.info("Verificando si existe Equipo por nombre: {}", nombre);
        return equipoRepository.existsByNombre(nombre);
    }
}
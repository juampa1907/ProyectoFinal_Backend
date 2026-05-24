package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.service.api.PartidoServiceAPI;
import co.edu.unbosque.repository.PartidoRepository;
import java.util.List;

@Slf4j
@Service
public class PartidoServiceImpl extends GenericServiceImpl<Partido, Integer> implements PartidoServiceAPI {

    @Autowired
    private PartidoRepository partidoRepository;

    @Override
    public CrudRepository<Partido, Integer> getDao() {
        log.debug("Obteniendo DAO de Partido");
        return partidoRepository;
    }

    @Override
    public List<Partido> findByFase(String fase) {
        log.info("Buscando Partidos por fase: {}", fase);
        return partidoRepository.findByFase(fase);
    }

    @Override
    public List<Partido> findByEstado(String estado) {
        log.info("Buscando Partidos por estado: {}", estado);
        return partidoRepository.findByEstado(estado);
    }
}

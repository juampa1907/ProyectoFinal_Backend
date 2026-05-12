package co.edu.unbosque.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.service.api.PartidoServiceAPI;
import co.edu.unbosque.repository.PartidoRepository;
import java.util.List;

@Service
public class PartidoServiceImpl extends GenericServiceImpl<Partido, Integer> implements PartidoServiceAPI {

    @Autowired
    private PartidoRepository partidoRepository;

    @Override
    public CrudRepository<Partido, Integer> getDao() {
        return partidoRepository;
    }

    @Override
    public List<Partido> findByFase(String fase) {
        return partidoRepository.findByFase(fase);
    }

    @Override
    public List<Partido> findByEstado(String estado) {
        return partidoRepository.findByEstado(estado);
    }
}

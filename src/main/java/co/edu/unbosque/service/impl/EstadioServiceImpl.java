package co.edu.unbosque.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Estadio;
import co.edu.unbosque.service.api.EstadioServiceAPI;
import co.edu.unbosque.repository.EstadioRepository;
import java.util.List;

@Service
public class EstadioServiceImpl extends GenericServiceImpl<Estadio, Integer> implements EstadioServiceAPI {

    @Autowired
    private EstadioRepository estadioRepository;

    @Override
    public CrudRepository<Estadio, Integer> getDao() {
        return estadioRepository;
    }

    @Override
    public List<Estadio> findByEstado(String estado) {
        return estadioRepository.findByEstado(estado);
    }
}

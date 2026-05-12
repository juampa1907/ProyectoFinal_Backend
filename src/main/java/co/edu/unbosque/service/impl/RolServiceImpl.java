package co.edu.unbosque.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Rol;
import co.edu.unbosque.service.api.RolServiceAPI;
import co.edu.unbosque.repository.RolRepository;

@Service
public class RolServiceImpl extends GenericServiceImpl<Rol, Integer> implements RolServiceAPI {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public CrudRepository<Rol, Integer> getDao() {
        return rolRepository;
    }
}
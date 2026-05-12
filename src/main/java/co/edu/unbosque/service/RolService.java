package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Rol;
import co.edu.unbosque.repository.RolRepository;
import java.util.List;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> getAll() {
        List<Rol> lista = new java.util.ArrayList<>();
        rolRepository.findAll().forEach(lista::add);
        return lista;
    }

    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    public void delete(Integer id) {
        rolRepository.deleteById(id);
    }

    public Rol get(Integer id) {
        return rolRepository.findById(id).orElse(null);
    }
}
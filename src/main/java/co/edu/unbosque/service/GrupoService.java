package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Grupo;
import co.edu.unbosque.repository.GrupoRepository;
import java.util.List;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    public List<Grupo> getAll() {
        List<Grupo> lista = new java.util.ArrayList<>();
        grupoRepository.findAll().forEach(lista::add);
        return lista;
    }

    public Grupo save(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public void delete(String id) {
        grupoRepository.deleteById(id);
    }

    public Grupo get(String id) {
        return grupoRepository.findById(id).orElse(null);
    }
}
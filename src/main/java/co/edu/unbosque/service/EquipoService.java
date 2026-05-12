package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.repository.EquipoRepository;
import java.util.List;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    public List<Equipo> getAll() {
        List<Equipo> lista = new java.util.ArrayList<>();
        equipoRepository.findAll().forEach(lista::add);
        return lista;
    }

    public List<Equipo> findByIdGrupo(String idGrupo) {
        return equipoRepository.findByIdGrupo(idGrupo);
    }

    public List<Equipo> findByEstado(String estado) {
        return equipoRepository.findByEstado(estado);
    }

    public Equipo save(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public void delete(Integer id) {
        equipoRepository.deleteById(id);
    }

    public Equipo get(Integer id) {
        return equipoRepository.findById(id).orElse(null);
    }
}
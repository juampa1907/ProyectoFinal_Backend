package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.repository.PartidoRepository;
import java.util.List;

@Service
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    public List<Partido> getAll() {
        List<Partido> lista = new java.util.ArrayList<>();
        partidoRepository.findAll().forEach(lista::add);
        return lista;
    }

    public List<Partido> findByFase(String fase) {
        return partidoRepository.findByFase(fase);
    }

    public List<Partido> findByEstado(String estado) {
        return partidoRepository.findByEstado(estado);
    }

    public Partido save(Partido partido) {
        return partidoRepository.save(partido);
    }

    public void delete(Integer id) {
        partidoRepository.deleteById(id);
    }

    public Partido get(Integer id) {
        return partidoRepository.findById(id).orElse(null);
    }
}
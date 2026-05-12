package co.edu.unbosque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.repository.AuditoriaRepository;
import java.util.List;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public List<Auditoria> getAll() {
        List<Auditoria> lista = new java.util.ArrayList<>();
        auditoriaRepository.findAll().forEach(lista::add);
        return lista;
    }

    public List<Auditoria> findByIdUsuario(Integer idUsuario) {
        return auditoriaRepository.findByIdUsuario(idUsuario);
    }

    public Auditoria save(Auditoria auditoria) {
        return auditoriaRepository.save(auditoria);
    }

    public void delete(Long id) {
        auditoriaRepository.deleteById(id);
    }

    public Auditoria get(Long id) {
        return auditoriaRepository.findById(id).orElse(null);
    }
}
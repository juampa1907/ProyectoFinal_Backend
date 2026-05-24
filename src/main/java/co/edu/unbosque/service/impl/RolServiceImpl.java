package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.entity.Rol;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.RolServiceAPI;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.repository.RolRepository;

import java.util.List;

@Slf4j
@Service
public class RolServiceImpl extends GenericServiceImpl<Rol, Integer> implements RolServiceAPI {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;

    @Override
    public CrudRepository<Rol, Integer> getDao() {
        log.debug("Obteniendo DAO de Rol");
        return rolRepository;
    }

    @Override
    public boolean existsByNombreRol(String nombreRol) {
        log.info("Verificando si existe Rol por nombreRol: {}", nombreRol);
        return rolRepository.existsByNombreRol(nombreRol);
    }

    @Override
    public Rol update(Rol entity) {
        log.info("Actualizando Rol con id: {}", entity.getIdRol());
        Rol existente = get(entity.getIdRol());
        String estadoAnterior = existente.getEstado();

        if (entity.getNombreRol() != null) existente.setNombreRol(entity.getNombreRol());
        if (entity.getEstado() != null) existente.setEstado(entity.getEstado());

        Rol rolActualizado = super.update(existente);

        if (estadoAnterior != null && !estadoAnterior.equals(rolActualizado.getEstado())) {
            List<Usuario> usuarios = usuarioServiceAPI.findByIdRol(entity.getIdRol());
            for (Usuario usuario : usuarios) {
                usuario.setEstado(rolActualizado.getEstado());
                usuarioServiceAPI.update(usuario);
            }
        }

        return rolActualizado;
    }
}
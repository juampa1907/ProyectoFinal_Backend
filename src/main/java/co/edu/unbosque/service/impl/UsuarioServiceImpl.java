package co.edu.unbosque.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import co.edu.unbosque.utils.GenericServiceImpl;
import co.edu.unbosque.utils.HashUtil;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.UsuarioServiceAPI;
import co.edu.unbosque.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario, Integer> implements UsuarioServiceAPI {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public CrudRepository<Usuario, Integer> getDao() {
        log.debug("Obteniendo DAO de Usuario");
        return usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        log.info("Guardando Usuario: {}", usuario.getUsername());
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(HashUtil.hashSHA1(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        log.info("Actualizando Usuario con id: {}", usuario.getIdUsuario());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByUsernameAndPassword(String username, String password) {
        log.info("Buscando Usuario por username: {}", username);
        return usuarioRepository.findByUsernameAndPassword(username, HashUtil.hashSHA1(password));
    }

    @Override
    public Optional<Usuario> findByCorreoAndPassword(String correo, String password) {
        log.info("Buscando Usuario por correo: {}", correo);
        return usuarioRepository.findByCorreoAndPassword(correo, HashUtil.hashSHA1(password));
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        log.info("Buscando Usuario por username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        log.info("Buscando Usuario por correo: {}", correo);
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("Verificando si existe Usuario por username: {}", username);
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByNombreApellido(String nombreApellido) {
        log.info("Verificando si existe Usuario por nombreApellido: {}", nombreApellido);
        return usuarioRepository.existsByNombreApellido(nombreApellido);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        log.info("Verificando si existe Usuario por correo: {}", correo);
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    public List<Usuario> findByIdRol(Integer idRol) {
        log.info("Buscando Usuarios por idRol: {}", idRol);
        return usuarioRepository.findByIdRol(idRol);
    }
}

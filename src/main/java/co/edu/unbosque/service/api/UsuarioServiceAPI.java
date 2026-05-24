package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioServiceAPI extends GenericServiceAPI<Usuario, Integer> {
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByUsername(String username);
    boolean existsByNombreApellido(String nombreApellido);
    boolean existsByCorreo(String correo);
    List<Usuario> findByIdRol(Integer idRol);
}
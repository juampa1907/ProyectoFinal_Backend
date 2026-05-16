package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.GenericServiceAPI;
import co.edu.unbosque.entity.Usuario;
import java.util.Optional;

public interface UsuarioServiceAPI extends GenericServiceAPI<Usuario, Integer> {
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByNombreApellido(String nombreApellido);
}
package co.edu.unbosque.repository;

import co.edu.unbosque.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByNombreApellido(String nombreApellido);
    boolean existsByCorreo(String correo);
    List<Usuario> findByIdRol(Integer idRol);
}
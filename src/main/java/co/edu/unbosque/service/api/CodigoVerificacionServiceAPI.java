package co.edu.unbosque.service.api;

public interface CodigoVerificacionServiceAPI {

    String generarCodigo();

    void guardarCodigo(String correo, String codigo);

    boolean validarCodigo(String correo, String codigo);

    void eliminarCodigo(String correo);

    boolean verificarCodigoSinConsumir(String correo, String codigo);

    void marcarValidado(String username);

    boolean estaValidado(String username);

    void limpiarValidacion(String username);

}

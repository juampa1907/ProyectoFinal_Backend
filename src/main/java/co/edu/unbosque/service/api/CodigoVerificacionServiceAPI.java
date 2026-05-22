package co.edu.unbosque.service.api;

public interface CodigoVerificacionServiceAPI {

    String generarCodigo();

    void guardarCodigo(String correo, String codigo);

    boolean validarCodigo(String correo, String codigo);

    void eliminarCodigo(String correo);

}

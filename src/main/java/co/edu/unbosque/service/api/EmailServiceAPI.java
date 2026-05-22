package co.edu.unbosque.service.api;

import co.edu.unbosque.entity.Usuario;

public interface EmailServiceAPI {

    void enviarCodigoVerificacion(String destinatario, String codigo);

    void enviarCredenciales(Usuario usuario, String passwordOriginal);

}

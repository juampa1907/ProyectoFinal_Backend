package co.edu.unbosque.service.impl;

import co.edu.unbosque.service.api.CodigoVerificacionServiceAPI;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodigoVerificacionServiceImpl implements CodigoVerificacionServiceAPI {

    private static final long EXPIRACION_MINUTOS = 3;
    private final Map<String, CodigoData> codigos = new ConcurrentHashMap<>();

    @Override
    public String generarCodigo() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

    @Override
    public void guardarCodigo(String correo, String codigo) {
        CodigoData data = new CodigoData(codigo, LocalDateTime.now().plusMinutes(EXPIRACION_MINUTOS));
        codigos.put(correo, data);
    }

    @Override
    public boolean validarCodigo(String correo, String codigo) {
        CodigoData data = codigos.get(correo);
        if (data == null) {
            return false;
        }
        if (data.isUsado()) {
            return false;
        }
        if (LocalDateTime.now().isAfter(data.getExpiracion())) {
            codigos.remove(correo);
            return false;
        }
        if (!data.getCodigo().equals(codigo)) {
            return false;
        }
        data.setUsado(true);
        return true;
    }

    @Override
    public void eliminarCodigo(String correo) {
        codigos.remove(correo);
    }

    private static class CodigoData {

        private final String codigo;
        private final LocalDateTime expiracion;
        private boolean usado;

        public CodigoData(String codigo, LocalDateTime expiracion) {
            this.codigo = codigo;
            this.expiracion = expiracion;
            this.usado = false;
        }

        public String getCodigo() {
            return codigo;
        }

        public LocalDateTime getExpiracion() {
            return expiracion;
        }

        public boolean isUsado() {
            return usado;
        }

        public void setUsado(boolean usado) {
            this.usado = usado;
        }

    }

}

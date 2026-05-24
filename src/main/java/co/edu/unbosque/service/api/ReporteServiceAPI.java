package co.edu.unbosque.service.api;

import co.edu.unbosque.utils.exception.GeneralException;

public interface ReporteServiceAPI {

    byte[] generarReporte() throws GeneralException;

}

package co.edu.unbosque.controller;

import co.edu.unbosque.service.api.ReporteServiceAPI;
import co.edu.unbosque.utils.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reporte")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteRestController {

    @Autowired
    private ReporteServiceAPI reporteServiceAPI;

    @GetMapping(value = "/generar", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarReporte() throws GeneralException {
        byte[] pdf = reporteServiceAPI.generarReporte();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reporte_mundial2026.pdf")
                .body(pdf);
    }

}

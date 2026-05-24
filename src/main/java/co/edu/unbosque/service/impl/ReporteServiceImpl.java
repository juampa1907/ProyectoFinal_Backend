package co.edu.unbosque.service.impl;

import co.edu.unbosque.entity.Auditoria;
import co.edu.unbosque.entity.Equipo;
import co.edu.unbosque.entity.Jugador;
import co.edu.unbosque.entity.Partido;
import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.*;
import co.edu.unbosque.utils.exception.GeneralException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteServiceAPI {

    @Autowired
    private AuditoriaServiceAPI auditoriaServiceAPI;

    @Autowired
    private UsuarioServiceAPI usuarioServiceAPI;

    @Autowired
    private EquipoServiceAPI equipoServiceAPI;

    @Autowired
    private JugadorServiceAPI jugadorServiceAPI;

    @Autowired
    private PartidoServiceAPI partidoServiceAPI;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public byte[] generarReporte() throws GeneralException {
        try {
            List<Auditoria> auditorias = auditoriaServiceAPI.getAll();
            List<Usuario> usuarios = usuarioServiceAPI.getAll();
            List<Equipo> equipos = equipoServiceAPI.getAll();
            List<Jugador> jugadores = jugadorServiceAPI.getAll();
            List<Partido> partidos = partidoServiceAPI.getAll();

            long usuariosActivos = usuarios.stream().filter(u -> "A".equals(u.getEstado())).count();
            long usuariosInactivos = usuarios.size() - usuariosActivos;
            long equiposActivos = equipos.stream().filter(e -> "A".equals(e.getEstado())).count();
            long jugadoresActivos = jugadores.stream().filter(j -> "A".equals(j.getEstado())).count();
            long partidosActivos = partidos.stream().filter(p -> "A".equals(p.getEstado())).count();

            List<String> fases = partidos.stream()
                    .map(Partido::getFase)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            String chartAcciones = generarChartAcciones(auditorias);
            String chartPartidosFase = generarChartPartidosFase(partidos);
            String chartEquiposGrupo = generarChartEquiposGrupo(equipos);
            String chartJugadoresEquipo = generarChartJugadoresEquipo(jugadores, equipos);

            Context context = new Context();
            context.setVariable("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            context.setVariable("usuariosActivos", usuariosActivos);
            context.setVariable("usuariosInactivos", usuariosInactivos);
            context.setVariable("equiposActivos", equiposActivos);
            context.setVariable("jugadoresActivos", jugadoresActivos);
            context.setVariable("partidosActivos", partidosActivos);
            context.setVariable("totalAuditorias", (long) auditorias.size());
            context.setVariable("fases", fases);
            context.setVariable("auditorias", auditorias);
            context.setVariable("chartAcciones", chartAcciones);
            context.setVariable("chartPartidosFase", chartPartidosFase);
            context.setVariable("chartEquiposGrupo", chartEquiposGrupo);
            context.setVariable("chartJugadoresEquipo", chartJugadoresEquipo);

            String html = templateEngine.process("reporte", context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new GeneralException("Error al generar el reporte: " + e.getMessage());
        }
    }

    private String generarChartAcciones(List<Auditoria> auditorias) {
        Map<String, Long> conteo = auditorias.stream()
                .collect(Collectors.groupingBy(Auditoria::getAccion, Collectors.counting()));

        DefaultPieDataset dataset = new DefaultPieDataset();
        conteo.forEach((accion, count) -> dataset.setValue(accion, count));

        JFreeChart chart = ChartFactory.createPieChart(
                null, dataset, false, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(new Color(0, 0, 0, 0));
        plot.setSectionPaint("CREATE", new Color(39, 174, 96));
        plot.setSectionPaint("UPDATE", new Color(46, 134, 193));
        plot.setSectionPaint("DELETE", new Color(231, 76, 60));
        plot.setSectionPaint("LOGIN", new Color(142, 68, 173));
        plot.setSectionPaint("UPDATE", new Color(46, 134, 193));

        PieSectionLabelGenerator labelGen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})");
        plot.setLabelGenerator(labelGen);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setSimpleLabels(true);

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5));

        return chartToBase64(chart, 650, 400);
    }

    private String generarChartPartidosFase(List<Partido> partidos) {
        Map<String, Long> conteo = partidos.stream()
                .filter(p -> p.getFase() != null)
                .collect(Collectors.groupingBy(Partido::getFase, Collectors.counting()));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        conteo.forEach((fase, count) -> dataset.addValue(count, "Partidos", fase));

        JFreeChart chart = ChartFactory.createBarChart(
                null, null, "Cantidad", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(46, 134, 193));
        renderer.setMaximumBarWidth(0.12);
        renderer.setShadowPaint(new Color(0, 0, 0, 0));

        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));

        return chartToBase64(chart, 650, 400);
    }

    private String generarChartEquiposGrupo(List<Equipo> equipos) {
        Map<String, Long> conteo = equipos.stream()
                .filter(e -> e.getIdGrupo() != null)
                .collect(Collectors.groupingBy(Equipo::getIdGrupo, Collectors.counting()));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        conteo.forEach((grupo, count) -> dataset.addValue(count, "Equipos", grupo));

        JFreeChart chart = ChartFactory.createBarChart(
                null, "Grupo", "Cantidad", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(39, 174, 96));
        renderer.setMaximumBarWidth(0.12);
        renderer.setShadowPaint(new Color(0, 0, 0, 0));

        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));

        return chartToBase64(chart, 650, 400);
    }

    private String generarChartJugadoresEquipo(List<Jugador> jugadores, List<Equipo> equipos) {
        Map<Integer, Long> conteo = jugadores.stream()
                .filter(j -> j.getIdEquipo() != null)
                .collect(Collectors.groupingBy(Jugador::getIdEquipo, Collectors.counting()));

        Map<Integer, String> nombresEquipos = equipos.stream()
                .collect(Collectors.toMap(Equipo::getIdEquipo, Equipo::getNombre, (a, b) -> a));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        conteo.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> {
                    String nombre = nombresEquipos.getOrDefault(entry.getKey(), "Equipo #" + entry.getKey());
                    if (nombre.length() > 15) nombre = nombre.substring(0, 15) + "...";
                    dataset.addValue(entry.getValue(), "Jugadores", nombre);
                });

        JFreeChart chart = ChartFactory.createBarChart(
                null, "Equipo", "Cantidad", dataset,
                PlotOrientation.HORIZONTAL, false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.setPadding(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(231, 76, 60));
        renderer.setMaximumBarWidth(0.12);
        renderer.setShadowPaint(new Color(0, 0, 0, 0));

        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 11));
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));

        return chartToBase64(chart, 650, 400);
    }

    private String chartToBase64(JFreeChart chart, int width, int height) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(baos, chart, width, height);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }

}

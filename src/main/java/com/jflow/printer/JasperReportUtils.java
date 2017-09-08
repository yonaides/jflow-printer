/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import java.sql.Connection;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hectorvent@gmail.com
 */
public class JasperReportUtils {

    private static final Map<String, Object> PARAMETERS = new HashMap();
    private static final Logger LOG = LogManager.getLogger(JasperReportUtils.class);
    private String jasperResource;
    private File fileJasper;

    public static JasperReportUtils newReport() {
        return new JasperReportUtils();
    }

    public String getJasperResource() {
        return jasperResource;
    }

    public JasperReportUtils setJasperResource(String jasperResource) {
        this.jasperResource = jasperResource;
        return this;
    }

    public JasperReportUtils put(String key, Object value) {
        PARAMETERS.put(key, value);
        return this;
    }

    public JasperReportUtils putAll(Map<String, Object> values) {
        PARAMETERS.putAll(values);
        return this;
    }

    public JasperReportUtils clearAll() {
        PARAMETERS.clear();
        return this;
    }

    public File getFileJasper() {
        return fileJasper;
    }

    public JasperReportUtils setFileJasper(File fileJasper) {
        this.fileJasper = fileJasper;
        return this;
    }

    public void printOnPrinter(String name) throws JasperReportExcepcion {

        // Se lista todos los printers 
        PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService p = null;

        for (PrintService ps : pss) {

            LOG.info("[IMPRESORA INSTALADA]: '{}' [IMPRESORA DETECTADA]: '{}'", ps.getName(), name);
            if (ps.getName().equals(name)) {
                LOG.info("[PRINT SERVICE IMPRESORA] - Nombre coincide / '{}'", name);
                p = ps;
                break;
            }
        }

        if (p == null) {
            LOG.info("[PRINT SERVICE NULO]");
            for (PrintService printService1 : pss) {
                if (printService1.getName().contains("Star TSP")) {
                    p = printService1;
                    break;
                }
            }
        }

        printOnService(p);

    }

    public void printOnService(PrintService ps) throws JasperReportExcepcion {

        LOG.info("[EJECUTANDO PRINT SERVICE]");
        InputStream is = null;

        if (fileJasper != null && fileJasper.exists()) {
            try {
                is = new FileInputStream(fileJasper);
            } catch (FileNotFoundException ex) {
                LOG.log(Level.FATAL, "Archivo no encontrado {0}", ex);
            }

        } else {
            is = JasperPrint.class.getResourceAsStream(jasperResource);
        }

        Objects.requireNonNull(is, "JasperFile not found!");

        try {
            JasperPrint jp = JasperFillManager.fillReport(is, PARAMETERS, (Connection) null);

            System.out.println("JasperPrint = " + jp);

            JRPrintServiceExporter jPrinter = new JRPrintServiceExporter();
            
            printPreview(jp);

            /*jPrinter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            if (ps != null) {
                jPrinter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, ps);
            }
            jPrinter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, true);
            jPrinter.exportReport();*/
        } catch (JRException ex) {
            throw new JasperReportExcepcion("Error al ejecutar reporte ", ex);
        }
    }

    public void printPreview(JasperPrint jp) {

        try {

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new File("ticket.pdf"));

            try {
                exporter.exportReport();
            } catch (JRException ex) {
                java.util.logging.Logger.getLogger(JasperReportUtils.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {

        }

    }

}

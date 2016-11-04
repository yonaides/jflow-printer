/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
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

//        if (p != null) {
        printOnService(p);
//        }

    }

    public void printOnService(PrintService ps) throws JasperReportExcepcion {

        LOG.info("[EJECUTANDO PRINT SERVICE]");
        InputStream is = null;

        if (fileJasper != null && fileJasper.exists()) {
            try {
                is = new FileInputStream(fileJasper);
            } catch (FileNotFoundException ex) {
            }

        } else {
            is = JasperPrint.class.getResourceAsStream(jasperResource);
        }

        Objects.requireNonNull(is, "JasperFile not found!");

        try {
            JasperPrint jp = JasperFillManager.fillReport(is, PARAMETERS, (Connection) null);
            JRPrintServiceExporter jPrinter = new JRPrintServiceExporter();
            jPrinter.setParameter(JRExporterParameter.JASPER_PRINT, jp);

            if (ps != null) {
                jPrinter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, ps);
            }

            jPrinter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
            jPrinter.exportReport();

        } catch (JRException ex) {
            throw new JasperReportExcepcion("Error al ejecutar reporte ", ex);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import com.jflow.jflowcore.Message;
import com.jflow.jflowcore.MessageListener;
import com.jflow.jflowcore.MessageType;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hventurar@edenorte.com.do
 */
public class TicketPrint implements MessageListener {

    private static final String JASPER_RESOURCE = "/jaspers/ticket.jasper";
    private static final Logger LOG = LogManager.getLogger(TicketPrint.class);

    @Override
    public void onMessage(Message message) {

        LOG.info("MENSAJE: {}", message.toString());

        if (!MessageType.PRINT.equals(message.getTipoMensaje())) {
            return;
        }

        Map<String, Object> params = new HashMap();
        message.getMensaje()
                .entrySet()
                .stream()
                .forEach((entry) -> {
                    params.put(entry.getKey(), entry.getValue().getAsString());
                });

        LOG.info("[IMPRESORA] {}", "");

        JasperReportUtils jp = JasperReportUtils.newReport()
                .putAll(params);

        // Utilizar archivo para los tickets
        File file = new File(TicketDownloader.FILE_TICKET);
        if (file.exists()) {
            jp.setFileJasper(file);
        } else {
            jp.setJasperResource(JASPER_RESOURCE);
        }

        try {
            jp.printOnPrinter("");
        } catch (JasperReportExcepcion ex) {
            LOG.error("Error Imprimiendo ", ex);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import com.google.gson.JsonElement;
import com.jflow.jflowcore.JflowCoreWS;
import com.jflow.jflowcore.Message;
import com.jflow.jflowcore.MessageListener;
import com.jflow.jflowcore.MessageType;
import com.jflow.jflowcore.utils.FileDownloader;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hventurar@edenorte.com.do
 */
public class TicketDownloader implements MessageListener {

    public static final String FILE_TICKET = "ticket.jasper";
    private static final Logger LOG = LogManager.getLogger(TicketDownloader.class);

    @Override
    public void onMessage(Message message) {

        LOG.info("MENSAJE: {}", message.toString());

        if (!MessageType.DOWNLOAD.equals(message.getTipoMensaje())) {
            return;
        }

        JsonElement fileJasper = message.getMensaje()
                .get("jasperFileId");

        LOG.info("JasperFileId = " + fileJasper);

        if (fileJasper != null) {

            String fileId = fileJasper.getAsString();
            String servidor = JflowCoreWS.getConfig().getAsString("serverUrl");
            String url = servidor.concat("/archivo/").concat(fileId);

            try {
                FileDownloader.download(url, new File(FILE_TICKET));
            } catch (Exception ex) {
                LOG.error("Error downloading jasper file", ex);
            }
        }
    }

}

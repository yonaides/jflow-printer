/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import com.jflow.jflowcore.JflowCoreWS;
import com.jflow.jflowcore.JflowWSType;
import com.jflow.jflowcore.Message;
import com.jflow.jflowcore.MessageListener;
import com.jflow.jflowcore.MessageType;
import com.jflow.jflowcore.RestStatusCheker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hectorvent@gmail.com
 */
public class App implements MessageListener {

    private static final Logger LOG = LogManager.getLogger(App.class);
    private JasperReportUtils rp = null;

    public void start() {

        // Iniciamos despues de haber iniciado toda la configuración
        rp = new JasperReportUtils();
        rp.setJasperFile("/jaspers/TurnoRepresentante.jasper");

        JflowCoreWS.createWsCore(JflowWSType.PRINTER)
                .addQueueMessageListener(this)
                .start();

        RestStatusCheker.getRestatusCheker()
                .addMessageListener(message -> {
                    System.out.println(message);
                });

    }

    @Override
    public void onMessage(Message message) {

        LOG.info("MENSAJE: {}", message.toString());

        if (MessageType.PRINT.equals(message.getTipoMensaje())) {
            try {
                message.getMensaje()
                        .entrySet()
                        .stream()
                        .forEach((entry) -> {
                            rp.put(entry.getKey(), entry.getValue());
                        });

                LOG.info("[IMPRESORA] {}", "");
                rp.printOnPrinter("");

            } catch (JasperReportExcepcion ex) {
                LOG.error("Error Imprimiendo ", ex);
            }
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

}

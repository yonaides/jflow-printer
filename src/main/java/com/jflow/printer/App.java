/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import com.jflow.jflowcore.JflowCoreWS;
import com.jflow.jflowcore.JflowWSType;
import com.jflow.jflowcore.RestStatusCheker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hectorvent@gmail.com
 */
public class App {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public void start() {

        LOG.info("Starting 'JflowCoreWS [Mode => Printer]' ");

        JflowCoreWS.createWsCore(JflowWSType.PRINTER)
                //Imprimir en orden de llegada
                .addQueueMessageListener(new TicketPrint())
                // Desgargar archivo en el Hilo indempendiente
                .addMessageListener(new TicketDownloader())
                .start();

        RestStatusCheker.getRestatusCheker()
                .addMessageListener(message -> {
                    LOG.info("Message : {}");
                });

        LOG.info("Started 'JflowCoreWS [Modo => Printer]' ");

    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

}

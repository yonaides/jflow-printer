/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

/**
 *
 * @author hventura@citrus.com.do
 */
public class JasperReportExcepcion extends Exception {

    public JasperReportExcepcion() {
    }

    public JasperReportExcepcion(String message) {
        super(message);
    }

    public JasperReportExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public JasperReportExcepcion(Throwable cause) {
        super(cause);
    }

}

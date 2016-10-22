/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jflow.printer;

import java.io.Serializable;

/**
 *
 * @author hectorvent@gmail.com
 */
public class TurnoPrinter implements Serializable {

    private String oficina;
    private String contrato;
    private String nombreCliente;
    private String servicio;
    private String turno;
    private String fecha;

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "TurnoPrinter{" + "oficina=" + oficina + ", contrato=" + contrato + ", nombreCliente=" + nombreCliente + ", servicio=" + servicio + ", turno=" + turno + ", fecha=" + fecha + '}';
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filatiweb;

/**
 *
 * @author P0170848
 */
public class Atendimento {
    private String chamado;
    private String tecnico;
    private String dataHora;

    public Atendimento(String chamado, String tecnico, String dataHora) {
        this.chamado = chamado;
        this.tecnico = tecnico;
        this.dataHora = dataHora;
    }

    public String getChamado() {
        return chamado;
    }

    public String getTecnico() {
        return tecnico;
    }

    public String getDataHora() {
        return dataHora;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.filatiweb;


import java.time.LocalTime;
/**
 *
 * @author P0170848
 */

    
public class Tecnico {

    private int id;
    private String nome;
    private LocalTime inicioAlmoco;
    private LocalTime fimAlmoco;
    private boolean ativo;
    private int ordem;

    public Tecnico(
            int id,
            String nome,
            LocalTime inicioAlmoco,
            LocalTime fimAlmoco,
            boolean ativo,
            int ordem) {

        this.id = id;
        this.nome = nome;
        this.inicioAlmoco = inicioAlmoco;
        this.fimAlmoco = fimAlmoco;
        this.ativo = ativo;
        this.ordem = ordem;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalTime getInicioAlmoco() {
        return inicioAlmoco;
    }

    public LocalTime getFimAlmoco() {
        return fimAlmoco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public int getOrdem() {
        return ordem;
    }
}

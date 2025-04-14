package com.example.TerceiraAvaliacao.model;

import java.time.LocalTime;

public class Horario {
    // horário da exibição
    private LocalTime horario;

    // filme que será exibido nesse horário
    private Filme filme;

    // sala onde o filme será exibido
    private Sala sala;

    // construtor que define o horário, o filme e a sala da exibição
    public Horario(LocalTime horario, Filme filme, Sala sala) {
        this.horario = horario;
        this.filme = filme;
        this.sala = sala;
    }

    // retorna o horário da exibição
    public LocalTime getHorario() {
        return horario;
    }

    // retorna o filme da exibição
    public Filme getFilme() {
        return filme;
    }

    // retorna a sala da exibição
    public Sala getSala() {
        return sala;
    }
}

package com.example.TerceiraAvaliacao.model;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    // nome do cinema
    private String nome;

    // endereço do cinema
    private String endereco;

    // lista que armazena todas as salas cadastradas no cinema
    private List<Sala> salas = new ArrayList<>();

    // lista que armazena todos os horários de exibição no cinema
    private List<Horario> horarios = new ArrayList<>();

    // construtor que recebe nome e endereço do cinema
    public Cinema(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    // retorna o nome do cinema
    public String getNome() {
        return nome;
    }

    // retorna o endereço do cinema
    public String getEndereco() {
        return endereco;
    }

    // retorna a lista de salas do cinema
    public List<Sala> getSalas() {
        return salas;
    }

    // retorna a lista de horários de exibição do cinema
    public List<Horario> getHorarios() {
        return horarios;
    }

    // adiciona uma nova sala à lista de salas
    public void adicionarSala(Sala sala) {
        salas.add(sala);
    }

    // remove uma sala da lista com base no nome, ignorando letras maiúsculas/minúsculas
    public void removerSala(String nomeSala) {
        salas.removeIf(s -> s.getNome().equalsIgnoreCase(nomeSala));
    }

    // adiciona um novo horário à lista de horários
    public void adicionarHorario(Horario horario) {
        horarios.add(horario);
    }
}

package com.example.TerceiraAvaliacao.model;

public class SalaPequena extends Sala {
    // construtor define a capacidade como 100 para salas pequenas
    public SalaPequena(String nome) {
        super(nome, 100); // capacidade fixa
    }

    // retorna o tipo da sala como "Sala Pequena"
    @Override
    public String tipoSala() {
        return "Sala Pequena";
    }
}

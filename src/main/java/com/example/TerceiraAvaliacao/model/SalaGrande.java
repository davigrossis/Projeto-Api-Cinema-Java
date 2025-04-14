package com.example.TerceiraAvaliacao.model;

public class SalaGrande extends Sala {
    // construtor define a capacidade como 150 para salas grandes
    public SalaGrande(String nome) {
        super(nome, 150); // capacidade fixa
    }

    // retorna o tipo da sala como "Sala Grande"
    @Override
    public String tipoSala() {
        return "Sala Grande";
    }
}

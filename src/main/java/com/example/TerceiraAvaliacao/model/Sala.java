package com.example.TerceiraAvaliacao.model;

public abstract class Sala implements Exibir {
    // nome da sala
    protected String nome;

    // capacidade máxima de pessoas na sala
    protected int capacidade;

    // construtor que define o nome e a capacidade da sala
    public Sala(String nome, int capacidade) {
        this.nome = nome;
        this.capacidade = capacidade;
    }

    // retorna o nome da sala
    public String getNome() {
        return nome;
    }

    // retorna a capacidade da sala
    public int getCapacidade() {
        return capacidade;
    }

    // implementação da interface Exibir, imprime o nome e a capacidade da sala
    @Override
    public void exibirInformacoes() {
        System.out.println("Sala: " + nome + " ," + capacidade + " pessoas no máximo)");
    }

    // método abstrato que define o tipo da sala (será implementado nas subclasses)
    public abstract String tipoSala();
}

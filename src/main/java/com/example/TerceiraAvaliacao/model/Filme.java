package com.example.TerceiraAvaliacao.model;

public class Filme implements Exibir {
    // título do filme
    private String titulo;

    // duração do filme em minutos
    private int duracaoMinutos;

    // construtor que define o título e a duração do filme
    public Filme(String titulo, int duracaoMinutos) {
        this.titulo = titulo;
        this.duracaoMinutos = duracaoMinutos;
    }

    // retorna o título do filme
    public String getTitulo() {
        return titulo;
    }

    // retorna a duração do filme em minutos
    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    // implementação da interface Exibir, mostra o título do filme no console
    @Override
    public void exibirInformacoes() {
        System.out.println("Filme: " + titulo);
    }

    // retorna uma representação em texto do filme com título e duração formatados
    @Override
    public String toString() {
        return titulo + " (" + duracaoMinutos + " min)";
    }
}

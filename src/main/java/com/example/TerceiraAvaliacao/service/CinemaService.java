package com.example.TerceiraAvaliacao.service;

import com.example.TerceiraAvaliacao.model.*;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class CinemaService {

    // cria uma instância estática de Cinema que será usada em todo o sistema
    private static Cinema cinema = new Cinema("Projeto Cultural", "Universidade Federal de Juiz de Fora, ICH");

    // retorna o cinema atual
    public static Cinema getCinema() {
        return cinema;
    }

    // método genérico que adiciona qualquer tipo de sala (SalaGrande ou SalaPequena) ao cinema
    public static <T extends Sala> void adicionarSalaGenerica(T sala) {
        cinema.adicionarSala(sala);
    }

    // busca uma sala pelo nome (ignorando maiúsculas/minúsculas), ou retorna null se não encontrar
    public static Sala buscarSalaPorNome(String nomeSala) {
        return cinema.getSalas().stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nomeSala))
                .findFirst().orElse(null);
    }

    // remove um horário da lista de exibições com base no horário e nome da sala
    public static void removerHorario(LocalTime horario, String nomeSala) {
        cinema.getHorarios().removeIf(h ->
                h.getHorario().equals(horario) &&
                        h.getSala().getNome().equalsIgnoreCase(nomeSala)
        );
    }

    // retorna a lista de salas cadastradas no cinema
    public static List<Sala> listarSalas() {
        return cinema.getSalas();
    }

    // retorna a lista de horários agendados
    public static List<Horario> listarHorarios() {
        return cinema.getHorarios();
    }

    // agenda um novo horário após validar se o horário está dentro do permitido e se não há conflitos
    public static String agendarHorarioComVerificacao(LocalTime novoInicio, Filme filme, Sala sala) {
        Cinema cinema = getCinema(); // pega o cinema atual
        LocalTime novoFim = novoInicio.plusMinutes(filme.getDuracaoMinutos()); // calcula o horário de término

        // define os limites de horário permitidos
        LocalTime inicioPermitido = LocalTime.of(9, 0);
        LocalTime limiteUltimoInicio = LocalTime.of(23, 30);

        // verifica se o novo horário está fora do intervalo permitido
        if (novoInicio.isBefore(inicioPermitido) || novoInicio.isAfter(limiteUltimoInicio)) {
            return "FORA_DO_HORARIO";
        }

        // verifica se há conflitos com outros horários da mesma sala
        for (Horario h : cinema.getHorarios()) {
            if (h.getSala().getNome().equalsIgnoreCase(sala.getNome())) {
                LocalTime inicioExistente = h.getHorario();
                LocalTime fimExistente = inicioExistente.plusMinutes(h.getFilme().getDuracaoMinutos());

                // verifica se o novo horário sobrepõe o existente
                boolean conflita = novoInicio.isBefore(fimExistente) && novoFim.isAfter(inicioExistente);
                if (conflita) {
                    return "CONFLITO";
                }
            }
        }

        // se passou nas validações, adiciona o novo horário ao cinema
        Horario novoHorario = new Horario(novoInicio, filme, sala);
        cinema.adicionarHorario(novoHorario);
        return null; // retorno nulo indica sucesso
    }

    // busca um horário específico com base no horário e na sala
    public static Horario buscarHorario(LocalTime horario, Sala sala) {
        return cinema.getHorarios().stream()
                .filter(h -> h.getHorario().equals(horario) &&
                        h.getSala().getNome().equalsIgnoreCase(sala.getNome()))
                .findFirst().orElse(null);
    }

}

package com.example.TerceiraAvaliacao.controller;

import com.example.TerceiraAvaliacao.model.*;
import com.example.TerceiraAvaliacao.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Controller
public class CinemaController {

    // injeta automaticamente o serviço responsável pela lógica do cinema
    @Autowired
    private CinemaService cinemaService;

    // obtém a instância do cinema usada ao longo do controller
    Cinema cinema = cinemaService.getCinema();

    // rota principal da aplicação que mostra os detalhes do cinema e suas salas
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cinema", CinemaService.getCinema()); // adiciona o cinema ao modelo
        model.addAttribute("salas", CinemaService.listarSalas()); // adiciona a lista de salas ao modelo
        return "cinema-detalhes"; // retorna a view correspondente
    }

    // exibe o formulário para criação de uma nova sala
    @GetMapping("/sala/nova")
    public String formularioSala(Model model) {
        model.addAttribute("sala", new SalaPequena("")); // adiciona uma sala padrão do tipo pequena
        return "formulario-sala"; // retorna a view com o formulário
    }

    // processa o formulário de criação de sala e adiciona a nova sala ao cinema
    @PostMapping("/sala/salvar")
    public String salvarSala(@RequestParam String nome, @RequestParam String tipo) {
        Sala sala;
        if (tipo.equals("grande")) {
            sala = new SalaGrande(nome); // cria sala grande se o tipo for "grande"
        } else {
            sala = new SalaPequena(nome); // caso contrário, cria sala pequena
        }
        CinemaService.adicionarSalaGenerica(sala); // adiciona a sala ao cinema
        return "redirect:/"; // redireciona para a página principal
    }

    // remove uma sala específica e todos os seus horários associados
    @GetMapping("/sala/excluir/{nome}")
    public String excluirSala(@PathVariable String nome) {
        cinema.getHorarios().removeIf(h -> h.getSala().getNome().equalsIgnoreCase(nome)); // remove os horários da sala
        cinema.removerSala(nome); // remove a sala em si
        return "redirect:/"; // redireciona para a página principal
    }

    // exibe a lista de todos os horários cadastrados
    @GetMapping("/horarios")
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", CinemaService.listarHorarios()); // adiciona os horários ao modelo
        return "horarios"; // retorna a view com a lista de horários
    }

    // exibe o formulário para cadastrar um novo horário
    @GetMapping("/horarios/novo")
    public String mostrarFormularioHorario(Model model) {
        model.addAttribute("salas", CinemaService.listarSalas()); // adiciona salas disponíveis
        model.addAttribute("filme", new Filme("", 90)); // adiciona um filme vazio como exemplo
        model.addAttribute("horario", ""); // campo de horário vazio
        return "formulario-horario"; // retorna a view com o formulário
    }

    // processa o envio do formulário de novo horário e tenta agendar o filme
    @PostMapping("/horarios/novo")
    public String processarFormularioHorario(@RequestParam String titulo,
                                             @RequestParam int duracao,
                                             @RequestParam String nomeSala,
                                             @RequestParam String horario,
                                             Model model) {
        Filme filme = new Filme(titulo, duracao); // cria o filme com os dados informados
        Sala sala = CinemaService.buscarSalaPorNome(nomeSala); // busca a sala pelo nome
        LocalTime hora = LocalTime.parse(horario); // converte a string em hora

        String resultado = CinemaService.agendarHorarioComVerificacao(hora, filme, sala); // tenta agendar

        if (resultado != null) {
            String mensagemErro;
            switch (resultado) {
                case "FORA_DO_HORARIO":
                    mensagemErro = "Agendamento fora do horário comercial (entre 09:00 e 23:30).";
                    break;
                case "CONFLITO":
                    mensagemErro = "Um filme já estará em exibição nessa hora.";
                    break;
                default:
                    mensagemErro = "Erro ao agendar horário.";
            }
            model.addAttribute("erro", mensagemErro); // envia mensagem de erro
            return mostrarFormularioHorario(model); // retorna para o formulário
        }

        return "redirect:/horarios"; // redireciona para a lista de horários
    }

    // remove um horário específico com base no horário e nome da sala
    @PostMapping("/horarios/remover")
    public String removerHorario(@RequestParam String horario,
                                 @RequestParam String nomeSala) {
        LocalTime hora = LocalTime.parse(horario); // converte o horário
        CinemaService.removerHorario(hora, nomeSala); // remove o horário do sistema
        return "redirect:/horarios"; // redireciona para a lista
    }

    // exibe o formulário de edição para um horário existente
    @GetMapping("/horarios/editar")
    public String mostrarFormularioEdicao(@RequestParam String horario,
                                          @RequestParam String nomeSala,
                                          Model model) {
        LocalTime hora = LocalTime.parse(horario); // converte o horário
        Sala sala = CinemaService.buscarSalaPorNome(nomeSala); // busca a sala
        Horario h = CinemaService.buscarHorario(hora, sala); // busca o horário

        if (h == null) {
            return "redirect:/horarios"; // se não encontrar, volta para a lista
        }

        // adiciona dados ao modelo para preenchimento do formulário
        model.addAttribute("salas", CinemaService.listarSalas());
        model.addAttribute("filme", h.getFilme());
        model.addAttribute("horarioAtual", h.getHorario().toString());
        model.addAttribute("salaAtual", h.getSala().getNome());
        return "formulario-editar-horario"; // retorna o formulário de edição
    }

    // processa os dados enviados no formulário de edição de horário
    @PostMapping("/horarios/editar")
    public String processarEdicao(@RequestParam String titulo,
                                  @RequestParam int duracao,
                                  @RequestParam String nomeSala,
                                  @RequestParam String horarioNovo,
                                  @RequestParam String horarioAtual,
                                  @RequestParam String nomeSalaAtual,
                                  Model model) {
        Sala salaAntiga = CinemaService.buscarSalaPorNome(nomeSalaAtual); // busca a sala original
        LocalTime horaAntiga = LocalTime.parse(horarioAtual); // horário original

        Sala salaNova = CinemaService.buscarSalaPorNome(nomeSala); // nova sala
        LocalTime horaNova = LocalTime.parse(horarioNovo); // novo horário
        Filme filme = new Filme(titulo, duracao); // novo filme com os dados atualizados

        CinemaService.removerHorario(horaAntiga, nomeSalaAtual); // remove o antigo horário

        String resultado = CinemaService.agendarHorarioComVerificacao(horaNova, filme, salaNova); // tenta agendar novo

        if (resultado != null) {
            CinemaService.agendarHorarioComVerificacao(horaAntiga, filme, salaAntiga); // desfaz alteração em caso de erro

            String mensagemErro;
            switch (resultado) {
                case "FORA_DO_HORARIO":
                    mensagemErro = "Agendamento fora do horário comercial (entre 09:00 e 23:30).";
                    break;
                case "CONFLITO":
                    mensagemErro = "Um filme já estará em exibição nessa hora.";
                    break;
                default:
                    mensagemErro = "Erro ao agendar horário.";
            }

            model.addAttribute("erro", mensagemErro); // envia mensagem de erro
            return mostrarFormularioEdicao(horarioAtual, nomeSalaAtual, model); // volta ao formulário
        }

        return "redirect:/horarios"; // redireciona para a lista de horários
    }

}

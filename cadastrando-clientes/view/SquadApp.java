package view;

import model.DatabaseManager;
import model.SquadDAO;
import model.SquadMember;

import java.util.List;
import java.util.Scanner;


public class SquadApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SquadDAO squadDAO = new SquadDAO();

    public static void main(String[] args) {
        DatabaseManager.criarTabela();
        exibirMenu();
    }

    private static void exibirMenu() {
        while(true) {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar Integrante");
            System.out.println("2. Listar Integrantes");
            System.out.println("3. Atualizar Integrante");
            System.out.println("4. Deletar Integrante");
            System.out.println("5. Sair");
            System.out.print("Escolha a opção: ");

            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1" -> cadastrar();
                case "2" -> listar();
                case "3" -> atualizar();
                case "4" -> deletar();
                case "5" -> {
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Opção Inválida, tente novamente.");
            }
        }
    }

    private static void cadastrar() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Tempo de Empresa (anos): ");
        int tempo = Integer.parseInt(scanner.nextLine());
        System.out.print("Squad: ");
        String squad = scanner.nextLine();
        System.out.print("Função: ");
        String funcao = scanner.nextLine(); SquadMember member = new SquadMember(0, nome, tempo, squad, funcao);
        squadDAO.cadastrarIntegrante((member));
    }

    private static void listar() {
        List<SquadMember> integrantes = squadDAO.listarIntegrantes();
        System.out.println("\n Integrantes Cadastrados:");
        for (SquadMember member : integrantes) {
            System.out.println((member));
        }
    }

    private static void atualizar() {
        System.out.print("ID do Integrante: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Tempo de Empresa (anos): ");
        int tempo = Integer.parseInt(scanner.nextLine());
        System.out.print("Squad: ");
        String squad = scanner.nextLine();
        System.out.print("Função: ");
        String funcao = scanner.nextLine(); SquadMember member = new SquadMember(id, nome, tempo, squad, funcao);
        squadDAO.atualizarIntegrante((member));

        member.setNome(nome);
        member.setTempoEmpresa(tempo);
        member.setSquad(squad);
        member.setFuncao(funcao);

        squadDAO.atualizarIntegrante(member);

    }

    private static void deletar() {
        System.out.print("ID do Integrante: ");
        int id = Integer.parseInt(scanner.nextLine());
        squadDAO.deletarIntegrante(id);
    }
}
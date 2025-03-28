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
        String nome;
        do {
            System.out.print("Nome: ");
            nome = scanner.nextLine();
            if (!squadDAO.isValidString(nome)) {
                System.out.println("Nome inválido. Deve conter apenas letras.");
            }
        } while (!squadDAO.isValidString(nome));

        System.out.print("Tempo de empresa (anos): ");
        int tempo = Integer.parseInt(scanner.nextLine());

        String squad;
        do {
            System.out.print("Squad: ");
            squad = scanner.nextLine();
            if (!squadDAO.isValidString(squad)) {
                System.out.print("Squad inválido. Deve conter apenas letras.\n");
            }
        } while (!squadDAO.isValidString(squad));

        String funcao;
        do {
            System.out.print("Função: ");
            funcao = scanner.nextLine();
            if (!squadDAO.isValidString(funcao)) {
                System.out.print("Função inválida. Deve conter apenas letras.\n");
            }
        } while (!squadDAO.isValidString(funcao));

        SquadMember member = new SquadMember(0, nome, tempo, squad, funcao);
        squadDAO.cadastrarIntegrante(member);
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

        if (id <= 0 || !squadDAO.isValidId(id)) {
            System.out.println("ID inválido. Tente novamente.");
            return;
        }

        System.out.print("Nome (deixe vazio para não alterar): ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) nome = null;
        else if (!squadDAO.isValidString(nome)) {
            System.out.println("Nome inválido. Deve conter apenas letras.");
            return;
        }

        System.out.print("Tempo de Empresa (anos): ");
        String tempoInput = scanner.nextLine().trim();
        Integer tempo = null;
        if (!tempoInput.isEmpty()) {
            try {
                tempo = Integer.parseInt(tempoInput);
            } catch (NumberFormatException e) {
                System.out.println("Tempo de empresa inválido. Tente novamente.");
                return;
            }
        }

        System.out.print("Squad (deixe vazio para não alterar): ");
        String squad = scanner.nextLine().trim();
        if (squad.isEmpty()) squad = null;
        else if(!squadDAO.isValidString(squad)) {
            System.out.println("Squad inválido. Deve conter apenas letras.");
            return;
        }

        System.out.print("Função (deixe vazio para não alterar): ");
        String funcao = scanner.nextLine().trim();
        if (funcao.isEmpty()) funcao = null;
        else if(!squadDAO.isValidString(funcao)) {
            System.out.println("Função inválida. Deve conter apenas letras.");
            return;
        }

        if(nome == null && tempo == null && squad == null && funcao == null) {
            System.out.println("Nenhuma informação foi alterada.");
            return;
        }

        SquadMember member = new SquadMember(id, (nome != null) ? nome: null, (tempo != null) ? tempo: 0, (squad != null) ? squad: null, (funcao != null) ? funcao: null);
        squadDAO.atualizarIntegrante(member);
    }

    private static void deletar() {
        System.out.print("ID do Integrante: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (id <= 0 || !squadDAO.isValidId(id)) {
            System.out.println("ID inválido. Tente novamente.");
            return;
        }
        squadDAO.deletarIntegrante(id);
    }
}
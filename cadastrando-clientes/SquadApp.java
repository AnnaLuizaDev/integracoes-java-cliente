import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class SquadMember {
    private int id;
    private String nome;
    private int tempoEmpresa;
    private String squad;
    private String funcao;

    public SquadMember(int id, String nome, int tempoEmpresa, String squad, String funcao) {
        this.id = id;
        this.nome = nome;
        this.tempoEmpresa = tempoEmpresa;
        this.squad = squad;
        this.funcao = funcao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempoEmpresa() {
        return tempoEmpresa;
    }

    public void setTempoEmpresa(int tempoEmpresa) {
        this.tempoEmpresa = tempoEmpresa;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override public String toString() {
        return String.format("ID: %d | Nome: %s | Tempo: %d anos | Squad: %s | FUnção: %s", id, nome, tempoEmpresa, squad, funcao);
    }
}


class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:squad.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void criarTabela() throws RuntimeException {
        String sql = "CREATE TABLE IF NOT EXISTS squad ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "tempo_empresa INTEGER NOT NULL,"
                + "squad TEXT NOT NULL,"
                + "funcao TEXT NOT NULL);";
        try(Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


class SquadDAO {
    public void cadastrarIntegrante(SquadMember member) {
        String sql = "INSERT INTO squad (nome, tempo_empresa, squad, funcao) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getNome());
            pstmt.setInt(2, member.getTempoEmpresa());
            pstmt.setString(3, member.getSquad());
            pstmt.setString(4, member.getFuncao());
            pstmt.executeUpdate();
            System.out.println("Integrante cadastrado com sucesso!");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SquadMember> listarIntegrantes() {
        List<SquadMember> integrantes = new ArrayList<>();
        String sql = "SELECT * FROM squad";
        try(Connection conn = DatabaseManager.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                SquadMember member = new SquadMember(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("tempo_empresa"),
                        rs.getString("squad"),
                        rs.getString("funcao")
                );
                integrantes.add(member);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return integrantes;
    }

    public void atualizarIntegrante(SquadMember member) {
        String sql = "UPDATE squad SET nome = ?, tempo_empresa = ?, squad = ?, funcao = ? WHERE id = ?";
        try(Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getNome());
            pstmt.setInt(2, member.getTempoEmpresa());
            pstmt.setString(3, member.getSquad());
            pstmt.setString(4, member.getFuncao());
            pstmt.setInt(5, member.getId());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Integrante atualizado com sucesso" : "Nenhum usuário encontrado com o ID fornecido.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletarIntegrante(int id) { String sql = "DELETE FROM squad WHERE id = ?";
        try(Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Integrante deletado com sucesso!" : "Nenhum integrante encontrado com o ID fornecido.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SquadApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SquadDAO squadDAO = new SquadDAO();

    public static void main(String[] args) {
        DatabaseManager.criarTabela(); exibirMenu();
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
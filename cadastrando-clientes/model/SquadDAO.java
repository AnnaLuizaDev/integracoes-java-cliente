package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SquadDAO {
    private static final Pattern VALID_STRING = Pattern.compile("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$");

    public void cadastrarIntegrante(SquadMember member) {
        if (!isValidString(member.getNome()) || !isValidString(member.getSquad()) || !isValidString(member.getFuncao())) {
            throw new IllegalArgumentException("Nome, Squad e Função devem conter apenas letras.");
        }
        String sql = "INSERT INTO squad (nome, tempo_empresa, squad, funcao) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getNome());
            pstmt.setInt(2, member.getTempoEmpresa());
            pstmt.setString(3, member.getSquad());
            pstmt.setString(4, member.getFuncao());
            pstmt.executeUpdate();
            System.out.println("Integrante cadastrado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SquadMember> listarIntegrantes() {
        List<SquadMember> integrantes = new ArrayList<>();
        String sql = "SELECT * FROM squad";
        try (Connection conn = DatabaseManager.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SquadMember member = new SquadMember(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("tempo_empresa"),
                        rs.getString("squad"),
                        rs.getString("funcao")
                );
                integrantes.add(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return integrantes;
    }

    public void atualizarIntegrante(SquadMember member) {
        if (!isValidId(member.getId())) {
            throw new IllegalArgumentException("ID não encontrado...");
        }

        StringBuilder sql = new StringBuilder("UPDATE squad SET ");
        List<Object> params = new ArrayList<>();

        if (member.getNome() != null) {
            if (!isValidString(member.getNome())) {
                throw new IllegalArgumentException("Nome deve conter apenas letras.");
            }
            sql.append("nome = ?, ");
            params.add(member.getNome());
        }

        if (member.getTempoEmpresa() > 0) {
            sql.append("tempo_empresa = ?, ");
            params.add(member.getTempoEmpresa());
        }

        if (member.getSquad() != null) {
            if (!isValidString(member.getSquad())) {
                throw new IllegalArgumentException("Squad deve conter apenas letras.");
            }
            sql.append("squad = ?, ");
            params.add(member.getSquad());
        }

        if (member.getFuncao() != null) {
            if (!isValidString(member.getFuncao())) {
                throw new IllegalArgumentException("Função deve conter apenas letras.");
            }
            sql.append("funcao = ?, ");
            params.add(member.getFuncao());
        }

        if(params.isEmpty()) {
            System.out.println("Nenhuma informação foi alterada.");
            return;
        }

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        params.add(member.getId());

        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Integrante atualizado com sucesso" : "Nenhum usuário encontrado com o ID fornecido.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletarIntegrante(int id) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("ID não encontrado...");
        }

        String sql = "DELETE FROM squad WHERE id = ?";
        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Integrante deletado com sucesso!" : "Nenhum integrante encontrado com o ID fornecido.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidString(String input) {
        return input != null && VALID_STRING.matcher(input).matches();
    }

    public boolean isValidId(int id) {
        String sql = "SELECT COUNT(*) FROM squad WHERE id = ?";
        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
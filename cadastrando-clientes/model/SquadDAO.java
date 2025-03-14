package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SquadDAO {
    public void cadastrarIntegrante(SquadMember member) {
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
        String sql = "UPDATE squad SET nome = ?, tempo_empresa = ?, squad = ?, funcao = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    public void deletarIntegrante(int id) {
        String sql = "DELETE FROM squad WHERE id = ?";
        try (Connection conn = DatabaseManager.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Integrante deletado com sucesso!" : "Nenhum integrante encontrado com o ID fornecido.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

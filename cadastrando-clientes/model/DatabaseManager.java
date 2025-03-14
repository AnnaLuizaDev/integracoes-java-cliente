package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:squad.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS squad ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "tempo_empresa INTEGER NOT NULL,"
                + "squad TEXT NOT NULL,"
                + "funcao TEXT NOT NULL);";
    }
}

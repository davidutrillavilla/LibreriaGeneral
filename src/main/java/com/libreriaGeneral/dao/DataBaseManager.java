package com.libreriaGeneral.dao;

import java.sql.*;

public class DataBaseManager extends Object{

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";

    private static final String USER = "postgres";

    private static final String PASSWORD = "postgres";

    private Connection connection;

    public DataBaseManager() throws SQLException {

        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);

        System.out.println("Conexi�n exitosa a la base de datos");

    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {

        return connection.prepareStatement(sql);
    }

    public Connection getConnection() {

        return connection;
    }

    public void close() {

        try{

            if (connection != null && !connection.isClosed()) {

                connection.close();

                System.out.println("Conexi�n cerrada");
            }
        }catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement) throws  SQLException {

        return  preparedStatement.executeQuery();
    }
}

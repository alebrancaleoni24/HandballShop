package isa.handballshop.model.dao.implementation;

import isa.handballshop.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import isa.handballshop.model.dao.*;

public class MySQLJDBCImpl extends JDBCDAOFactory{
    
    private Connection connection;

    /*Stabilisce la connessione con il DB settando a false l'autocommit */

    @Override
    public void beginTransaction() {
        try {
            Class.forName(Configuration.MYSQL_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.MYSQL_URL);
            this.connection.setAutoCommit(false);
        }catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Metodo per fare la commit della transazione */

    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Metodo per fare la rollBack della transazione */

    @Override
    public void rollbackTransaction() {
        try {
            this.connection.rollback();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Metodo per chiudere la connessione con il DB */

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContieneDAO getContieneDAO() {
        return new ContieneDAOImpl(connection);
    }

    @Override
    public OrdineDAO getOrdineDAO() {
        return new OrdineDAOImpl(connection);
    }

    @Override
    public PagamentoDAO getPagamentoDAO() {
        return new PagamentoDAOImpl(connection);
    }

    @Override
    public ProdottoDAO getProdottoDAO() {
        return new ProdottoDAOImpl(connection);
    }
    
    @Override
    public TagliaDAO getTagliaDAO() {
        return new TagliaDAOImpl(connection);
    }

    @Override
    public UtenteDAO getUtenteDAO() {
        return new UtenteDAOImpl(connection);
    }
    
}
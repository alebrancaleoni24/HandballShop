package isa.handballshop.model.dao;

import isa.handballshop.model.dao.implementation.DB2JDBCImpl;
import isa.handballshop.model.dao.implementation.MySQLJDBCImpl;

public abstract class JDBCDAOFactory {

    // List of DAO types supported by the factory
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String DB2JDBCIMPL = "BD2JDBCImpl";

    /*Metodi per le transazioni*/
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();
    
    /*Metodi per ritornare i DAO*/
    public abstract ContieneDAO getContieneDAO();
    public abstract OrdineDAO getOrdineDAO();
    public abstract PagamentoDAO getPagamentoDAO();
    public abstract ProdottoDAO getProdottoDAO();
    public abstract TagliaDAO getTagliaDAO();
    public abstract UtenteDAO getUtenteDAO();
    
    public static JDBCDAOFactory getJDBCImpl(String whichFactory) {
        if (whichFactory.equals(MYSQLJDBCIMPL)) {
            return new MySQLJDBCImpl();
        }else{
            if(whichFactory.equals(DB2JDBCIMPL)){
                return new DB2JDBCImpl();
            }else{
                return null;
            }
        }
    }

}
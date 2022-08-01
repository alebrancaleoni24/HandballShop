package isa.handballshop.model.dao.implementation;

import static isa.handballshop.services.util.Conversion.convertJavaDateToSqlDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import isa.handballshop.model.dao.PagamentoDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Pagamento;
import isa.handballshop.model.valueObject.Utente;

public class PagamentoDAOImpl implements PagamentoDAO{
    
    private Connection connection;
    
    public PagamentoDAOImpl(Connection connection){
        this.connection=connection;
    }

    /* Crea una nuova tupla di pagamento nel DB */
    @Override
    public Pagamento creaPagamento(String stato, String carta, java.util.Date dataRichiestaPagamento, java.util.Date dataPagamento, float importo, 
                                    Utente utente, Ordine ordine) throws DuplicatedObjectException {
        
        PreparedStatement ps;
        Pagamento pagamento = new Pagamento();
        pagamento.setStato(stato);
        pagamento.setCarta(carta);
        pagamento.setDataRichiestaPagamento(dataRichiestaPagamento);
        pagamento.setDataPagamento(dataPagamento);
        pagamento.setImporto(importo);
        pagamento.setUtente(utente);
        pagamento.setOrdine(ordine);

        try{

            /* Preparo la query per vedere se esiste già una tupla uguale */
            String sql
                    = " SELECT codicePag "
                    + " FROM pagamento "
                    + " WHERE "
                    + " stato = ? AND"
                    + " carta = ? AND"
                    + " dataRichiestaPagamento = ? AND"
                    + " dataPagamento = ? AND"
                    + " importo = ? AND"
                    + " email = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, pagamento.getStato());
            ps.setString(i++, pagamento.getCarta());
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataRichiestaPagamento()));
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataPagamento()));
            ps.setFloat(i++, pagamento.getImporto());
            ps.setString(i++, pagamento.getUtente().getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che il pagamento è già stato effettuato, quindi sollevo l'eccezione DuplicatedObjectException */
            if(exist){
                throw new DuplicatedObjectException("PagamentoDAOImpl.creaPagamento: Tentativo di inserimento di un pagamento già esistente");
            }


            sql = " INSERT INTO pagamento "
                    + "   ( codicePag,"
                    + " dataRichiestaPagamento,"
                    + " dataPagamento,"
                    + " stato,"
                    + " carta,"
                    + " importo,"
                    + " email"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, getUltimoCodice()+1);
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataRichiestaPagamento()));
            ps.setDate(i++, convertJavaDateToSqlDate(pagamento.getDataPagamento()));
            ps.setString(i++, pagamento.getStato());
            ps.setString(i++, pagamento.getCarta());
            ps.setFloat(i++, pagamento.getImporto());
            ps.setString(i++, pagamento.getUtente().getEmail());

            ps.executeUpdate();
            
            pagamento.setCodicePagamento(getUltimoCodice());

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return pagamento;
    }
    
    /*  Recupera dal DB il codice dell'ultimo pagamento inserito nel DB */
    @Override
    public long getUltimoCodice(){
        PreparedStatement ps;
        long codicePag = 0;
        try{
            String sql = " SELECT MAX(codicePag) AS codicePag"
                    + " FROM pagamento ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            if(resultSet.next()){
                codicePag = resultSet.getLong("codicePag");
            }
            
            resultSet.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return codicePag;
    }
    
    @Override
    public float getImporto(long codicePagamento){
        PreparedStatement ps;
        float importo = 0;
        try{
            String sql = " SELECT importo "
                    + " FROM pagamento "
                    + " WHERE codicePag = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setLong(1, codicePagamento);
            
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                importo = resultSet.getFloat("importo");
            }
            
            resultSet.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return importo;
    }
    
}
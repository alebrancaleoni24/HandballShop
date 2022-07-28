package isa.handballshop.model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;

import isa.handballshop.model.dao.OrdineDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;

import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Pagamento;
import isa.handballshop.model.valueObject.Utente;

import static isa.handballshop.services.util.Conversion.convertJavaDateToSqlDate;

public class OrdineDAOImpl implements OrdineDAO{
    
    private Connection connection;
    
    public OrdineDAOImpl(Connection connection){
        this.connection=connection;
    }

    /* Crea una nuova tupla di ordine nel DB */
    @Override
    public Ordine creaOrdine(java.util.Date dataOrdine, String stato, java.util.Date dataConsegna, String nazione, String città, String via, String numeroCivico, int CAP, Pagamento pag, Utente utente, ArrayList<Contiene> contiene) throws DuplicatedObjectException {
        
        PreparedStatement ps;
        Ordine ordine = new Ordine();
        ordine.setDataOrdine(dataOrdine);
        ordine.setStato(stato);
        ordine.setDataConsegna(dataConsegna);
        ordine.setNazione(nazione);
        ordine.setCittà(città);
        ordine.setVia(via);
        ordine.setNumeroCivico(numeroCivico);
        ordine.setCAP(CAP);
        ordine.setPagamento(pag);
        ordine.setUtente(utente);
        ordine.setContiene(contiene);

        try{

            /* Preparo la query per vedere se esiste già un ordine uguale */
            String sql
                    = " SELECT codiceOrdine "
                    + " FROM ordine "
                    + " WHERE "
                    + " dataOrdine = ? AND"
                    + " stato = ? AND"
                    + " dataConsegna = ? AND"
                    + " nazione = ? AND"
                    + " città = ? AND"
                    + " via = ? AND"
                    + " numeroCivico = ? AND"
                    + " CAP = ? AND"
                    + " codicePag = ? AND"
                    + " email = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataOrdine()));
            ps.setString(i++, ordine.getStato());
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataConsegna()));
            ps.setString(i++, ordine.getNazione());
            ps.setString(i++, ordine.getCittà());
            ps.setString(i++, ordine.getVia());
            ps.setString(i++, ordine.getNumeroCivico());
            ps.setInt(i++, ordine.getCAP());
            ps.setLong(i++, pag.getCodicePagamento());
            ps.setString(i++, utente.getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che l'ordine esiste già, quindi sollevo l'eccezione DuplicatedObjectException */
            if(exist){
                throw new DuplicatedObjectException("OrdineDAOImpl.creaOrdine: Tentativo di inserimento di un ordine già esistente");
            }

            /* Se sono arrivato qui l'ordine non esiste nel DB quindi creo la query per inserirlo */
            sql = " INSERT INTO ordine "
                    + "   ( dataOrdine,"
                    + " stato,"
                    + " dataConsegna,"
                    + " nazione,"
                    + " città,"
                    + " via,"
                    + " numeroCivico,"
                    + " CAP,"
                    + " codicePag,"
                    + " email"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataOrdine()));
            ps.setString(i++, ordine.getStato());
            ps.setDate(i++, convertJavaDateToSqlDate(ordine.getDataConsegna()));
            ps.setString(i++, ordine.getNazione());
            ps.setString(i++, ordine.getCittà());
            ps.setString(i++, ordine.getVia());
            ps.setString(i++, ordine.getNumeroCivico());
            ps.setInt(i++, ordine.getCAP());
            ps.setLong(i++, pag.getCodicePagamento());
            ps.setString(i++, utente.getEmail());

            ps.executeUpdate();
            
            ordine.setCodiceOrdine(getUltimoCodice());

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordine;
    }

    /* Modifica lo stato dell'ordine nel DB */

    @Override
    public void aggiornaStato(long codiceOrd, String stato){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE ordine "
                + " SET "
                + "   stato = ? "
                + " WHERE "
                + "   codiceOrdine = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, stato);
            ps.setLong(i++, codiceOrd);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /* Modifica lo stato dell'ordine nel DB */

    @Override
    public void aggiornaStato(long codiceOrd, String stato, Date dataOdierna){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE ordine "
                + " SET "
                + " stato = ?, "
                + " dataConsegna = ? "
                + " WHERE "
                + "   codiceOrdine = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, stato);
            ps.setDate(i++, convertJavaDateToSqlDate(dataOdierna));
            ps.setLong(i++, codiceOrd);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /* Recupera dal DB tutti gli ordini presenti */
    @Override
    public ArrayList<Ordine> findOrdini() {
        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        try{
            
            String sql
                = " SELECT * "
                + " FROM ordine "
                + " ORDER BY dataOrdine";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                ordine = read(resultSet);
                ordini.add(ordine);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordini;
    }

    /* Recupera tutti gli ordini dal DB dell'utente con l'email passata come parametro */

    @Override
    public ArrayList<Ordine> findByUtente(String email) {
        PreparedStatement ps;
        Ordine ordine;
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        try{
            
            String sql
                = " SELECT * "
                + " FROM ordine "
                + " WHERE email = ?"
                + " ORDER BY dataOrdine";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                ordine = read(resultSet);
                ordini.add(ordine);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return ordini;
    }
    
    /* Recupera dal DB il codice dell'ultimo ordine inserito */
    @Override
    public long getUltimoCodice(){
        PreparedStatement ps;
        long codiceOrd = 0;
        try{
            String sql = " SELECT MAX(codiceOrdine) AS codiceOrd "
                    + " FROM ordine ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            if(resultSet.next()){
                codiceOrd = resultSet.getLong("codiceOrd");
            }
            
            resultSet.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return codiceOrd;
    }
    
    /* Conta gli ordini effettuati dall'utente con l'email passata come parametro */

    @Override
    public int contaOrdini(String email){
        PreparedStatement ps;
        int numero = 0;
        try{
            String sql = " SELECT COUNT(*) AS num "
                    + " FROM ordine "
                    + " WHERE email = ? ";
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ResultSet resultSet = ps.executeQuery();
            
            if(resultSet.next()){
                numero = resultSet.getInt("num");
            }
            
            resultSet.close();
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return numero;
    }
    
    /* Leggo i campi del resultset e li carico in ordine */
    protected Ordine read(ResultSet resultSet){
        Ordine ordine = new Ordine();
        
        Pagamento pagamento = new Pagamento();
        ordine.setPagamento(pagamento);
        
        Utente utente = new Utente();
        ordine.setUtente(utente);
        
        /*Leggo il codice*/
        try {
            ordine.setCodiceOrdine(resultSet.getLong("codiceOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo dataOrdine*/
        try {
            ordine.setDataOrdine(resultSet.getDate("dataOrdine"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo lo stato*/
        try {
            ordine.setStato(resultSet.getString("stato"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo dataConsegna*/
        try {
            ordine.setDataConsegna(resultSet.getDate("dataConsegna"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo nazione*/
        try {
            ordine.setNazione(resultSet.getString("nazione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo città*/
        try {
            ordine.setCittà(resultSet.getString("città"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo via*/
        try {
            ordine.setVia(resultSet.getString("via"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il numero civico*/
        try {
            ordine.setNumeroCivico(resultSet.getString("numeroCivico"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il CAP*/
        try {
            ordine.setCAP(resultSet.getInt("CAP"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo codicePagamento*/
        try {
            ordine.getPagamento().setCodicePagamento(resultSet.getLong("codicePag"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo email*/
        try {
            ordine.getUtente().setEmail(resultSet.getString("email"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return ordine;
    }
    
}
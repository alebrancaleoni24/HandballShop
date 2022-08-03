package isa.handballshop.model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import isa.handballshop.model.dao.UtenteDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Utente;

public class UtenteDAOImpl implements UtenteDAO{
    
    private Connection connection;
    
    public UtenteDAOImpl(Connection connection){
        this.connection=connection;
    }

    /* Crea una nuova tupla di utente nel DB */
    @Override
    public Utente registrati(String email, String nome, String cognome, String password, String genere, String nazione, String città, 
                            String via, String numeroCivico, int CAP, boolean admin, boolean blocked) throws DuplicatedObjectException {

        /* Creo il preparestatement e un nuovo oggetto Utente caricandolo con i parametri ricevuti */
        PreparedStatement ps;
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setPassword(password);
        utente.setNazione(nazione);
        utente.setGenere(genere);
        utente.setNazione(nazione);
        utente.setCittà(città);
        utente.setVia(via);
        utente.setNumeroCivico(numeroCivico);
        utente.setCAP(CAP);
        utente.setAdmin(admin);
        utente.setBlocked(blocked);

        try{

            /* Controllo che l'email non sia già registrata */
            String sql
                    = " SELECT email "
                    + " FROM utente "
                    + " WHERE email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, utente.getEmail());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che l'utente esiste già, quindi sollevo l'eccezione DuplicatedObjectException */
            if(exist){
                throw new DuplicatedObjectException("OrdineDAOImpl.creaOrdine: Tentativo di inserimento di un utente già esistente");
            }

            /* Se sono arrivato qui l'account non esiste nel DB quindi creo la query per inserirlo */
            sql = " INSERT INTO utente "
                    + "   ( email,"
                    + " nome,"
                    + " cognome,"
                    + " password,"
                    + " genere,"
                    + " nazione,"
                    + " città,"
                    + " via,"
                    + " numeroCivico,"
                    + " CAP,"
                    + " admin,"
                    + " blocked"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, utente.getEmail());
            ps.setString(i++, utente.getNome());
            ps.setString(i++, utente.getCognome());
            ps.setString(i++, utente.getPassword());
            ps.setString(i++, utente.getGenere());
            ps.setString(i++, utente.getNazione());
            ps.setString(i++, utente.getCittà());
            ps.setString(i++, utente.getVia());
            ps.setString(i++, utente.getNumeroCivico());
            ps.setInt(i++, utente.getCAP());
            if(utente.isAdmin()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            if(utente.isBlocked()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            
            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utente;
    }

    /* Recupera dal DB tutti gli utenti registrati */
    @Override
    public ArrayList<Utente> findUtenti() {
        PreparedStatement ps;
        Utente utente;
        ArrayList<Utente> utenti = new ArrayList<Utente>();

        try{
            /* Prende tutti gli utenti nel DB in ordine alfabetico di cognome */
            String sql
                = " SELECT * "
                + " FROM utente "
                + " ORDER BY cognome, nome";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utenti;
    }
    
    /* Trova le iniziali dei cognomi di tutti gli utenti che possiedono un account */

    @Override
    public ArrayList<String> findInitialsUtenti(){
        PreparedStatement ps;
        String initial;
        ArrayList<String> initials = new ArrayList<String>();

        try {
            /* Prende soltanto la prima lettera a sinistra del cognome, la trasforma in maiuscolo e la chiama initial */
            String sql
                    = " SELECT DISTINCT UCase(Left(cognome,1)) AS initial "
                    + " FROM utente "
                    + " ORDER BY UCase(Left(cognome,1))";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                initial = resultSet.getString("initial");
                initials.add(initial);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return initials;
    }
    
    /* Recupera dal DB tutti gli utenti che hanno il cognome che inizia con la lettera passata come parametro */

    @Override
    public ArrayList<Utente> findUtentiByInitial(String initial){
        PreparedStatement ps;
        Utente utente;
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        
        try {

            String sql
                    = " SELECT * "
                    + " FROM utente "
                    + " WHERE UCASE(LEFT(cognome,1)) = ? "
                    + "ORDER BY cognome, nome";

            ps = connection.prepareStatement(sql);
            ps.setString(1, initial);
            
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                utente = read(resultSet);
                utenti.add(utente);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utenti;
    }

    /* Recupera dal DB l'utente che possiede l'email passata come parametro */

    @Override
    public Utente findByEmail(String email) {
        PreparedStatement ps;
        Utente utente = null;

        try {
            String sql
                    = " SELECT *"
                    + " FROM utente "
                    + " WHERE email = ? AND "
                    + "   blocked  = 'N' ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                utente = read(resultSet);
            }
            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utente;
    }
    
    /* Blocca l'utente che possiede l'email passata come parametro */

    @Override
    public void bloccaUtente(String email){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE utente "
                + " SET "
                + "   blocked = 'S' "
                + " WHERE "
                + "   email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /* Sblocca l'utente che ha l'email passata come parametro */

    @Override
    public void sbloccaUtente(String email){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE utente "
                + " SET "
                + "   blocked = 'N' "
                + " WHERE "
                + "   email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /*Elimina un utente dal DB*/
    @Override
    public void eliminaUtente(String email) {
        PreparedStatement ps;
        try{
            String sql 
                = " DELETE "
                + " FROM utente "
                + " WHERE email = ? ";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        
    }

    /* Recupera l'utente con l'email specificata e tutti gli ordini da lui effettuati */
    @Override
    public Utente schedaUtente(String email) {
        PreparedStatement ps;
        Utente utente = null;

        try{
            /* Prende tutti gli utenti nel DB in ordine alfabetico di cognome */
            String sql
                = " SELECT UTENTE.*, ORDINE.* "
                + " FROM UTENTE, ORDINE "
                + " WHERE UTENTE.email = ORDINE.email AND UTENTE.email = ?";

            ps = connection.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                utente = readJoin(resultSet);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return utente;
    }
    
    /* Leggo i campi del resultset e li carico in ordine */
    protected Utente read(ResultSet resultSet){
        Utente utente = new Utente();
        
        /*Leggo l'email*/
        try {
            utente.setEmail(resultSet.getString("email"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il nome*/
        try {
            utente.setNome(resultSet.getString("nome"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il cognome*/
        try {
            utente.setCognome(resultSet.getString("cognome"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la password*/
        try {
            utente.setPassword(resultSet.getString("password"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il genere*/
        try {
            utente.setGenere(resultSet.getString("genere"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la nazione*/
        try {
            utente.setNazione(resultSet.getString("nazione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la città*/
        try {
            utente.setCittà(resultSet.getString("città"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la via*/
        try {
            utente.setVia(resultSet.getString("via"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il numero civico*/
        try {
            utente.setNumeroCivico(resultSet.getString("numeroCivico"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il CAP*/
        try {
            utente.setCAP(resultSet.getInt("CAP"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo admin*/
        try {
            if(resultSet.getString("admin").equals("S")){
                utente.setAdmin(true);
            }else{
                utente.setAdmin(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo blocked*/
        try {
            if(resultSet.getString("blocked").equals("S")){
                utente.setBlocked(true);
            }else{
                utente.setBlocked(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return utente;
    }

    /* Leggo i campi del resultset e li carico in ordine */
    protected Utente readJoin(ResultSet resultSet) throws SQLException {
        Utente utente = new Utente();
        
        /*Leggo l'email*/
        try {
            utente.setEmail(resultSet.getString("email"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il nome*/
        try {
            utente.setNome(resultSet.getString("nome"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il cognome*/
        try {
            utente.setCognome(resultSet.getString("cognome"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la password*/
        try {
            utente.setPassword(resultSet.getString("password"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il genere*/
        try {
            utente.setGenere(resultSet.getString("genere"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la nazione*/
        try {
            utente.setNazione(resultSet.getString("nazione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la città*/
        try {
            utente.setCittà(resultSet.getString("città"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la via*/
        try {
            utente.setVia(resultSet.getString("via"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il numero civico*/
        try {
            utente.setNumeroCivico(resultSet.getString("numeroCivico"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il CAP*/
        try {
            utente.setCAP(resultSet.getInt("CAP"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo admin*/
        try {
            if(resultSet.getString("admin").equals("S")){
                utente.setAdmin(true);
            }else{
                utente.setAdmin(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo blocked*/
        try {
            if(resultSet.getString("blocked").equals("S")){
                utente.setBlocked(true);
            }else{
                utente.setBlocked(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }

        /* LEGGO GLI ORDINI */
        ArrayList<Ordine> ordini = new ArrayList<>();
        OrdineDAOImpl ordineDAOImpl = new OrdineDAOImpl(connection);
        do{

            ordini.add(ordineDAOImpl.read(resultSet));
            
        }while(resultSet.next());

        utente.setOrdine(ordini);
        
        return utente;
    }
    
}
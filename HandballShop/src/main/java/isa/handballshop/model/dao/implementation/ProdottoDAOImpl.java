package isa.handballshop.model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Prodotto;

public class ProdottoDAOImpl implements ProdottoDAO{
    
    private Connection connection;
    
    public ProdottoDAOImpl(Connection connection){
        this.connection=connection;
    }

    /* Crea una nuova tupla di prodotto nel DB */

    @Override
    public Prodotto creaProdotto(String categoria, String marca, String modello, String genere, String img, String descrizione, float prezzo, 
                                boolean blocked, boolean push, Contiene[] contiene) throws DuplicatedObjectException {
        
        PreparedStatement ps;
        Prodotto prodotto = new Prodotto();
        prodotto.setCategoria(categoria);
        prodotto.setMarca(marca);
        prodotto.setModello(modello);
        prodotto.setGenere(genere);
        prodotto.setImage(img);
        prodotto.setDescrizione(descrizione);
        prodotto.setPrezzo(prezzo);
        prodotto.setBlocked(blocked);
        prodotto.setPush(push);
        prodotto.setContiene(contiene);

        try{

            /* Preparo la query per vedere se esiste già un prodotto uguale */
            String sql
                    = " SELECT codiceProd "
                    + " FROM prodotto "
                    + " WHERE "
                    + " categoria = ? AND"
                    + " marca = ? AND"
                    + " modello = ? AND"
                    + " genere = ? AND"
                    + " img = ? AND"
                    + " descrizione = ? AND"
                    + " prezzo = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, prodotto.getCategoria());
            ps.setString(i++, prodotto.getMarca());
            ps.setString(i++, prodotto.getModello());
            ps.setString(i++, prodotto.getGenere());
            ps.setString(i++, prodotto.getImage());
            ps.setString(i++, prodotto.getDescrizione());
            ps.setFloat(i++, prodotto.getPrezzo());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che il prodotto esiste già, quindi sollevo l'eccezione DuplicatedObjectException */

            if(exist){
                throw new DuplicatedObjectException("ProdottoDAOImpl.creaProdotto: Tentativo di inserimento di un prodotto già esistente");
            }

            /* Se sono arrivato qui il prodotto non esiste nel DB quindi creo la query per inserirlo */
            
            sql = " INSERT INTO prodotto "
                    + "   ( codiceProd,"
                    + " categoria,"
                    + " marca,"
                    + " prezzo,"
                    + " modello,"
                    + " genere,"
                    + " img,"
                    + " descrizione,"
                    + " blocked,"
                    + " push"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);
            int j = 1;
            ps.setLong(i++, getUltimoCodice()+1);
            ps.setString(j++, prodotto.getCategoria());
            ps.setString(j++, prodotto.getMarca());
            ps.setFloat(j++, prodotto.getPrezzo());
            ps.setString(j++, prodotto.getModello());
            ps.setString(j++, prodotto.getGenere());
            ps.setString(j++, prodotto.getImage());
            ps.setString(j++, prodotto.getDescrizione());
            if(prodotto.isBlocked()){
                ps.setString(j++, "S");
            }else{
                ps.setString(j++, "N");
            }
            if(prodotto.isPush()){
                ps.setString(j++, "S");
            }else{
                ps.setString(j++, "N");
            }

            ps.executeUpdate();
            
            prodotto.setCodiceProdotto(getUltimoCodice());

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotto;
    }

    /* Modifica i valori di un prodotto nel DB controllando che non venga duplicato */

    @Override
    public void aggiorna(Prodotto prodotto) throws DuplicatedObjectException {
        PreparedStatement ps;
        try {

            /* Preparo la query per vedere se esiste già un prodotto uguale */

            String sql
                    = " SELECT codiceProd "
                    + " FROM prodotto "
                    + " WHERE "
                    + " categoria = ? AND"
                    + " marca = ? AND"
                    + " modello = ? AND"
                    + " genere = ? AND"
                    + " img = ? AND"
                    + " descrizione = ? AND"
                    + " prezzo = ?";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, prodotto.getCategoria());
            ps.setString(i++, prodotto.getMarca());
            ps.setString(i++, prodotto.getModello());
            ps.setString(i++, prodotto.getGenere());
            ps.setString(i++, prodotto.getImage());
            ps.setString(i++, prodotto.getDescrizione());
            ps.setFloat(i++, prodotto.getPrezzo());
            
            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che il prodotto esiste già, quindi sollevo l'eccezione DuplicatedObjectException */

            if(exist) {
                throw new DuplicatedObjectException("BuonoDAOImpl.aggiorna: Tentativo di aggiornamento in un buono già esistente");
            }

            /* Se sono arrivato qui il prodotto non esiste nel DB quindi creo la query per aggiornarlo */

            sql 
                = " UPDATE prodotto "
                + " SET categoria = ?, "
                + " marca = ?, "
                + " prezzo = ?, "
                + " modello = ?, "
                + " genere = ?, "
                + " img = ?, "
                + " descrizione = ?, "
                + " blocked = ?, "
                + " push = ? "
                + " WHERE "
                + " codiceProd = ?";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setString(i++, prodotto.getCategoria());
            ps.setString(i++, prodotto.getMarca());
            ps.setFloat(i++, prodotto.getPrezzo());
            ps.setString(i++, prodotto.getModello());
            ps.setString(i++, prodotto.getGenere());
            ps.setString(i++, prodotto.getImage());
            ps.setString(i++, prodotto.getDescrizione());
            if(prodotto.isBlocked()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            if(prodotto.isPush()){
                ps.setString(i++, "S");
            }else{
                ps.setString(i++, "N");
            }
            ps.setLong(i++, prodotto.getCodiceProdotto());
            
            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    /* Setta come bloccato un prodotto nel DB */
    @Override
    public void blocca(long codiceProd) {
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE prodotto "
                + " SET blocked = 'S' "
                + " WHERE "
                + " codiceProd = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProd);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /* Setta come sbloccato un prodotto nel DB */
    @Override
    public void sblocca(long codiceProd) {
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE prodotto "
                + " SET blocked = 'N' "
                + " WHERE "
                + " codiceProd = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProd);
            ps.executeUpdate();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /* Recupera dal DB tutte le categorie presenti */
    @Override
    public ArrayList<String> trovaCategorie(){
        PreparedStatement ps;
        String categoria;
        ArrayList<String> categorie = new ArrayList<String>();
        
        try{
            /* Prendo tutte le categorie esistenti in ordine alfabetico una volta sola */
            String sql
                    = " SELECT DISTINCT categoria "
                    + " FROM prodotto "
                    + " ORDER BY categoria ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                categoria = resultSet.getString("categoria");
                categorie.add(categoria);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return categorie;
    }
    
    /* Recupera dal DB tutte le marche presenti */

    @Override
    public ArrayList<String> trovaMarche(){
        PreparedStatement ps;
        String marca;
        ArrayList<String> marche = new ArrayList<String>();
        
        try{
            /* Prendo tutte le marche esistenti in ordine alfabetico una volta sola */
            String sql
                    = " SELECT DISTINCT marca "
                    + " FROM prodotto "
                    + " ORDER BY marca ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                marca = resultSet.getString("marca");
                marche.add(marca);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return marche;
    }
    
    /* Recupera dal DB tutti i generi presenti */

    @Override
    public ArrayList<String> trovaGeneri(){
        PreparedStatement ps;
        String genere;
        ArrayList<String> generi = new ArrayList<String>();
        
        try{
            /* Prendo tutti i generi esistenti in ordine alfabetico una volta sola */
            String sql
                    = " SELECT DISTINCT genere "
                    + " FROM prodotto "
                    + " ORDER BY genere ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                genere = resultSet.getString("genere");
                generi.add(genere);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException sqle){
            throw new RuntimeException(sqle);
        }
        
        return generi;
    }

    
    /* Recupera dal DB il prodotto con il codiceProd specificato */

    @Override
    public Prodotto findByKey(Long codiceProd){
        PreparedStatement ps;
        Prodotto prodotto = null;

        try{
            
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE codiceProd = ?";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProd);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                prodotto = read(resultSet);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotto;
    }
    
    /* Recupera dal DB tutti i prodotti presenti */
    @Override
    public ArrayList<Prodotto> trovaProdotti(){
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{

            String sql
                = " SELECT * "
                + " FROM prodotto ";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }

    /* Recupera dal DB tutti i prodotti che soddisfano la stringa di ricerca search */
    @Override
    public ArrayList<Prodotto> findByString(String search) {
        PreparedStatement ps;
        Prodotto prodotto;
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        try{
            /* Prende tutti i prodotti in base alla stringa specificata */
            String sql
                = " SELECT * "
                + " FROM prodotto "
                + " WHERE categoria LIKE '%" +search+ "%' OR"
                + " marca LIKE '%" +search+ "%' OR"
                + " modello LIKE '%" +search+ "%' OR"
                + " descrizione LIKE '%" +search+ "%'"
                + " AND blocked = 'N' ";

            ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                prodotto = read(resultSet);
                prodotti.add(prodotto);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return prodotti;
    }
    
    /* Recupera dal DB il codice dell'ultimo prodotto inserito */
    @Override
    public long getUltimoCodice(){
        PreparedStatement ps;
        long codiceProdotto = 0;
        try{
            String sql = " SELECT MAX(codiceProd) AS codice "
                    + " FROM prodotto ";
            
            ps = connection.prepareStatement(sql);
            
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                codiceProdotto = resultSet.getLong("codice");
            }
            ps.close();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return codiceProdotto;
    }
    
    /* Leggo i campi del resultset e li carico in prodotto */
    protected Prodotto read(ResultSet resultSet){
        Prodotto prodotto = new Prodotto();
        
        /*Leggo il codice*/
        try {
            prodotto.setCodiceProdotto(resultSet.getLong("codiceProd"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo categoria*/
        try {
            prodotto.setCategoria(resultSet.getString("categoria"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo marca*/
        try {
            prodotto.setMarca(resultSet.getString("marca"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo prezzo*/
        try {
            prodotto.setPrezzo(resultSet.getFloat("prezzo"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo modello*/
        try {
            prodotto.setModello(resultSet.getString("modello"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo genere*/
        try {
            prodotto.setGenere(resultSet.getString("genere"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo l'immagine*/
        try {
            prodotto.setImage(resultSet.getString("img"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la descrizione*/
        try {
            prodotto.setDescrizione(resultSet.getString("descrizione"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo blocked*/
        try {
            if(resultSet.getString("blocked").equals("S")){
                prodotto.setBlocked(true);
            }else{
                prodotto.setBlocked(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo push*/
        try {
            if(resultSet.getString("push").equals("S")){
                prodotto.setPush(true);
            }else{
                prodotto.setPush(false);
            }
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return prodotto;
    }
    
}
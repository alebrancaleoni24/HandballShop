package isa.handballshop.model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import isa.handballshop.model.dao.ContieneDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Prodotto;
import isa.handballshop.model.valueObject.Taglia;

public class ContieneDAOImpl implements ContieneDAO{
    
    private Connection connection;
    
    public ContieneDAOImpl(Connection connection){
        this.connection=connection;
    }

    /* Crea una nuova tupla nella tabella contiene del DB */
    @Override
    public Contiene creaContiene(long codiceOrd, long codiceProd, long quantità, String t) throws DuplicatedObjectException {
        
        PreparedStatement ps;
        Contiene contiene = new Contiene();
        
        Ordine ordine = new Ordine();
        ordine.setCodiceOrdine(codiceOrd);
        contiene.setOrdine(ordine);
        
        Prodotto prodotto = new Prodotto();
        prodotto.setCodiceProdotto(codiceProd);
        contiene.setProdotto(prodotto);
        contiene.setQuantità(quantità);
        
        Taglia taglia = new Taglia();
        taglia.setTaglia(t);
        contiene.setTaglia(taglia);

        try{
            
            /* Preparo la query per vedere se esiste già una tupla uguale */
            String sql
                    = " SELECT codiceProd "
                    + " FROM contiene "
                    + " WHERE "
                    + " codiceProd = ? AND"
                    + " codiceOrd = ? AND"
                    + " quantità = ? AND"
                    + " taglia = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, prodotto.getCodiceProdotto());
            ps.setLong(i++, ordine.getCodiceOrdine());
            ps.setLong(i++, contiene.getQuantità());
            ps.setString(i++, contiene.getTaglia().getTaglia());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            /* Se exist è true vuol dire che la tupla di contiene esiste già, quindi sollevo l'eccezione DuplicatedObjectException e la gestisco */
            if(exist){
                throw new DuplicatedObjectException("ContieneDAOImpl.creaContiene: Tentativo di inserimento di un contiene già esistente");
            }

            /* Se sono arrivato qui la tupla di contiene non esiste nel DB quindi creo la query per inserirla */

            sql = " INSERT INTO contiene "
                + "   ( codiceProd,"
                + "     codiceOrd,"
                + "     quantità,"
                + "     taglia"
                + "   ) "
                + " VALUES (?,?,?,?)";

            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, contiene.getProdotto().getCodiceProdotto());
            ps.setLong(i++, contiene.getOrdine().getCodiceOrdine());
            ps.setLong(i++, contiene.getQuantità());
            ps.setString(i++, contiene.getTaglia().getTaglia());

            ps.executeUpdate();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return contiene;
    }
    
    
    @Override
    public ArrayList<Contiene> findContieneByOrdine(long codiceOrdine){
        PreparedStatement ps;
        Contiene contiene;
        ArrayList<Contiene> contieneArray = new ArrayList<Contiene>();

        try{
            
            String sql
                = " SELECT * "
                + " FROM contiene "
                + " WHERE codiceOrd = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceOrdine);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()) {
                contiene = read(resultSet);
                contieneArray.add(contiene);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return contieneArray;
    }
    
    /* Leggo i campi del resultset e li carico in ordine */
    protected Contiene read(ResultSet resultSet){
        Contiene contiene = new Contiene();
        
        Ordine ordine = new Ordine();
        contiene.setOrdine(ordine);
        
        Prodotto prodotto = new Prodotto();
        contiene.setProdotto(prodotto);
        
        Taglia taglia = new Taglia();
        contiene.setTaglia(taglia);
        
        /*Leggo il codiceProdotto*/
        try {
            contiene.getProdotto().setCodiceProdotto(resultSet.getLong("codiceProd"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo il codiceOrdine*/
        try {
            contiene.getOrdine().setCodiceOrdine(resultSet.getLong("codiceOrd"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la quantità*/
        try {
            contiene.setQuantità(resultSet.getLong("quantità"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        /*Leggo la taglia*/
        try {
            contiene.getTaglia().setTaglia(resultSet.getString("taglia"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return contiene;
    }
}
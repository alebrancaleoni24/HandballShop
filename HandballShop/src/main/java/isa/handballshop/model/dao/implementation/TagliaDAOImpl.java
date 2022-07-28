package isa.handballshop.model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import isa.handballshop.model.dao.TagliaDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Taglia;

public class TagliaDAOImpl implements TagliaDAO{
    
    private Connection connection;
    
    public TagliaDAOImpl(Connection connection){
        this.connection=connection;
    }
    
    /* Crea una nuova tupl di taglia nel DB con i parametri passatogli */
    @Override
    public void creaTaglia(long codiceProd, long quantità, String taglia) throws DuplicatedObjectException {
        PreparedStatement ps;
        try{
            String sql = " SELECT quantità "
                    + " FROM taglia "
                    + " WHERE codiceProd = ? AND "
                    + " taglia = ? ";
            
            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, codiceProd);
            ps.setString(i++, taglia);
            
            ResultSet resultSet = ps.executeQuery();
            
            boolean exist;
            exist = resultSet.next();
            resultSet.close();
            
            if(exist){
                throw new DuplicatedObjectException("TagliaDAOImpl.creaTaglia: Tentativo di inserimento di una taglia già esistente");
            }
            
            sql = " INSERT INTO taglia "
                    + " (codiceProd,"
                    + " taglia,"
                    + " quantità)"
                    + " VALUES (?,?,?)";
            
            ps = connection.prepareStatement(sql);
            i = 1;
            ps.setLong(i++, codiceProd);
            ps.setString(i++, taglia);
            ps.setLong(i++, quantità);
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    

    /* Recupera la giacenza del prodotto con il codiceProd e la taglia t specificata */
    @Override
    public Taglia getTagliaByKey(long codiceProd, String t){
        PreparedStatement ps;
        Taglia taglia = null;

        try{
            /*
            *Prende la disponibilità di magazzino in base al codiceProd e alla taglia
            */
            String sql
                = " SELECT * "
                + " FROM taglia "
                + " WHERE codiceProd = ? AND "
                + " taglia = ? ";

            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProd);
            ps.setString(2, t);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()) {
                taglia = read(resultSet);
            }

            resultSet.close();
            ps.close();

        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return taglia;
    }
    
    /* Modifica la tupla di taglia passatogli aggiornando la disponibilità */
    @Override
    public void aggiorna(Taglia t){
        PreparedStatement ps;
        try{
            String sql 
                = " UPDATE taglia "
                + " SET "
                + " quantità = ? "
                + " WHERE "
                + " codiceProd = ? AND "
                + " taglia = ? ";

            ps = connection.prepareStatement(sql);
            int i = 1;
            ps.setLong(i++, t.getQuantità());
            ps.setLong(i++, t.getCodiceProd());
            ps.setString(i++, t.getTaglia());
            
            ps.executeUpdate();
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    
    /* Recupera le informazioni di taglia e relativa giacenza del prodotto con il codiceProdotto passatogli */
    
    @Override
    public ArrayList<Taglia> getTaglie(long codiceProdotto, String categoria){
        PreparedStatement ps;
        Taglia taglia;
        ArrayList<Taglia> taglie = new ArrayList<Taglia>();
        
        try{
            String sql = " SELECT * "
                    + " FROM taglia "
                    + " WHERE codiceProd = ?";
            
            ps = connection.prepareStatement(sql);
            ps.setLong(1, codiceProdotto);
            
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()){
                taglia = read(resultSet);
                taglie.add(taglia);
            }
            
            if(!categoria.equals("Scarpe")){
                taglie=ordina(taglie);
            }
            
            resultSet.close();
            ps.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        
        return taglie;
    }
    
    protected Taglia read (ResultSet rs){
        Taglia taglia = new Taglia();
        
        /*Leggo il codice*/
        try {
            taglia.setCodiceProd(rs.getLong("codiceProd"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        try {
            taglia.setTaglia(rs.getString("taglia"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        try {
            taglia.setQuantità(rs.getLong("quantità"));
        }catch(SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        
        return taglia;
    }
    
    protected ArrayList<Taglia> ordina(ArrayList<Taglia> taglie){
        ArrayList<Taglia> tagliaBis = new ArrayList<>();
        boolean found = false;
        int i;
        String[] t = {"XS","S","M","L","XL","XXL","XXXL"};
        
        for(int j=0 ; j<7 ; j++){
            i=0;
            found = false;
            do{
                if(taglie.get(i).getTaglia().equals(t[j])){
                    found = true;
                    tagliaBis.add(taglie.get(i));
                }
                i++;
            }while(!found && i<7);
        }
        
        return tagliaBis;
    }
    
}
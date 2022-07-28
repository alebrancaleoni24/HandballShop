package isa.handballshop.model.session.dao;

import java.util.ArrayList;
import isa.handballshop.model.session.valueObject.Carrello;

public interface CarrelloDAO {
    
    public ArrayList<Carrello> crea(long codiceProd, long quantità, String taglia);
    
    public void aggiungi(long codiceProd, long quantità, String taglia);
    
    public ArrayList<Carrello> rimuovi(long codiceProd, String taglia);
    
    public void elimina();
    
    public ArrayList<Carrello> trova();
    
    public ArrayList<Carrello> modificaQuantità(long codiceProdotto, long quantità, String taglia);
    
}
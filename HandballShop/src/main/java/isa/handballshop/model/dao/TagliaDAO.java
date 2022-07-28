package isa.handballshop.model.dao;

import java.util.ArrayList;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Taglia;

public interface TagliaDAO {
    
    public void creaTaglia(long codiceProd, long quantità, String taglia) throws DuplicatedObjectException;
    
    /*Recupera l'intera tupla della tabella Taglia*/
    public Taglia getTagliaByKey(long codiceProd, String t);
    
    /*Modifica la tupla di taglia passatogli aggiornando la disponibilità di magazzino*/
    public void aggiorna(Taglia t);
    
    /*Recupera tutte le taglie del prodotto con il codice passatogli*/
    public ArrayList<Taglia> getTaglie(long codiceProdotto, String categoria);
    
}
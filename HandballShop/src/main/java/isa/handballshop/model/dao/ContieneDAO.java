package isa.handballshop.model.dao;

import java.util.ArrayList;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Contiene;

public interface ContieneDAO {
    
    public Contiene creaContiene(long codiceOrd, long codiceProd, long quantità, String taglia) throws DuplicatedObjectException;
    
    public ArrayList<Contiene> findContieneByOrdine(long codiceOrdine);
    
}
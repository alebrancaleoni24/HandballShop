package isa.handballshop.model.session.dao;

import isa.handballshop.model.session.valueObject.UtenteLoggato;

public interface UtenteLoggatoDAO {
    
    /*Creazione cookie*/
    public UtenteLoggato crea(String email, String nome, String cognome, boolean admin);
    
    /*Aggiornamento cookie*/
    public void aggiorna(UtenteLoggato ul);
    
    /*Cancellazione cookie*/
    public void elimina();
    
    /*Lettura cookie*/
    public UtenteLoggato trova();
    
}
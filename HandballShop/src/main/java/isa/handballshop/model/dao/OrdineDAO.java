package isa.handballshop.model.dao;

import java.util.ArrayList;
import java.util.Date;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Ordine;
import isa.handballshop.model.valueObject.Pagamento;
import isa.handballshop.model.valueObject.Utente;

public interface OrdineDAO {
    
    public Ordine creaOrdine(java.util.Date dataOrdine, String stato, java.util.Date dataConsegna, String nazione, String città, String via, 
                            String numeroCivico, int CAP, Pagamento pag, Utente utente, ArrayList<Contiene> contiene) throws DuplicatedObjectException;
    
    /*Aggiorna lo stato dell'ordine specificato nel caso passi da 'in preparazione' a 'in viaggio'*/
    public void aggiornaStato(long codiceOrd, String stato);
    
    /*Aggiorna lo stato dell'ordine specificato nel caso passi a 'consegnato' poichè in questo caso viene aggiornata anche la data di consegna*/
    public void aggiornaStato(long codiceOrd, String stato, Date dataOdierna);
    
    /*Recupera tutti gli ordini effettuati*/
    public ArrayList<Ordine> findOrdini();
    
    /*Recupera tutti gli ordini effetttuati da uno specifico utente*/
    public ArrayList<Ordine> findByUtente(String email);
    
    public long getUltimoCodice();
    
    /*Conta quanti ordini ha effettuato uno specifico cliente*/
    public int contaOrdini(String email);

}
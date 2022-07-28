package isa.handballshop.model.dao;

import java.util.ArrayList;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Prodotto;

public interface ProdottoDAO {
    
    public Prodotto creaProdotto(String categoria, String marca, String modello, String genere, String img, 
                                String descrizione,float prezzo, boolean blocked, boolean push, Contiene[] contiene) 
                                throws DuplicatedObjectException;
    
    /*Aggiorna una tupla di prodotto*/
    public void aggiorna(Prodotto prodotto) throws DuplicatedObjectException;
    
    /*Blocca un prodotto*/
    public void blocca(long codiceProd);
    
    /*Sblocca un prodotto*/
    public void sblocca(long codiceProd);
    
    /*Trova tutte le categorie di prodotti presenti nel db*/
    public ArrayList<String> trovaCategorie();
    
    /*Trova tutte le marche dei prodotti presenti del db*/
    public ArrayList<String> trovaMarche();
    
    /*Trova tutti i generi dei prodotti presenti nel db*/
    public ArrayList<String> trovaGeneri();
    
    /*Recupera un prodotto dal db in base alla sua chiave*/
    public Prodotto findByKey(Long codiceProd);
    
    /*Recupera dal DB tutti i prodotti presenti*/
    public ArrayList<Prodotto> trovaProdotti();

    /*Trova tutti i prodotti che in un certo modo sono compatibili con la stringa di ricerca passata*/
    public ArrayList<Prodotto> findByString(String search);
    
    public long getUltimoCodice();
    
}
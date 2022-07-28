package isa.handballshop.model.session.dao.implementation;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.model.session.dao.CarrelloDAO;
import isa.handballshop.model.session.valueObject.Carrello;

public class CarrelloDAOImpl implements CarrelloDAO{
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    public CarrelloDAOImpl(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /* Crea il cookie carrello */
    @Override
    public ArrayList<Carrello> crea(long codiceProd, long quantità, String taglia) {
        Carrello carrello = new Carrello();
        carrello.setCodiceProd(codiceProd);
        carrello.setQuantità(quantità);
        carrello.setTaglia(taglia);
        
        ArrayList<Carrello> listaCarrello = new ArrayList<Carrello>();
        listaCarrello.add(carrello);

        /* Creo un oggetto cookie dandogli il nome Carrello e settandogli i parametri come li ritorna il metodo encode */
        Cookie cookie = new Cookie("Carrello", encode(listaCarrello));
        /*Specifico che il cookie deve essere valido per tutto il sito*/
        cookie.setPath("/");
        /*Aggiungo un cookie alla response e ritorno il carrello*/
        response.addCookie(cookie);

        return listaCarrello;
    }

    /* Aggiunge un elemento al cookie carrello */
    @Override
    public void aggiungi(long codiceProd, long quantità, String taglia) {
        ArrayList<Carrello> listaCarrello = trova();
        boolean trovato = false;
        
        Carrello carrello = new Carrello();
        carrello.setCodiceProd(codiceProd);
        carrello.setQuantità(quantità);
        carrello.setTaglia(taglia);
        
        /* Controllo che il prodotto inserito non fosse già nel carrello, in tal caso modifico la quantità di quel prodotto */
        for(int i=0 ; i < listaCarrello.size() && !trovato ; i++){
            if(listaCarrello.get(i).getCodiceProd() == carrello.getCodiceProd() && listaCarrello.get(i).getTaglia().equals(carrello.getTaglia())){
                trovato = true;
                carrello.setQuantità(carrello.getQuantita()+listaCarrello.get(i).getQuantita());
                
                /* Rimuovo dalla lista l'elemento in modo da poterci aggiungere l'elemento modificato */
                listaCarrello.removeIf(n -> (n.getCodiceProd() == codiceProd && n.getTaglia().equals(taglia)));
                elimina();
            }
        }
        
        listaCarrello.add(carrello);
        
        Cookie cookie = new Cookie("Carrello", encode(listaCarrello));
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    
    /* Rimuove un elemento dal cookie carrello */
    @Override
    public ArrayList<Carrello> rimuovi(long codiceProd, String taglia) {
        ArrayList<Carrello> listaCarrello = trova();
        Cookie cookie = null;
        
        /* Rimuovo dalla lista l'oggetto con il codice e la taglia passata per parametro */
        listaCarrello.removeIf(n -> (n.getCodiceProd() == codiceProd && n.getTaglia().equals(taglia)));
        elimina();
        
        if(listaCarrello.size() > 0){
            cookie = new Cookie("Carrello", encode(listaCarrello));
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        
        return listaCarrello;
    }

    /* Elimina l'intero cookie carrello */
    @Override
    public void elimina() {
        Cookie cookie = new Cookie("Carrello", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /* Recupera il cookie carrello */
    @Override
    public ArrayList<Carrello> trova() {
        /*Carico l'array dei cookie e l'oggetto Carrello*/
        Cookie[] cookies = request.getCookies();
        ArrayList<Carrello> carrello = new ArrayList<Carrello>();

        /* Se l'array dei cokie non è vuoto, lo scorro tutto oppure fino a quando carrello non è più vuoto */
        if (cookies != null) {
            for (int i = 0; i < cookies.length && carrello != null; i++) {
                /* Quando trovo il cookie carrello chiamo il metodo decode per fargli settare e restituirr l'oggetto carrello */
                if (cookies[i].getName().equals("Carrello")) {
                    carrello = decode(cookies[i].getValue());
                }
            }
        }

        return carrello;
    }
    
    /*  Modifica la quantità del prodotto specificato nel carrello */
    @Override
    public ArrayList<Carrello> modificaQuantità(long codiceProdotto, long quantità, String taglia){
        ArrayList<Carrello> listaCarrello = trova();
        
        for(int i=0; i<listaCarrello.size(); i++){
            if(listaCarrello.get(i).getCodiceProd() == codiceProdotto && listaCarrello.get(i).getTaglia().equals(taglia)){
                listaCarrello.get(i).setQuantità(quantità);
            }
        }
        
        Cookie cookie = new Cookie("Carrello", encode(listaCarrello));
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return listaCarrello;
    }
    
    /* Creo una stringa contenente i codici dei prodotti, le relative quantità e la taglia nel carrello dell'utente concatenati ma separati da un # */
    private String encode(ArrayList<Carrello> carrello) {
        String encodedTemp = new String();
        for(int i=0 ; i<carrello.size() ; i++){
            encodedTemp += carrello.get(i).getCodiceProd() + "#" + carrello.get(i).getQuantita() + "#" + carrello.get(i).getTaglia() + "%";
        }
        
        String encoded = encodedTemp.substring(0, encodedTemp.length()-1);
        
        return encoded;
    }
    
    /* Ricevuta la stringa encoded, separo i campi codiceProd, quantità e taglia separati da # e li setto nell'oggetto di Carrello */
    private ArrayList<Carrello> decode(String encoded) {
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String[] values = encoded.split("%");
        
        for(int i=0 ; i<values.length ; i++){
            carrelli.add(decodeAux(values[i]));
        }
        
        return carrelli;
    }
    
    private Carrello decodeAux(String encoded){
        Carrello carrello = new Carrello();
        String[] values = encoded.split("#");
        
        carrello.setCodiceProd(Long.parseLong(values[0]));
        carrello.setQuantità(Long.parseLong(values[1]));
        carrello.setTaglia(values[2]);
        
        return carrello;
    }
    
}
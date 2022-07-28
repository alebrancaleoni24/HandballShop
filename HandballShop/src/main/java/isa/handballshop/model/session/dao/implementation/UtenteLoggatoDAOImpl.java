package isa.handballshop.model.session.dao.implementation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.valueObject.UtenteLoggato;

public class UtenteLoggatoDAOImpl implements UtenteLoggatoDAO{
    
    HttpServletRequest request;
    HttpServletResponse response;
    
    public UtenteLoggatoDAOImpl(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    /* Crea un nuovo cookie UtenteLoggato */
    @Override
    public UtenteLoggato crea(String email, String nome, String cognome, boolean admin) {
        UtenteLoggato ul = new UtenteLoggato();
        ul.setEmail(email);
        ul.setNome(nome);
        ul.setCognome(cognome);
        ul.setAdmin(admin);

        /* Creo un oggetto cookie dandogli il nome UtenteLoggato e settandogli i parametri come li ritorna il metodo encode */
        Cookie cookie = new Cookie("UtenteLoggato", encode(ul));
        /* Specifico che il cookie deve essere valido per tutto il sito */
        cookie.setPath("/");
        /* Aggiungo un cookie alla response e ritorno l'utente loggato */
        response.addCookie(cookie);

        return ul;
    }

    /* Aggiorna il cookie UtenteLoggato */
    @Override
    public void aggiorna(UtenteLoggato ul) {
        Cookie cookie = new Cookie("UtenteLoggato", encode(ul));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /* Elimona il cookie UtenteLoggato */
    @Override
    public void elimina() {
        Cookie cookie = new Cookie("UtenteLoggato", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /* Recupera il cookie UtenteLoggato */
    @Override
    public UtenteLoggato trova() {
        /* Carico l'array dei cookie e l'oggetto UtenteLoggato */
        Cookie[] cookies = request.getCookies();
        UtenteLoggato ul = null;

        /* Se l'array dei cokie non è vuoto, lo scorro tutto oppure fino a quando ul non è più vuoto */
        if (cookies != null) {
            for (int i = 0; i < cookies.length && ul == null; i++) {
                /* Quando trovo il cookie UtenteLoggato chiamo il metodo decode per fargli settare e restituire l'oggetto ul */
                if (cookies[i].getName().equals("UtenteLoggato")) {
                    ul = decode(cookies[i].getValue());
                }
            }
        }

        return ul;
    }
    
    /* Creo una stringa contenente email, nome e cognome dell'utente concatenati ma separati da un # */
    private String encode(UtenteLoggato ul) {
        String encoded;
        String ad = null;
        if(ul.isAdmin()){
            ad="S";
        }else{
            ad="N";
        }
        encoded = ul.getEmail() + "#" + ul.getNome() + "#" + ul.getCognome() + "#" + ad;
        return encoded;
    }
    
    /* Ricevuta la stringa encoded, separo i campi email, nome e cognome separati da # e li setto nell'oggetto di UtenteLoggato */
    private UtenteLoggato decode(String encoded) {
        UtenteLoggato ul = new UtenteLoggato();
        String[] values = encoded.split("#");

        ul.setEmail(values[0]);
        ul.setNome(values[1]);
        ul.setCognome(values[2]);
        if(values[3].equals("S")){
            ul.setAdmin(true);
        }else{
            ul.setAdmin(false);
        }

        return ul;
    }
    
}
package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;
import isa.handballshop.model.dao.ContieneDAO;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.OrdineDAO;
import isa.handballshop.model.dao.PagamentoDAO;
import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.dao.UtenteDAO;
import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.UtenteLoggato;
import isa.handballshop.model.valueObject.Utente;

import isa.handballshop.services.logservice.LogService;

public class UtentiManagement {
    
    /*
    * Classe per gestire gli utenti (solo admin)
    * utenti.jsp
    */
    
    /* Metodo per eseguire la view di utenti.jsp */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti da visualizzare in base all'iniziale selezionata 
            e il numero di ordini effettuato da ognuno */
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller UtentiManagement", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }

    /* Metodo per bloccare l'account di un utente e fare la view di utenti.jsp */
    public static void bloccaUtente(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            
            /* Recupero l'email dell'utente da bloccare */
            String email = request.getParameter("email");
            
            /*Blocco l'utente*/
            utenteDAO.bloccaUtente(email);

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti da visualizzare in base all'iniziale selezionata 
            e il numero di ordini effettuato da ognuno */
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller UtentiManagement", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }

    /* Metodo per sbloccare l'account di un utente e fare la view di utenti.jsp */
    public static void sbloccaUtente(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            
            /*Recupero l'email dell'utente da sbloccare*/
            String email = request.getParameter("email");
            
            /*Sblocco l'utente*/
            utenteDAO.sbloccaUtente(email);

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti da visualizzare in base all'iniziale selezionata 
            e il numero di ordini effettuato da ognuno */
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller UtentiManagement", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }

    /* Metodo per eliminare l'account di un utente admin e fare la view di utenti.jsp */
    public static void eliminaUtente(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            
            /*Recupero l'email dell'utente da eliminare*/
            String email = request.getParameter("email");
            
            /*elimino l'utente*/
            utenteDAO.eliminaUtente(email);

            /* Chiamo il metodo per caricare le iniziali degli utenti, gli utenti da visualizzare in base all'iniziale selezionata 
            e il numero di ordini effettuato da ognuno */
            commonView(jdbc, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/utenti");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller UtentiManagement", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }

    /* Metodo per eseguire la view di schedaUtente.jsp */
    public static void schedaUtenteView(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            UtenteDAO utenteDAO = jdbc.getUtenteDAO();
            PagamentoDAO pagamentoDAO = jdbc.getPagamentoDAO();
            ContieneDAO contieneDAO = jdbc.getContieneDAO();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();

            /*Recupero il'email dell'utente da mostrare*/
            String email = request.getParameter("email");

            /* Recupero dal DB l'utente e tutti i suoi ordini */
            Utente utente = utenteDAO.schedaUtente(email);

            /*Per ogni ordine carico il prezzo complessivo*/
            for(int i=0 ; i<utente.getOrdine().size() ; i++){
                utente.getOrdine().get(i).getPagamento().setImporto(pagamentoDAO.getImporto(utente.getOrdine().get(i).getPagamento().getCodicePagamento()));
            }
        
            /*Carico 'CONTIENE'*/
            for(int i=0 ; i<utente.getOrdine().size() ; i++){
                utente.getOrdine().get(i).setContiene(contieneDAO.findContieneByOrdine(utente.getOrdine().get(i).getCodiceOrdine()));
            }
            
            /*Per ogni ordine carico all'interno di ogni contiene il prodotto specificato dal codice*/
            for(int i=0 ; i<utente.getOrdine().size() ; i++){
                for(int j=0 ; j<utente.getOrdine().get(i).getContiene().size() ; j++){
                    utente.getOrdine().get(i).getContiene().get(j).setProdotto(prodottoDAO.findByKey(utente.getOrdine().get(i).getContiene().get(j).getProdotto().getCodiceProdotto()));
                }
            }
            

            jdbc.commitTransaction();

            request.setAttribute("utente", utente);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "utentiManagement/schedaUtente");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller UtentiManagement", e);
            try {
                if(jdbc != null){
                    jdbc.rollbackTransaction();
                }
            }catch(Throwable t){
            }
            throw new RuntimeException(e);
        }finally{
            try{
                if(jdbc != null){
                    jdbc.closeTransaction();
                }
            }catch(Throwable t){
            }
        }
    }
    
    /* Metodo per caricare le iniziali degli utenti, gli utenti da visualizzare in base alla lettera selezionata e il numero di ordini effettuato da ognuno */
    public static void commonView(JDBCDAOFactory jdbc, HttpServletRequest request) {
        ArrayList<String> initials;
        ArrayList<Utente> utenti;

        UtenteDAO utenteDAO = jdbc.getUtenteDAO();
        OrdineDAO ordineDAO = jdbc.getOrdineDAO();

        /*Carico nella lista initials le iniziali degli utenti registrati*/
        initials = utenteDAO.findInitialsUtenti();

        /*Recupero il parametro che contiene la lettera scelta da mostrare*/
        String selectedInitial = request.getParameter("selectedInitial");

        /*Se il parametro è nullo lo setto di default a '*'*/
        if (selectedInitial == null) {
            selectedInitial = "*";
        }

        /*Se selectedInitial vale '*' recupero tutti gli utenti registrati,
        altrimenti solo quelli con l'iniziale selezionata*/
        if(selectedInitial.equals("*")){
            utenti = utenteDAO.findUtenti();
        }else{
            utenti = utenteDAO.findUtentiByInitial(selectedInitial);
        }
        
        Integer temp = 0;
        ArrayList<Integer> numOrdini = new ArrayList<Integer>();
        
        /*Conto, per ogni utente non admin, quanti ordini ha effettuato*/
        for(int i=0; i<utenti.size(); i++){
            if(!utenti.get(i).isAdmin()){
                temp = ordineDAO.contaOrdini(utenti.get(i).getEmail());
            }
            numOrdini.add(temp);
        }

        /*Setto gli attributi del view model*/
        request.setAttribute("selectedInitial", selectedInitial);
        request.setAttribute("initials", initials);
        request.setAttribute("utenti", utenti);
        request.setAttribute("numeroOrdini", numOrdini);

    }
    
}

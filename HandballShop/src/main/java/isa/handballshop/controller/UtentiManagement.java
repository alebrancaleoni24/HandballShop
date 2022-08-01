package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.OrdineDAO;
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

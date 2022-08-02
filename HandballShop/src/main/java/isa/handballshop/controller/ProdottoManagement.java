package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.dao.TagliaDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.UtenteLoggato;
import isa.handballshop.model.valueObject.Prodotto;
import isa.handballshop.services.logservice.LogService;

public class ProdottoManagement {
    
    /*
    * Classe per visualizzare i prodotti presenti in magazzino e gestirli
    * Usata per magazzino.jsp, gestisciProdotto.jsp e gestisciTaglie.jsp
    */
    
    /* Metodo per eseguire la view di magazzino.jsp */
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

            /*Chiamo il metodo uguale a tutte le chiamate per caricare
            tutti i prodotti nel DB*/
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller prodottoManagement", e);
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

    /* Metodo per fare la view di gestisciProdotto.jsp nel caso l'admin voglia crearne uno nuovo */
    public static void inserisciProdottoView(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Setto gli attributi*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciProdotto");

        }catch(Exception e){
            logger.log(Level.SEVERE, "prodottoManagement Controller Error", e);
            throw new RuntimeException(e);
        }
    }

    /* Metodo con cui mi porto dietro i dati del prodotto inseriti e fare la view di gestisciTaglie.jsp */
    public static void inserisciProdotto(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        Logger logger = LogService.printLog();

        try {
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            Prodotto prodotto = new Prodotto();
            
            /*Prelevo i valori inseriti e mi porto dietro il prodotto*/
            String categoria = request.getParameter("categoria");
            prodotto.setCategoria(categoria);
            
            String marca = request.getParameter("marca");
            prodotto.setMarca(marca);
            
            String modello = request.getParameter("modello");
            prodotto.setModello(modello);
            
            String genere = request.getParameter("genere");
            prodotto.setGenere(genere);
            
            String immagine = request.getParameter("immagine");
            prodotto.setImage(immagine);
            
            String descrizione = request.getParameter("descrizione");
            prodotto.setDescrizione(descrizione);
            
            Float prezzo = Float.parseFloat(request.getParameter("prezzo"));
            prodotto.setPrezzo(prezzo);
            
            if(request.getParameter("blocked").equals("S")){
                prodotto.setBlocked(true);
            }else{
                prodotto.setBlocked(false);
            }
            
            if(request.getParameter("push").equals("S")){
                prodotto.setPush(true);
            }else{
                prodotto.setPush(false);
            }
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciTaglie");

        }catch(Exception e){
            logger.log(Level.SEVERE, "ProdottoManagement Controller Error", e);
            throw new RuntimeException(e);
        }
    }

    /* Metodo con cui prelevo i dati del prodotto e della giacenza di magazzino per ogni taglia e aggiorno il DB. Infine faccio la view di magazzino.jsp */
    public static void inserisciTaglia(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        String applicationMessage;
        
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
            
            Prodotto prodotto = null;
            
            /*Recupero i valori del prodotto*/
            String categoria = request.getParameter("categoria");
            String marca = request.getParameter("marca");
            String modello = request.getParameter("modello");
            String genere = request.getParameter("genere");
            String img = request.getParameter("immagine");
            String descrizione = request.getParameter("descrizione");
            Float prezzo = Float.parseFloat(request.getParameter("prezzo"));
            boolean blocked;
            if(request.getParameter("blocked").equals("S")){
                blocked = true;
            }else{
                blocked = false;
            }
            boolean push;
            if(request.getParameter("push").equals("S")){
                push = true;
            }else{
                push = false;
            }
            
            /*INSERISCO IL PRODOTTO NEL DB*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            try{
                prodotto = prodottoDAO.creaProdotto(categoria, marca, modello, genere, img, descrizione, prezzo, blocked, push, null);
            }catch(DuplicatedObjectException doe){
                applicationMessage = "Prodotto già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di un prodotto già esistente");
            }
            
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            long[] quantità = new long[7];
            int i = 0;
            String[] taglie = {"XS","S","M","L","XL","XXL","XXXL"};
            String[] taglieScarpe = {"39","40","41","42","43","44","45"};
            
            /*Prelevo i valori inseriti*/
            quantità[i++] = Long.parseLong(request.getParameter("xs"));
            quantità[i++] = Long.parseLong(request.getParameter("s"));
            quantità[i++] = Long.parseLong(request.getParameter("m"));
            quantità[i++] = Long.parseLong(request.getParameter("l"));
            quantità[i++] = Long.parseLong(request.getParameter("xl"));
            quantità[i++] = Long.parseLong(request.getParameter("xxl"));
            quantità[i++] = Long.parseLong(request.getParameter("xxxl"));
            
            /*INSERISCO LE TAGLIE DEL PRODOTTO NEL DB*/
            for(i=0;i<7;i++){
                try{
                    if(!prodotto.getCategoria().equals("Scarpe")){
                        tagliaDAO.creaTaglia(prodotto.getCodiceProdotto(), quantità[i], taglie[i]);
                    }else{
                        tagliaDAO.creaTaglia(prodotto.getCodiceProdotto(), quantità[i], taglieScarpe[i]);

                    }
                }catch(DuplicatedObjectException doe){
                    applicationMessage = "La taglia per questo prodotto è già esistente";
                    logger.log(Level.INFO, "Tentativo di inserimento di una taglia già esistente");
                }
            }

            /* Chiamo il metodo uguale a tutte le chiamate per caricare tutti i prodotti nel DB */
            commonView(jdbc, sessionDAO, request);

            jdbc.commitTransaction();

            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "prodottoManagement/magazzino");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller ProdottoManagement", e);
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

    /* Metodo uguale a tutte le chiamate di magazzino.jsp per caricare tutti i prodotti nel DB */
    private static void commonView(JDBCDAOFactory jdbc, SessionDAO sessionDAO, HttpServletRequest request) {
        ArrayList<Prodotto> prodotti;

        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();

        prodotti = prodottoDAO.trovaProdotti();

        /* Setto gli attributi del view model */
        request.setAttribute("prodotti", prodotti);

    }
}
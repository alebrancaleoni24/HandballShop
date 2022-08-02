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
import isa.handballshop.model.valueObject.Taglia;
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

    /* Metodo per bloccare la vendita di un prodotto */
    public static void bloccaProdotto(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Recupero il codice del prodotto da bloccare*/
            long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Blocco il prodotto*/
            prodottoDAO.blocca(codiceProd);

            /* Chiamo il metodo uguale a tutte le chiamate per caricare tutti i prodotti nel DB */
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
    
    /* Metodo per sbloccare un prodotto bloccato per la vendita */
    public static void sbloccaProdotto(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Recupero il codice del prodotto da sbloccare*/
            long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Sblocco il prodotto*/
            prodottoDAO.sblocca(codiceProd);

            /* Chiamo il metodo uguale a tutte le chiamate per caricare tutti i prodotti nel DB */
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

    /* Metodo per fare la view di gestisciProdotto.jsp nel caso in cui si vogliano modificare i dati di un prodotto già esistente */
    public static void modificaProdottoView(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Prelevo il codice del prodotto da modificare*/
            long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));
            
            /*Prelevo il prodotto da modificare dal DB*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProd);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciProdotto");

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

    /* Metodo per aggiornare il DB con le modifiche al prodotto apportate dall'admin e fare la view di magazzino.jsp */
    public static void modificaProdotto(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Stabilisco la connessione*/
            jdbc.beginTransaction();
            
            Prodotto prodotto = new Prodotto();
            
            /*Prelevo i valori inseriti*/
            prodotto.setCodiceProdotto(Long.parseLong(request.getParameter("codiceProdotto")));
            prodotto.setCategoria(request.getParameter("categoria"));
            prodotto.setMarca(request.getParameter("marca"));
            prodotto.setModello(request.getParameter("modello"));
            prodotto.setGenere(request.getParameter("genere"));
            prodotto.setImage(request.getParameter("immagine"));
            prodotto.setDescrizione(request.getParameter("descrizione"));
            prodotto.setPrezzo(Float.parseFloat(request.getParameter("prezzo")));
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
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*AGGIORNO IL PRODOTTO NEL DB*/
            try{
                prodottoDAO.aggiorna(prodotto);
            }catch(DuplicatedObjectException doe){
                applicationMessage = "Prodotto già esistente";
                logger.log(Level.INFO, "Tentativo di inserimento di un prodotto già esistente");
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

    /* Metodo per fare la view di gestisciTaglie.jsp nel caso si voglia modificare la giacenza di magazzino di un prodotto */
    public static void disponibilitaView(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Prelevo il codice del prodotto selezionato*/
            long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = new Prodotto();
            prodotto = prodottoDAO.findByKey(codiceProd);
            
            /*Prelevo la disponibilità di magazzino del prodotto selezionato dal DB*/
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            ArrayList<Taglia> taglie = tagliaDAO.getTaglie(codiceProd, prodotto.getCategoria());
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi*/
            request.setAttribute("codiceProdotto", codiceProd);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("taglie", taglie);
            request.setAttribute("viewUrl", "prodottoManagement/gestisciTaglie");

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
    
    /* Metodo per aggiornare il DB con le nuove giacenze di magazzino e fare la view di magazzino.jsp */
    public static void modificaDisponibilita(HttpServletRequest request, HttpServletResponse response){
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
            
            /*Recupero dal DB il prodotto selezionato*/
            long codiceProdotto = Long.parseLong(request.getParameter("codiceProdotto"));
            Prodotto prodotto = new Prodotto();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            prodotto = prodottoDAO.findByKey(codiceProdotto);
            
            Taglia taglia = new Taglia();
            
            long[] quantità = new long[7];
            int i = 0;
            String[] taglie = {"XS","S","M","L","XL","XXL","XXXL"};
            String[] taglieScarpe = {"39","40","41","42","43","44","45"};
            
            /*Recupero tutti i nuovi valori passati dalla jsp*/
            quantità[i++] = Long.parseLong(request.getParameter("xs"));
            quantità[i++] = Long.parseLong(request.getParameter("s"));
            quantità[i++] = Long.parseLong(request.getParameter("m"));
            quantità[i++] = Long.parseLong(request.getParameter("l"));
            quantità[i++] = Long.parseLong(request.getParameter("xl"));
            quantità[i++] = Long.parseLong(request.getParameter("xxl"));
            quantità[i++] = Long.parseLong(request.getParameter("xxxl"));
            
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            
            /*AGGIORNO IL DB*/
            for(i=0;i<7;i++){
                taglia.setCodiceProd(codiceProdotto);
                taglia.setQuantità(quantità[i]);
                if(!prodotto.getCategoria().equals("Scarpe")){
                    taglia.setTaglia(taglie[i]);
                }else{
                    taglia.setTaglia(taglieScarpe[i]);
                }
                tagliaDAO.aggiorna(taglia);
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
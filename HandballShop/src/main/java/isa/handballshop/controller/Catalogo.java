package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.session.dao.*;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.*;
import isa.handballshop.model.valueObject.Prodotto;
import isa.handballshop.services.logservice.LogService;

public class Catalogo{
    
    /*
    * Classe per gestire la visualizzazione e la ricerca dei prodotti da parte dell'utente
    * catalogo.jsp e viewProd.jsp
    */
    
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ArrayList<Prodotto> prodotti = null;
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Metodo per caricare categorie, marche e generi*/
            commonView(jdbc, sessionDAO, request);
            
            /* Estraggo i prodotti da visualizzare in base al filtro di ricerca, se non è applicato nessun filtro, di default prendo i prodotti in push */
            if(request.getParameter("searchType") == null){
                prodotti = prodottoDAO.trovaProdotti();
                prodotti.stream().filter(w -> w.isPush());
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("categoria")){
                prodotti = prodottoDAO.trovaProdotti();
                prodotti.stream().filter(w -> w.getCategoria().equals(request.getParameter("searchName")));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("marca")){
                prodotti = prodottoDAO.trovaProdotti();
                prodotti.stream().filter(w -> w.getMarca().equals(request.getParameter("searchName")));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("genere")){
                prodotti = prodottoDAO.trovaProdotti();
                prodotti.stream().filter(w -> w.getGenere().equals(request.getParameter("searchName")));
            }
            if(request.getParameter("searchType") != null && request.getParameter("searchType").equals("searchString")){
                String cerca = request.getParameter("searchName");
                cerca = cerca.substring(0, 1).toUpperCase() + cerca.substring(1,cerca.length()).toLowerCase();
                prodotti = prodottoDAO.findByString(cerca);
            }
            
            /* Setto gli attributi del viewModel */
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("viewUrl", "catalogo/catalogo");
            
            jdbc.commitTransaction();
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    
    /* Metodo per la view di viewProd.jsp */
    public static void viewProd(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBCDAOFactory jdbc = null;
    
        Logger logger = LogService.printLog();

        try {

            /* Inizializzo la sessione */
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);

            /* Recupero il cookie utente */
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /* Recupero il cookie carrello */
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            /*Inizio la transazione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Recupero la chiave del prodotto selezionato*/
            Long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));

            /*Recupero dal DB il prodotto selezionato*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProd);
            
            /*Metodo per caricare categorie, marche e generi*/
            commonView(jdbc, sessionDAO, request);
      
            jdbc.commitTransaction();

            /*Preparo gli attributi della response*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "catalogo/viewProd");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    
    /* Metodo per aggiungere un prodotto al carrello usato in viewProd.jsp */
    public static void insert(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        JDBCDAOFactory jdbc = null;
    
        Logger logger = LogService.printLog();

        try {

            /*Inizializzo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);

            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Recupero il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.trova();

            /*Recupero la chiave, la taglia e la quantità del prodotto selezionato*/
            Long codiceProd = Long.parseLong(request.getParameter("codiceProdotto"));
            String taglia = request.getParameter("taglia");
            Long quantità = Long.parseLong(request.getParameter("quantita"));

            /*Se un cookie carrello esiste già aggiungo il prodotto al carello, altrimenti ne creo uno nuovo*/
            if(carrelli == null){
                carrelloDAO.crea(codiceProd, quantità, taglia);
            }else{
                carrelloDAO.aggiungi(codiceProd, quantità, taglia);
            }
            
            /*Inizio la transazione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();

            /*Recupero dal DB il prodotto selezionato*/
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            Prodotto prodotto = prodottoDAO.findByKey(codiceProd);
            
            /*Metodo per caricare categorie, marche e generi*/
            commonView(jdbc, sessionDAO, request);
      
            jdbc.commitTransaction();

            /*Preparo gli attributi della response*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            request.setAttribute("prodotto", prodotto);
            request.setAttribute("viewUrl", "catalogo/viewProd");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Catalogo", e);
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
    
    /* Metodo comune per caricare le categorie, le marche e i generi dei prodotti */
    private static void commonView(JDBCDAOFactory jdbc, SessionDAO sessionDAO, HttpServletRequest request) {
        ArrayList<String> categorie;
        ArrayList<String> marche;
        ArrayList<String> generi;
            
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
        /*Estraggo le categorie, le marche e i generi*/
        categorie = prodottoDAO.trovaCategorie();
        marche = prodottoDAO.trovaMarche();
        generi = prodottoDAO.trovaGeneri();
                
        request.setAttribute("categorie", categorie);
        request.setAttribute("marche", marche);
        request.setAttribute("generi", generi);

    }
    
}
package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;

import isa.handballshop.model.dao.ContieneDAO;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.OrdineDAO;
import isa.handballshop.model.dao.PagamentoDAO;
import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.dao.UtenteDAO;

import isa.handballshop.model.session.dao.CarrelloDAO;
import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.Carrello;
import isa.handballshop.model.session.valueObject.UtenteLoggato;

import isa.handballshop.model.valueObject.Contiene;
import isa.handballshop.model.valueObject.Ordine;

import isa.handballshop.services.logservice.LogService;

public class Ordini {
    
    /* 
    * Classe per gestire la visualizzazione degli ordini da parte dei clienti e la modifica dello stato da parte degli admin
    * ordini.jsp e ordiniManagement.jsp
    */
    
    /* Metodo per visualizzare la pagina ordini.jsp. Recupera tutti gli ordini da visualizzare dal DB */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*Metodo per caricare tutti gli ordini nel DB da visualizzare*/
            commonView(jdbc, sessionDAO, request);
            
            jdbc.commitTransaction();
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Ordini", e);
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

    public static void aggiornaStato(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            OrdineDAO ordineDAO = jdbc.getOrdineDAO();
            
            /*Recupero il codice dell'ordine e lo stato*/
            long codiceOrd = Long.parseLong(request.getParameter("codiceOrdine"));
            String stato = request.getParameter("stato");
            
            /*Aggiorno il DB*/
            if(stato.equals("Consegnato")){
                Date dataOdierna = new Date();
                ordineDAO.aggiornaStato(codiceOrd, stato, dataOdierna);
            }else{
                ordineDAO.aggiornaStato(codiceOrd, stato);
            }
            
            /*Metodo per caricare tutti gli ordini nel DB da visualizzare*/
            commonView(jdbc, sessionDAO, request);
            
            jdbc.commitTransaction();
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Ordini", e);
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
    
    /*
    *
    * Metodo comune a tutte le view
    * Carico tutti gli ordini presenti nel DB se l'utente loggato è un admin,
    * altrimenti carico tutti gli ordini che appartengono all'utente loggato
    */
    public static void commonView(JDBCDAOFactory jdbc, SessionDAO sessionDAO, HttpServletRequest request){
        UtenteLoggato ul;
        
        /*Recupero il cookie utente*/
        UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
        ul = ulDAO.trova();
        
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        
        /*Recupero il cookie carrello*/
        CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
        carrelli = carrelloDAO.trova();
            
        ArrayList<Ordine> ordini = null;
        ArrayList<Contiene> contiene = null;
            
        OrdineDAO ordineDAO = jdbc.getOrdineDAO();
        PagamentoDAO pagamentoDAO = jdbc.getPagamentoDAO();
        ContieneDAO contieneDAO = jdbc.getContieneDAO();
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
        UtenteDAO utenteDAO = jdbc.getUtenteDAO();
        
        /*Se si tratta di un admin estraggo dal DB tutti gi ordini
        Se si tratta di un utente estraggo dal DB solo i suoi ordini*/
        if(ul.isAdmin()){
            ordini = ordineDAO.findOrdini();
        }else{
            ordini = ordineDAO.findByUtente(ul.getEmail());
        }
        
        /*Per ogni ordine carico il prezzo complessivo*/
        for(int i=0 ; i<ordini.size() ; i++){
            ordini.get(i).getPagamento().setImporto(pagamentoDAO.getImporto(ordini.get(i).getPagamento().getCodicePagamento()));
        }
        
        /*Carico 'CONTIENE'*/
        for(int i=0 ; i<ordini.size() ; i++){
            ordini.get(i).setContiene(contieneDAO.findContieneByOrdine(ordini.get(i).getCodiceOrdine()));
        }
        
        /*Per ogni ordine carico all'interno di ogni contiene il prodotto specificato dal codice*/
        for(int i=0 ; i<ordini.size() ; i++){
            for(int j=0 ; j<ordini.get(i).getContiene().size() ; j++){
                ordini.get(i).getContiene().get(j).setProdotto(prodottoDAO.findByKey(ordini.get(i).getContiene().get(j).getProdotto().getCodiceProdotto()));
            }
        }
        
        /*Carico nome e cognome dell'utente proprietario di ogni ordine*/
        for(int i=0; i<ordini.size() ; i++){
            ordini.get(i).setUtente(utenteDAO.findByEmail(ordini.get(i).getUtente().getEmail()));
        }
          
        if(ul.isAdmin()){
            request.setAttribute("viewUrl", "ordini/ordiniManagement");
        }else{
            request.setAttribute("carrello", carrelli);
            request.setAttribute("viewUrl", "ordini/ordini");
        }
        request.setAttribute("ordini", ordini);
        request.setAttribute("loggedOn",ul!=null);
        request.setAttribute("loggedUser", ul);
    }
    
}

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
import isa.handballshop.model.session.dao.CarrelloDAO;
import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.Carrello;
import isa.handballshop.model.session.valueObject.UtenteLoggato;
import isa.handballshop.model.valueObject.Prodotto;
import isa.handballshop.services.logservice.LogService;

public class Acquisto {
    
    /*Classe per gestire il carrello e l'ordine del carrello
    *carrello.jsp, pagamento.jsp e riepilogo.jsp*/
    
    private Acquisto(){
    }
    
    /*Metodo per visualizzare carrello.jsp*/
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            /*
            * Metodo comune a tutte le view di carrello.jsp
            * Carico tutti i prodotti dal DB contenuti nel carrello e mappo le disponibilità
            */
            commonView(jdbc, sessionDAO, request);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller Acquisto", e);
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
    * Metodo comune a tutte le view di carrello.jsp
    * Carico tutti i prodotti dal DB contenuti nel carrello e mappo le disponibilità
    */
    public static void commonView(JDBCDAOFactory jdbc, SessionDAO sessionDAO, HttpServletRequest request){
        /*Recupero il cookie carrello*/
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
        carrelli = carrelloDAO.trova();
        double prezzo = 0;
        
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            
        ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
        /*Estraggo i prodotti dal DB*/
        for(int i=0; i<carrelli.size(); i++){
            prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
        }
            
        ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
        TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            
        /*Mappo le disponibilità dei prodotti*/
        for(int j=0; j<prodotti.size(); j++){
            if(carrelli.get(j).getQuantita() < tagliaDAO.getTagliaByKey(carrelli.get(j).getCodiceProd(), carrelli.get(j).getTaglia()).getQuantità() && !prodotti.get(j).isBlocked()){
                disponibilità.add(Boolean.TRUE);
            }else{
                disponibilità.add(Boolean.FALSE);
            }
        }
        
        /*Calcolo il prezzo totale del carrello*/
        for(int i=0; i<prodotti.size(); i++){
            prezzo += prodotti.get(i).getPrezzo()*carrelli.get(i).getQuantita();
        }
        
        /*Arrotondo il prezzo alla seconda cifra decimale*/
        prezzo = Math.round(prezzo*100.0) / 100.0;
        
        /*Setto gli attributi del viewModel*/
        request.setAttribute("prezzo", prezzo);
        request.setAttribute("disponibilita", disponibilità);
        request.setAttribute("prodotti", prodotti);
        request.setAttribute("carrello", carrelli);
    }

}
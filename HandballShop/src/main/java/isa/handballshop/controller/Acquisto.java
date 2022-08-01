package isa.handballshop.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.config.Configuration;
import isa.handballshop.model.dao.ContieneDAO;
import isa.handballshop.model.dao.JDBCDAOFactory;
import isa.handballshop.model.dao.OrdineDAO;
import isa.handballshop.model.dao.PagamentoDAO;
import isa.handballshop.model.dao.ProdottoDAO;
import isa.handballshop.model.dao.TagliaDAO;
import isa.handballshop.model.dao.UtenteDAO;
import isa.handballshop.model.dao.exception.DuplicatedObjectException;
import isa.handballshop.model.session.dao.*;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.*;
import isa.handballshop.model.valueObject.*;
import isa.handballshop.services.logservice.LogService;
import isa.handballshop.services.util.Accounting;

public class Acquisto {
    
    /*Classe per gestire il carrello e l'ordine del carrello
    *carrello.jsp, pagamento.jsp e riepilogo.jsp*/
    
    private Acquisto(){
    }
    
    /*Metodo per visualizzare carrello.jsp*/
    public static void viewCarrello(HttpServletRequest request, HttpServletResponse response){
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

    /* Metodo per portarmi dietro i cookie e fare la view di pagamento.jsp solo
    se ho disponibilità di magazzino sufficiente per tutti i prodotti del carrello */
    public static void viewPagamento(HttpServletRequest request, HttpServletResponse response){
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
            
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
                        
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
            }
            
            /*CONTROLLO LA DISPONIBILITA' DI MAGAZZINO*/
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            boolean disponibilità = true;
            
            for(int i=0; i<carrelli.size(); i++){
                if(carrelli.get(i).getQuantita() > tagliaDAO.getTagliaByKey(carrelli.get(i).getCodiceProd(), carrelli.get(i).getTaglia()).getQuantità() || prodotti.get(i).isBlocked()){
                    disponibilità = false;
                }
            }
            
            if(!disponibilità){
                /*
                * Metodo comune a tutte le view di carrello.jsp
                * Carico tutti i prodotti dal DB contenuti nel carrello e mappo le disponibilità
                */
                commonView(jdbc, sessionDAO, request);
            }
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            if(disponibilità){
                request.setAttribute("carrello", carrelli);
                request.setAttribute("viewUrl", "acquisto/pagamento");
            }else{
                request.setAttribute("viewUrl", "acquisto/carrello");
                request.setAttribute("applicationMessage", "Uno o più prodotti non sono disponibili per l'acquisto");
            }
            
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

    /*Metodo per passare alla schermata di riepilogo dell'ordine*/
    public static void viewRiepilogo(HttpServletRequest request, HttpServletResponse response){
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
            
            /* Ricavo tutti i valori inseriti dall'utente e calcolo il prezzo */
            String carta = request.getParameter("carta");
            String nazione = request.getParameter("nazione");
            String città = request.getParameter("citta");
            String via = request.getParameter("via");
            String numeroCivico = request.getParameter("numeroCivico");
            String CAP = request.getParameter("CAP");
            
            /* Carico i prodotti del carrello dal DB */
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
            }
            
            /*Calcolo il prezzo totale del carrello*/
            ArrayList<Double> prezzi = new ArrayList<>();
            ArrayList<Long> quantita = new ArrayList<>();
            
            for(int i=0; i<prodotti.size(); i++){
                prezzi.add((double) prodotti.get(i).getPrezzo());
                quantita.add(carrelli.get(i).getQuantita());
            }

            double prezzo = Accounting.calcoloPrezzo(prezzi, quantita);
            double iva = Accounting.calcoloIVA(Accounting.calcoloPrezzo(prezzi, quantita), 22);
            
            request.setAttribute("carta", carta);
            request.setAttribute("nazione", nazione);
            request.setAttribute("citta", città);
            request.setAttribute("via", via);
            request.setAttribute("numeroCivico", numeroCivico);
            request.setAttribute("CAP", CAP);
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("iva", iva);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("viewUrl", "acquisto/riepilogo");
            
            jdbc.commitTransaction();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("carrello", carrelli);
            
            
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

    /* Metodo per il pagamento del carrello, view di carrello.jsp */
    public static void paga(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        String applicationMessage = null;
        
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
            
            boolean disponibilità = true;
            
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            Taglia taglia = null;
            
            /*Estraggo i prodotti dal DB*/
            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
            }
            
            /*CONTROLLO DISPONIBILITA*/
            for(int j=0; j<carrelli.size(); j++){
                if(carrelli.get(j).getQuantita() > tagliaDAO.getTagliaByKey(carrelli.get(j).getCodiceProd(), carrelli.get(j).getTaglia()).getQuantità() || prodotti.get(j).isBlocked()){
                    disponibilità = false;
                }
            }
            
            /* Se nel frattempo è rimasta disponibilità procedo all'acquisto, altrimenti mando un messaggio all'utente */
            if(disponibilità){
            
                /*Carico l'utente loggato*/
                UtenteDAO utenteDAO = jdbc.getUtenteDAO();
                Utente utente = utenteDAO.findByEmail(ul.getEmail());
            
                Pagamento pagamento = null;
                PagamentoDAO pagamentoDAO = jdbc.getPagamentoDAO();
            
                /*****INIZIO TRANSAZIONE*****/
                Date dataOdierna = new Date();
                
                /*INSERIMENTO PAGAMENTO NEL DB*/
                try{
                    pagamento = pagamentoDAO.creaPagamento("confermato", request.getParameter("carta"), 
                            dataOdierna, dataOdierna, Float.parseFloat(request.getParameter("prezzo")),
                            utente, null);
                }catch(DuplicatedObjectException doe){
                    applicationMessage = "Pagamento già esistente";
                    logger.log(Level.INFO, "Tentativo di inserimento di un pagamento già esistente");
                }
            
                Ordine ordine = null;
                OrdineDAO ordineDAO = jdbc.getOrdineDAO();
            
                /* Imposto una data di consegna di default 3 giorni successivi alla data dell'ordine */
                java.util.Date dataConsegna = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(dataConsegna);
                cal.add(Calendar.DATE, 3);
                dataConsegna = cal.getTime();
            
                /*INSERIMENTO ORDINE NEL DB*/
                try{
                    ordine = ordineDAO.creaOrdine(dataOdierna, "In preparazione", dataConsegna, request.getParameter("nazione"), 
                            request.getParameter("citta"), request.getParameter("via"), request.getParameter("numeroCivico"), 
                            Integer.parseInt(request.getParameter("CAP")), pagamento, utente, null);
                }catch(DuplicatedObjectException doe){
                    applicationMessage = "Ordine già esistente";
                    logger.log(Level.INFO, "Tentativo di inserimento di un ordine già esistente");
                }
            
                Contiene contiene = null;
                ContieneDAO contieneDAO = jdbc.getContieneDAO();
            
                /*INSERIMENTO CONTIENE NEL DB*/
                for(int i=0; i<carrelli.size(); i++){
                    try{
                        contiene = contieneDAO.creaContiene(ordine.getCodiceOrdine(), carrelli.get(i).getCodiceProd(), 
                            carrelli.get(i).getQuantita(), carrelli.get(i).getTaglia());
                    }catch(DuplicatedObjectException doe){
                        applicationMessage = "Contiene già esistente";
                        logger.log(Level.INFO, "Tentativo di inserimento di un 'Contiene' già esistente");
                    }
                }
            
                /*MODIFICA GIACENZA NEL DB*/
                for(int i=0; i<carrelli.size(); i++){
                    taglia = tagliaDAO.getTagliaByKey(carrelli.get(i).getCodiceProd(), carrelli.get(i).getTaglia());
                    taglia.setQuantità(taglia.getQuantità()-carrelli.get(i).getQuantita());
                    tagliaDAO.aggiorna(taglia);
                }
            
                /*CANCELLA COOCKIE CARRELLO*/
                carrelloDAO.elimina();
                applicationMessage = "Ordine avvenuto con successo";
                
                /*
                * Dopo la transazione ritorno alla pagina del carrello che, anche se vuoto, si aspetta di ricevere un parametro chiamato prezzo e
                * quindi gliene passo uno fittizio
                */
                double prezzo = 0;
                double iva = 0;
                request.setAttribute("prezzo", prezzo);
                request.setAttribute("iva", iva);
            }else{
                /* Caso in cui sia cambiata la disponibilità di magazzino */
                applicationMessage = "Disponibilità modificata durante la transazione, impossibile procedere all'ordine";
                
                /*
                *
                * Metodo comune a tutte le view di carrello.jsp
                * Carico tutti i prodotti dal DB contenuti nel carrello e mappo le disponibilità
                */
                commonView(jdbc, sessionDAO, request);
            }
              
            jdbc.commitTransaction();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("applicationMessage", applicationMessage);
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

    /* Metodo per cambiare la quantità di un prodotto nel carrello */
    public static void cambiaQuantita(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        
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
            /*Modifico il cookie*/
            carrelli = carrelloDAO.modificaQuantità(Long.parseLong(request.getParameter("codiceProdotto")), Long.parseLong(request.getParameter("quantita")), request.getParameter("taglia"));
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
            }
            
            ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            
            /*Mappo le disponibilità dei prodotti*/
            for(int j=0; j<prodotti.size(); j++){
                if(carrelli.get(j).getQuantita() < tagliaDAO.getTagliaByKey(carrelli.get(j).getCodiceProd(), carrelli.get(j).getTaglia()).getQuantità()){
                    disponibilità.add(Boolean.TRUE);
                }else{
                    disponibilità.add(Boolean.FALSE);
                }
            }
            
            /*Calcolo il prezzo totale del carrello*/
            ArrayList<Double> prezzi = new ArrayList<>();
            ArrayList<Long> quantita = new ArrayList<>();
            
            for(int i=0; i<prodotti.size(); i++){
                prezzi.add((double) prodotti.get(i).getPrezzo());
                quantita.add(carrelli.get(i).getQuantita());
            }

            double prezzo = Accounting.calcoloPrezzo(prezzi, quantita);
            double iva = Accounting.calcoloIVA(Accounting.calcoloPrezzo(prezzi, quantita), 22);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("iva", iva);
            request.setAttribute("disponibilita", disponibilità);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("carrello", carrelli);
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

    /* Metodo per rimuovere un prodotto dal carrello, carrello.jsp */
    public static void rimuovi(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        ArrayList<Carrello> carrelli = new ArrayList<Carrello>();
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        
        JDBCDAOFactory jdbc = null;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Rimuovo il prodotto dal carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelli = carrelloDAO.rimuovi(Long.parseLong(request.getParameter("codiceProdotto")), request.getParameter("taglia"));
            
            /*Stabilisco la connessione*/
            jdbc = JDBCDAOFactory.getJDBCImpl(Configuration.DAO_IMPL);
            jdbc.beginTransaction();
            
            ProdottoDAO prodottoDAO = jdbc.getProdottoDAO();
            
            /*Estraggo i prodotti dal DB*/
            for(int i=0; i<carrelli.size(); i++){
                prodotti.add(prodottoDAO.findByKey(carrelli.get(i).getCodiceProd()));
            }
            
            ArrayList<Boolean> disponibilità = new ArrayList<Boolean>();
            TagliaDAO tagliaDAO = jdbc.getTagliaDAO();
            
            /*Mappo le disponibilità dei prodotti*/
            for(int j=0; j<prodotti.size(); j++){
                if(carrelli.get(j).getQuantita() < tagliaDAO.getTagliaByKey(carrelli.get(j).getCodiceProd(), carrelli.get(j).getTaglia()).getQuantità()){
                    disponibilità.add(Boolean.TRUE);
                }else{
                    disponibilità.add(Boolean.FALSE);
                }
            }

            /*Calcolo il prezzo totale del carrello*/
            ArrayList<Double> prezzi = new ArrayList<>();
            ArrayList<Long> quantita = new ArrayList<>();
            
            for(int i=0; i<prodotti.size(); i++){
                prezzi.add((double) prodotti.get(i).getPrezzo());
                quantita.add(carrelli.get(i).getQuantita());
            }

            double prezzo = Accounting.calcoloPrezzo(prezzi, quantita);
            double iva = Accounting.calcoloIVA(Accounting.calcoloPrezzo(prezzi, quantita), 22);
            
            jdbc.commitTransaction();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("iva", iva);
            request.setAttribute("disponibilita", disponibilità);
            request.setAttribute("prodotti", prodotti);
            request.setAttribute("carrello", carrelli);
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

    /* Metodo per cancellare l'intero carrello, carrello.jsp */
    public static void cancella(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        double prezzo = 0;
        double iva = 0;
        
        Logger logger = LogService.printLog();
        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            /*Elimino il cookie carrello*/
            CarrelloDAO carrelloDAO = sessionDAO.getCarrelloDAO();
            carrelloDAO.elimina();
            
            /*Setto gli attributi del viewModel*/
            request.setAttribute("prezzo", prezzo);
            request.setAttribute("iva", iva);
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "acquisto/carrello");
            
        }catch(Exception e){
            logger.log(Level.SEVERE, "Controller Error", e);
            throw new RuntimeException(e);
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
        ArrayList<Double> prezzi = new ArrayList<>();
        ArrayList<Long> quantita = new ArrayList<>();
        
        for(int i=0; i<prodotti.size(); i++){
            prezzi.add((double) prodotti.get(i).getPrezzo());
            quantita.add(carrelli.get(i).getQuantita());
        }

        double prezzo = Accounting.calcoloPrezzo(prezzi, quantita);
        double iva = Accounting.calcoloIVA(Accounting.calcoloPrezzo(prezzi, quantita), 22);
        
        /*Setto gli attributi del viewModel*/
        request.setAttribute("prezzo", prezzo);
        request.setAttribute("iva", iva);
        request.setAttribute("disponibilita", disponibilità);
        request.setAttribute("prodotti", prodotti);
        request.setAttribute("carrello", carrelli);
    }

}
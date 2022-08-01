package isa.handballshop.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;
import isa.handballshop.model.session.dao.implementation.SessionDAOImpl;
import isa.handballshop.model.session.valueObject.UtenteLoggato;

import isa.handballshop.services.logservice.LogService;

public class HomeAdmin {
    
    /* Classe che gestisce la vista di homeAdmin.jsp */
    
    /* Metodo per la view di homeAdmin.jsp */
    public static void view(HttpServletRequest request, HttpServletResponse response){
        SessionDAO sessionDAO;
        UtenteLoggato ul;
        
        Logger logger = LogService.printLog();

        try{
            /*Creo la sessione*/
            sessionDAO = new SessionDAOImpl();
            sessionDAO.initSession(request, response);
            
            /*Recupero il cookie utente*/
            UtenteLoggatoDAO ulDAO = sessionDAO.getUtenteLoggatoDAO();
            ul = ulDAO.trova();
            
            request.setAttribute("loggedOn",ul!=null);
            request.setAttribute("loggedUser", ul);
            request.setAttribute("viewUrl", "homeAdmin/homeAdmin");

        }catch(Exception e){
            logger.log(Level.SEVERE, "Errore Controller HomeAdmin", e);
            throw new RuntimeException(e);
        }
    }
}
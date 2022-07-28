package isa.handballshop.model.session.dao.implementation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.model.session.dao.CarrelloDAO;
import isa.handballshop.model.session.dao.SessionDAO;
import isa.handballshop.model.session.dao.UtenteLoggatoDAO;

public class SessionDAOImpl implements SessionDAO{
    
    private HttpServletRequest request;
    private HttpServletResponse response;

    /* Inizializza la sessione memorizzando la request e la response */

    @Override
    public void initSession(HttpServletRequest request, HttpServletResponse response) {
        try{
            this.request = request;
            this.response = response;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public UtenteLoggatoDAO getUtenteLoggatoDAO() {
        return new UtenteLoggatoDAOImpl(request, response);
    }

    @Override
    public CarrelloDAO getCarrelloDAO() {
        return new CarrelloDAOImpl(request, response);
    }
    
}
package isa.handballshop.model.session.dao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionDAO {
    
    public void initSession(HttpServletRequest request, HttpServletResponse response);
    
    public UtenteLoggatoDAO getUtenteLoggatoDAO();
    
    public CarrelloDAO getCarrelloDAO();
    
}
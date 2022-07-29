package isa.handballshop.dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isa.handballshop.services.logservice.LogService;

/*Annotation che mi definisce l'url*/
@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
public class Dispatcher extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*Specifico che il contenuto della response sarà un html*/
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            /*Metto nella stringa controllerAction il parametro ricevuto nella request*/
            String controllerAction = request.getParameter("controllerAction");
      
            /*Nel caso in cui si arrivi da index (quindi è nullo) lo setto di default al catalogo*/
            if(controllerAction == null) controllerAction="Catalogo.view";
      
            String[] splittedAction=controllerAction.split("\\.");
      
            Class<?> controllerClass = Class.forName("isa.handballshop.controller." + splittedAction[0]);
            Method controllerMethod = controllerClass.getMethod(splittedAction[1], HttpServletRequest.class, HttpServletResponse.class);
      
            /*Vado a loggare come livello info le informazioni passate*/
            LogService.printLog().log(Level.INFO, splittedAction[0] + " " + splittedAction[1]);
      
            /*Invoco il metodo determinato in precedenza*/
            controllerMethod.invoke(null, request, response);
      
            String viewUrl=(String)request.getAttribute("viewUrl");
            RequestDispatcher view = request.getRequestDispatcher("/jsp/"+viewUrl+".jsp");
            view.forward(request,response);
        }catch(Exception e){
            e.printStackTrace(out);
            throw new ServerException("Dispacther Servlet Error",e);
        }finally{      
            out.close();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP
   * <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP
   * <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}

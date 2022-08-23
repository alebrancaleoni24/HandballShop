<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Utente"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    String selectedInitial = (String) request.getAttribute("selectedInitial");
    
    ArrayList<String> initials = (ArrayList<String>) request.getAttribute("initials");
    int numInitials;
    if(initials == null){
        numInitials = 0;
    }else{
        numInitials = initials.size();
    }
    
    ArrayList<Utente> utenti = (ArrayList<Utente>) request.getAttribute("utenti");
    int numUtenti;
    if(utenti == null){
        numUtenti = 0;
    }else{
        numUtenti = utenti.size();
    }
    
    ArrayList<Integer> numOrdini = (ArrayList<Integer>) request.getAttribute("numeroOrdini");
    int numOrd;
    if(numOrdini == null){
        numOrd = 0;
    }else{
        numOrd = numOrdini.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Utenti";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <style>
            .initial {  
                color: black;
            }

            .selectedInitial { 
                color: steelblue;
            }
            .email{
                font-size: 0.9em;
            }
        </style>

        <script language="javascript">
            function changeInitial(inital){
                document.changeInitialForm.selectedInitial.value = inital;
                document.changeInitialForm.submit();
            }
            
            function bloccaUtente(email){
                document.bloccaUtenteForm.email.value = email;
                document.bloccaUtenteForm.submit();
            }
            
            function sbloccaUtente(email){
                document.sbloccaUtenteForm.email.value = email;
                document.sbloccaUtenteForm.submit();
            }

            function eliminaUtente(email){
                document.eliminaUtenteForm.email.value = email;
                document.eliminaUtenteForm.submit();
            }

            function schedaUtente(email){
                document.schedaUtenteForm.email.value = email;
                document.schedaUtenteForm.submit();
            }
        </script>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <div id="page-container">
            <header>
                <%@include file="/include/headerAdmin.inc"%>
            </header>
            
            <main>
                <div class="nome" style="margin-bottom: 15px;">
                    <p id="loggedOn">Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                </div>

                <div style="clear: both"></div>

                <div style="width: 80%; margin-left: 10%">

                    <div class="clearfix" style="height: 64px;">
                        <section>
                            <!--FORM PER PASSARE ALLA PAGINA DI REGISTRAZIONE DI UN NUOVO UTENTE-->
                            <form name="inserisciUtente" action="Dispatcher" method="post">
                                <input type="hidden" name="controllerAction" value="LogOn.view"/>
                                <input type="hidden" name="opzione" value="R"/>
                                <input type="submit" value="Nuovo utente" class="mainButton" style="float: left;">
                            </form>
                        </section>
                        
                        <!--MINI MENU DI NAVIGAZIONE CON LE INIZIALI DEI NOMI DEGLI UTENTI-->
                        <nav style="margin-left: 3%; height: 30px; margin-top: 20px; float: left;">
                            <%if(selectedInitial.equals("*")){%>
                                <span class="selectedInitial">*</span>
                            <%}else{%>
                                <a class="initial" href="javascript:changeInitial('*');">*</a>
                            <%}%>
                            
                            <%for(i=0 ; i<numInitials ; i++){
                                if(initials.get(i).equals(selectedInitial)){%>
                                    <span class="selectedInitial"><%=initials.get(i)%></span>
                                <%}else{%>
                                    <a class="initial" href="javascript:changeInitial('<%=initials.get(i)%>');"><%=initials.get(i)%></a>
                                <%}%>  
                            <%}%>
                        </nav>
                    </div>
            
                    
                    <section class="clearfix">
                        
                        <!--LISTA DEGLI UTENTI DA MOSTRARE-->
                        <%for (i=0 ; i<numUtenti ; i++) {%>           
                            <article id="box">
                                <h1><a href="javascript:schedaUtente('<%=utenti.get(i).getEmail()%>');"><%=utenti.get(i).getNome()%> <%=utenti.get(i).getCognome()%></a></h1>
                                <span class="email"><%=utenti.get(i).getEmail()%></span>
                                <address>
                                    <%=utenti.get(i).getVia()%>, n.<%=utenti.get(i).getNumeroCivico()%><br/>
                                    <%=utenti.get(i).getCittà()%>, <%=utenti.get(i).getNazione()%><br/>   
                                </address>
                                <%if(!utenti.get(i).isAdmin()){%>
                                    <p>Ordini effettuati: <%=numOrdini.get(i)%></p>
                                <%}else{%>
                                    <p>Admin</p>
                                <%}%>
                                
                                <!--BOTTONI PER BLOCCARE O SBLOCCARE UN UTENTE-->
                                <%if(!utenti.get(i).isBlocked()){%>
                                    <a class="button" href="javascript:bloccaUtente('<%=utenti.get(i).getEmail()%>');">Blocca</a>
                                <%}else{%>
                                    <a class="button" href="javascript:sbloccaUtente('<%=utenti.get(i).getEmail()%>');">Sblocca</a>
                                <%}%>

                                <!-- BOTTONE PER ELIMINARE UN UTENTE -->
                                <%if(utenti.get(i).isAdmin() && !utenti.get(i).getEmail().equals(ul.getEmail())){%>
                                    <a class="button" href="javascript:eliminaUtente('<%=utenti.get(i).getEmail()%>');">Elimina</a>
                                <%}%>
                            </article>
                        <%}%>
                    </section>
                    
                    <!--FORM PER CAMBIARE IL FILTRO DEGLI UTENTI IN BASE ALL'INIZIALE SELEZIONATA-->
                    <form name="changeInitialForm" method="post" action="Dispatcher">
                        <input type="hidden" name="selectedInitial"/>
                        <input type="hidden" name="controllerAction" value="UtentiManagement.view"/>      
                    </form>
                    
                    <!--FORM PER BLOCCARE L'UTENTE SELEZIONATO-->
                    <form name="bloccaUtenteForm" method="post" action="Dispatcher">
                        <input type="hidden" name="selectedInitial" value="<%=selectedInitial%>"/>
                        <input type="hidden" name="email"/>
                        <input type="hidden" name="controllerAction" value="UtentiManagement.bloccaUtente"/>      
                    </form>
                        
                    <!--FORM PER SBLOCCARE L'UTENTE SELEZIONATO-->
                    <form name="sbloccaUtenteForm" method="post" action="Dispatcher">
                        <input type="hidden" name="selectedInitial" value="<%=selectedInitial%>"/>
                        <input type="hidden" name="email"/>
                        <input type="hidden" name="controllerAction" value="UtentiManagement.sbloccaUtente"/>      
                    </form>

                    <!--FORM PER ELIMINARE L'UTENTE SELEZIONATO-->
                    <form name="eliminaUtenteForm" method="post" action="Dispatcher">
                        <input type="hidden" name="selectedInitial" value="<%=selectedInitial%>"/>
                        <input type="hidden" name="email"/>
                        <input type="hidden" name="controllerAction" value="UtentiManagement.eliminaUtente"/>      
                    </form>

                    <!--FORM PER PASSARE ALLA SCHEDA UTENTE DELL'UTENTE SELEZIONATO-->
                    <form name="schedaUtenteForm" method="post" action="Dispatcher">
                        <input type="hidden" name="email"/>
                        <input type="hidden" name="controllerAction" value="UtentiManagement.schedaUtenteView"/>      
                    </form>
                    
                </div>

            </main>

            <div id="push"></div>

        </div>

        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

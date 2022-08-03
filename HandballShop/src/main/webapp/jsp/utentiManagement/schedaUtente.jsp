<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Utente"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    Utente utente = Utente request.getAttribute("utente");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String menuActiveLink = "Utenti";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <style>
            .email{
                font-size: 0.9em;
            }
        </style>
        <script language="javascript">
            
            function bloccaUtente(email){
                document.bloccaUtenteForm.email.value = email;
                document.bloccaUtenteForm.submit();
            }
            
            function sbloccaUtente(email){
                document.sbloccaUtenteForm.email.value = email;
                document.sbloccaUtenteForm.submit();
            }

        </script>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/headerAdmin.inc"%>
        </header>
        
        <hr>
        
        <main>
            
            <div class="nome" style="margin-bottom: 15px;">
                <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
            </div>
            
            <section id="box" name="profilo" class="clearfix">

                <!-- DATI DELL'UTENTE -->

                <!--BOTTONI PER BLOCCARE O SBLOCCARE UN UTENTE-->
                <%if(!utenti.get(i).isBlocked()){%>
                    <a class="button" href="javascript:bloccaUtente('<%=utenti.get(i).getEmail()%>');">Blocca</a>
                <%}else{%>
                    <a class="button" href="javascript:sbloccaUtente('<%=utenti.get(i).getEmail()%>');">Sblocca</a>
                <%}%>

            </section>

            <section id="box" name="ordini" class="clearfix">

                <!--LISTA DEGLI ORDINI DA MOSTRARE-->
                <%for (i=0 ; i<utente.getOrdini.size() ; i++) {%>           
                    <article>
                        <hr>
                        <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">
                            <div style="float: left; width: 60%">
                            
                                <%for(j=0 ; j<utente.getOrdini().get(i).getContiene().size() ; j++){%>
                                    <div class="clearfix">
                            
                                        <!--IMMAGINE DEL PRODOTTO-->
                                        <div style="float: left; padding-left: 2%; padding-right: 2%;">
                                            <img id="ProdImage" src="/images/<%=utente.getOrdini().get(i).getContiene().get(j).getProdotto().getImage()%>" width="186" height="186"/>
                                        </div>
                                        
                                        <!--INFORMAZIONI DEL PRODOTTO-->
                                        <div style="float: left; padding-left: 2%; padding-right: 2%; padding-top: 50px;">
                                            <p><b><%=utente.getOrdini().get(i).getContiene().get(j).getProdotto().getModello()%></b></br>Prezzo unitario: €<%=utente.getOrdini().get(i).getContiene().get(j).getProdotto().getPrezzo()%></br>Quantit&agrave;: <%=utente.getOrdini().get(i).getContiene().get(j).getQuantità()%></br>Taglia: <%=utente.getOrdini().get(i).getContiene().get(j).getTaglia().getTaglia()%></p>
                                        </div>
                                        
                                    </div>   
                                <%}%>
                            </div>
                        
                            <!--INFORMAZIONI GENERALI DELL'ORDINE-->
                            <div style="float: left; width: 40%">
                                <h3>Dettagli ordine</h3><br>
                                <p><b>Cliente:</b> <%=utente.getOrdini().get(i).getUtente().getNome()%> <%=utente.getOrdini().get(i).getUtente().getCognome()%></p>
                                <p><b>Data ordine: </b><%=utente.getOrdini().get(i).getDataOrdineString()%></br><b>Data consegna: </b><%=utente.getOrdini().get(i).getDataConsegnaString()%></br><b>Indirizzo di consegna: </b><%=utente.getOrdini().get(i).getVia()%> n° <%=utente.getOrdini().get(i).getNumeroCivico()%>, <%=utente.getOrdini().get(i).getCittà()%>, <%=utente.getOrdini().get(i).getNazione()%></br><b>Importo:</b> €<%=utente.getOrdini().get(i).getPagamento().getImporto()%></br></p>
                                <%if(utente.getOrdini().get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                    <p style="color: green">Consegnato</p>
                                <%}else{%>
                                    <p style="color: orangered"><%=utente.getOrdini().get(i).getStato()%></p>
                                <%}%>
                            
                                <!--FORM PER LA MODIFICA DELLO STATO DELL'ORDINE-->
                                <%if(!utente.getOrdini().get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                    <form name="aggiornaStato<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Ordini.aggiornaStato"/>
                                        <input type="hidden" name="codiceOrdine" value="<%=utente.getOrdini().get(i).getCodiceOrdine()%>"/>
                                        <label for="stato">Stato: </label>
                                        <select id="stato" name="stato" onchange="statoOrdine(<%=i%>)">
                                            <option value="In preparazione" <%if(utente.getOrdini().get(i).getStato().equalsIgnoreCase("In preparazione")){%>selected="selected"<%}%>>In preparazione</option>
                                            <option value="In viaggio" <%if(utente.getOrdini().get(i).getStato().equalsIgnoreCase("In viaggio")){%>selected="selected"<%}%>>In viaggio</option>
                                            <option value="Consegnato" <%if(utente.getOrdini().get(i).getStato().equalsIgnoreCase("Consegnato")){%>selected="selected"<%}%>>Consegnato</option>
                                        </select>
                                     </form>
                                <%}%>
                            </div>
                        </div>   
                    </article>
                <%}%>

            </section>
               
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
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

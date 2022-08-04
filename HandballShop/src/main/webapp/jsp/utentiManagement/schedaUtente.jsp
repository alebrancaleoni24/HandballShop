<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Utente"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0, j=0;
    
    Utente utente = (Utente) request.getAttribute("utente");
    
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
            .email{
                font-size: 0.9em;
            }
        </style>
        <script language="javascript">

            function statoOrdine(index){
                var f = document.forms["aggiornaStato" + index];
                f.submit();
                return;
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
                <article>
                    <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">
                        <h1>Profilo Utente</h1>
                        <div class="clearfix">
                            <table>
                                <tr>
                                    <td>Nome</td>
                                    <td><%=utente.getNome()%></td>
                                </tr>
                                <tr>
                                    <td>Cognome</td>
                                    <td><%=utente.getCognome()%></td>
                                </tr>
                                <tr>
                                    <td>E-Mail</td>
                                    <td><%=utente.getEmail()%></td>
                                </tr>
                                <tr>
                                    <td>Paese</td>
                                    <td><%=utente.getNazione()%></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </article>

            </section>

            <section id="box" name="ordini" class="clearfix">

                <!--LISTA DEGLI ORDINI DA MOSTRARE-->
                <%for (i=0 ; i < utente.getOrdine().size() ; i++) {%>           
                    <article>
                        <hr>
                        <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">
                            <div style="float: left; width: 60%">
                            
                                <%for(j=0 ; j < utente.getOrdine().get(i).getContiene().size() ; j++){%>
                                    <div class="clearfix">
                            
                                        <!--IMMAGINE DEL PRODOTTO-->
                                        <div style="float: left; padding-left: 2%; padding-right: 2%;">
                                            <img id="ProdImage" src="/images/<%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getImage()%>" width="186" height="186"/>
                                        </div>
                                        
                                        <!--INFORMAZIONI DEL PRODOTTO-->
                                        <div style="float: left; padding-left: 2%; padding-right: 2%; padding-top: 50px;">
                                            <p><b><%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getModello()%></b></br>Prezzo unitario: €<%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getPrezzo()%></br>Quantit&agrave;: <%=utente.getOrdine().get(i).getContiene().get(j).getQuantità()%></br>Taglia: <%=utente.getOrdine().get(i).getContiene().get(j).getTaglia().getTaglia()%></p>
                                        </div>
                                        
                                    </div>   
                                <%}%>
                            </div>
                        
                            <!--INFORMAZIONI GENERALI DELL'ORDINE-->
                            <div style="float: left; width: 40%">
                                <h3>Dettagli ordine</h3>
                                <p><b>Data ordine: </b><%=utente.getOrdine().get(i).getDataOrdineString()%></br><b>Data consegna: </b><%=utente.getOrdine().get(i).getDataConsegnaString()%></br><b>Indirizzo di consegna: </b><%=utente.getOrdine().get(i).getVia()%> n° <%=utente.getOrdine().get(i).getNumeroCivico()%>, <%=utente.getOrdine().get(i).getCittà()%>, <%=utente.getOrdine().get(i).getNazione()%></br><b>Importo:</b> €<%=utente.getOrdine().get(i).getPagamento().getImporto()%></br></p>
                                <%if(utente.getOrdine().get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                    <p style="color: green">Consegnato</p>
                                <%}else{%>
                                    <p style="color: orangered"><%=utente.getOrdine().get(i).getStato()%></p>
                                <%}%>
                            
                                <!--FORM PER LA MODIFICA DELLO STATO DELL'ORDINE-->
                                <%if(!utente.getOrdine().get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                    <form name="aggiornaStato<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Ordini.aggiornaStato"/>
                                        <input type="hidden" name="codiceOrdine" value="<%=utente.getOrdine().get(i).getCodiceOrdine()%>"/>
                                        <label for="stato">Stato: </label>
                                        <select id="stato" name="stato" onchange="statoOrdine(<%=i%>)">
                                            <option value="In preparazione" <%if(utente.getOrdine().get(i).getStato().equalsIgnoreCase("In preparazione")){%>selected="selected"<%}%>>In preparazione</option>
                                            <option value="In viaggio" <%if(utente.getOrdine().get(i).getStato().equalsIgnoreCase("In viaggio")){%>selected="selected"<%}%>>In viaggio</option>
                                            <option value="Consegnato" <%if(utente.getOrdine().get(i).getStato().equalsIgnoreCase("Consegnato")){%>selected="selected"<%}%>>Consegnato</option>
                                        </select>
                                     </form>
                                <%}%>
                            </div>
                        </div>   
                    </article>
                <%}%>

            </section>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

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

            table{
                width: 80%;
                margin-left: 10%;
                margin-top: 2%;
            }

            #sx{
                width: 40%;
                padding-bottom: 1%;
                font-weight: bold;
            }

            #dx{
                width: 60%;
                padding-bottom: 1%;
                border: 1px solid #000000;
                border-radius: 8px;
            }

            #content{
                margin-top: 2%;
                margin-bottom: 2%;
                margin-left: 1%;
                margin-right: 1%;
                padding-left: 1%;
                padding-right: 1%;
                padding-top: 2%;
                padding-bottom: 2%;
                background-color: rgb(241, 244, 247);
                border-radius: 5px;
            }

            .select{
                font-size: medium;
                border-radius: 5px;
                width: 50%;
                background-color: white;
            }

            #profilo{
                width: 92%;
                margin-left: 4%;
                margin-right: 4%;
                font-size: large;
                line-height: 1.5;
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
            
                    <section id="profilo" name="profilo" class="clearfix">

                        <!-- DATI DELL'UTENTE -->
                        <article>
                            <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">
                                <h1>Profilo Utente</h1>
                                <div class="clearfix">
                                    <table>
                                        <tr>
                                            <td id="sx">Nome</td>
                                            <td id="dx"><%=utente.getNome()%></td>
                                        </tr>
                                        <tr>
                                            <td id="sx">Cognome</td>
                                            <td id="dx"><%=utente.getCognome()%></td>
                                        </tr>
                                        <tr>
                                            <td id="sx">E-Mail</td>
                                            <td id="dx"><%=utente.getEmail()%></td>
                                        </tr>
                                        <tr>
                                            <td id="sx">Paese</td>
                                            <td id="dx"><%=utente.getNazione()%></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </article>

                    </section>

                    <section name="ordini" class="clearfix">

                        <!--LISTA DEGLI ORDINI DA MOSTRARE-->
                        <%for (i=0 ; i < utente.getOrdine().size() ; i++) {%>           
                            <article>
                                <div class="clearfix" id="content">
                                    <div style="float: left; width: 60%">
                                    
                                        <%for(j=0 ; j < utente.getOrdine().get(i).getContiene().size() ; j++){%>
                                            <div class="clearfix">
                                    
                                                <!--IMMAGINE DEL PRODOTTO-->
                                                <div style="float: left; margin-right: 1%;">
                                                    <img id="ProdImage" src="/images/<%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getImage()%>" width="200" height="200"/>
                                                </div>
                                                
                                                <!--INFORMAZIONI DEL PRODOTTO-->
                                                <div style="float: left; margin-left: 20%; line-height: 1.5;">
                                                    <p><b><%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getModello()%></b></br>Prezzo unitario: €<%=utente.getOrdine().get(i).getContiene().get(j).getProdotto().getPrezzo()%></br>Quantit&agrave;: <%=utente.getOrdine().get(i).getContiene().get(j).getQuantità()%></br>Taglia: <%=utente.getOrdine().get(i).getContiene().get(j).getTaglia().getTaglia()%></p>
                                                </div>
                                                
                                            </div>   
                                        <%}%>
                                    </div>
                                
                                    <!--INFORMAZIONI GENERALI DELL'ORDINE-->
                                    <div style="float: left; width: 40%; line-height: 1.5;">
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
                                                <select id="stato" name="stato" class="select" onchange="statoOrdine(<%=i%>)">
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
                    
                </div>

            </main>

            <div id="push"></div>

        </div>

        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

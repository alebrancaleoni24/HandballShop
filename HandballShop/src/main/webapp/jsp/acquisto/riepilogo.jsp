<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    
    String nazione = (String) request.getAttribute("nazione");
    String citta = (String) request.getAttribute("citta");
    String via = (String) request.getAttribute("via");
    String numeroCivico = (String) request.getAttribute("numeroCivico");
    String CAP = (String) request.getAttribute("CAP");
    String carta = (String) request.getAttribute("carta");
    double prezzo = (double) request.getAttribute("prezzo");
    
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numProd;
    if(prodotti == null){
        numProd = 0;
    }else{
        numProd = prodotti.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Riepilogo ordine";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        
        <script language="javascript">
            function procedi(){
                var f = document.paga;
                f.submit();
            }
            
            function goBack(){
                var f = document.annulla;
                f.submit();
            }
            
            function mainOnLoadHandler(){
                document.paga.submitButton.addEventListener("click", procedi);
                document.paga.backButton.addEventListener("click", goBack);
            }
        </script>
        <style>
            .indirizzo{
                float: left;
                width: 46%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: steelblue;
                padding: 10px 14px 10px 14px;
                margin: 0 17px 16px 0;
                box-shadow: 0 3px 2px #777;
            }
            
            .pagamento{
                float: left;
                width: 46%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: green;
                padding: 10px 14px 10px 14px;
                margin: 0 17px 16px 0;
                box-shadow: 0 3px 2px #777;
            }
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>
        
        <header>
            <%@include file="/include/headerUtente.inc" %>
        </header>
        
        <hr>
        
        <main>
            <div class='nome'>
                <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
            </div>
            
            <div style="margin-top: 15px; margin-bottom: 15px;">
                <h2>Step 2: Riepilogo pagamento</h2>
            </div>
            
            <div class="clearfix">
                
                <!--LATO SINISTRO CON LA CONFERMA DEI DATI PER L'ORDINE-->
                <div class="indirizzo">
                    <h3>Indirizzo di consegna</h3>
                    </br>
                    <address><%=via%> n. <%=numeroCivico%>, <%=citta%>, <%=nazione%></br>CAP: <%=CAP%></address>
                    
                    </br>
                    <h3>Dati carta</h3>
                    </br>
                    <p>Numero carta di credito: <%=carta%></p>
                </div>
            
                <!--LATO DESTRO CON LA LISTA DEI PRODOTTI INCLUSI NELL'ORDINE-->
                <div class="pagamento">
                    <h3>Lista prodotti inclusi nell'ordine</h3>
                    </br>
                    <%for( i=0 ; i<numProd ; i++ ){%>
                        <p><b><%=prodotti.get(i).getModello()%></b></br>Taglia: <%=carrello.get(i).getTaglia()%></br>Quantit&agrave;: <%=carrello.get(i).getQuantita()%></br>Prezzo unitario: €<%=prodotti.get(i).getPrezzo()%></br></br></p>
                    <%}%>
                
                    <p><b>Prezzo finale:</b> €<%=prezzo%></p>
                </div>
            
                <!--BOTTONI-->
                <div style="clear: both;">
                    <div class="left">
                        <form name="paga" action="Dispatcher" method="post">
                            <input type="hidden" name="controllerAction" value="Acquisto.paga"/>
                            <input type="hidden" name="carta" value="<%=carta%>"/>
                            <input type="hidden" name="nazione" value="<%=nazione%>"/>
                            <input type="hidden" name="citta" value="<%=citta%>"/>
                            <input type="hidden" name="via" value="<%=via%>"/>
                            <input type="hidden" name="numeroCivico" value="<%=numeroCivico%>"/>
                            <input type="hidden" name="CAP" value="<%=CAP%>"/>
                            <input type="hidden" name="prezzo" value="<%=prezzo%>"/>
                            <input type="button" name="submitButton" value="Paga" class="button" style="font-size: medium;">
                            <input type="button" name="backButton" value="Annulla" class="button" style="font-size: medium;">
                        </form>
                    </div>
                </div>
                
            </div>
                            
            <form name="annulla" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="Acquisto.ordina"/>
            </form>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

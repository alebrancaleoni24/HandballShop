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
    double iva = (double) request.getAttribute("iva");
    
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

            .pagamento{
                float: left;
                width: 48%;
                margin: 1%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: green;
                box-shadow: 0 3px 2px #777;
                padding: 1%;
            }

            .indirizzo{
                float: left;
                width: 43%;
                margin: 1%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: steelblue;
                box-shadow: 0 3px 2px #777;
                padding: 1%;
            }

            #content{
                margin-top: 2%;
                margin-bottom: 2%;
                margin-left: 1%;
                margin-right: 1%;
                padding-left: 1%;
                padding-right: 1%;
                padding-top: 2%;
                background-color: rgb(241, 244, 247);
                border-radius: 5px;
            }

            #paga{
                padding: 5px 10px;
                background-color: #228b22;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                margin-right: 2%;
            }

            #annulla{
                padding: 5px 10px;
                background-color: #dc143c;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
            }
            
        </style>
        
        <%@include file="/include/htmlHead.inc" %>
        
    </head>
    <body>

        <div id="page-container">
        
            <header>
                <%@include file="/include/headerUtente.inc" %>
            </header>
            
            <main>
                <div class='nome'>
                    <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                </div>
                
                <div style="clear: both"></div>

                <div class="clearfix" style="width: 70%; margin-left: 15%">

                    <!--LATO SINISTRO CON LA LISTA DEI PRODOTTI INCLUSI NELL'ORDINE-->
                    <div class="pagamento" style="line-height: 1.5;">
                        <h3>Prodotti inclusi nell'ordine</h3>
                        <%for( i=0 ; i<numProd ; i++ ){%>
                            <div class="clearfix" id="content">
                                <div style="float: left; margin-left: 1%;">
                                    <img src="/images/<%=prodotti.get(i).getImage()%>" width="140" height="140" alt="Visualizza prodotto"/>
                                    </form>
                                </div>
                                <div style="float: left; margin-left: 15%;">
                                    <p><b><%=prodotti.get(i).getModello()%></b></br>Taglia: <%=carrello.get(i).getTaglia()%></br>Quantit&agrave;: <%=carrello.get(i).getQuantita()%></br>Prezzo unitario: €<%=prodotti.get(i).getPrezzo()%></br></br></p>
                                </div>
                            </div>
                        <%}%>
                    
                        <p><b>Prezzo finale:</b> €<%=prezzo%></p>
                        <p><b>di cui IVA:</b> €<%=iva%></p>
                    </div>

                    <!--LATO DESTRO CON LA CONFERMA DEI DATI PER L'ORDINE-->
                    <div class="indirizzo" style="line-height: 1.5;">
                        <h3>Indirizzo di consegna</h3>
                        <address><%=via%> n. <%=numeroCivico%>, <%=citta%>, <%=nazione%></br>CAP: <%=CAP%></address>
                        <br>
                        <h3>Dati carta</h3>
                        <p>Numero carta di credito: <%=carta%></p>

                        <!--BOTTONI-->
                        <div style="clear: both;">
                            <div class="left" style="margin-top: 5%;">
                                <form name="paga" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="Acquisto.paga"/>
                                    <input type="hidden" name="carta" value="<%=carta%>"/>
                                    <input type="hidden" name="nazione" value="<%=nazione%>"/>
                                    <input type="hidden" name="citta" value="<%=citta%>"/>
                                    <input type="hidden" name="via" value="<%=via%>"/>
                                    <input type="hidden" name="numeroCivico" value="<%=numeroCivico%>"/>
                                    <input type="hidden" name="CAP" value="<%=CAP%>"/>
                                    <input type="hidden" name="prezzo" value="<%=prezzo%>"/>
                                    <input type="button" name="submitButton" value="Paga" id="paga" style="font-size: medium;">
                                    <input type="button" name="backButton" value="Annulla" id="annulla" style="font-size: medium;">
                                </form>
                            </div>
                        </div>
                    </div>
                    
                </div>
                                
                <form name="annulla" action="Dispatcher" method="post">
                    <input type="hidden" name="controllerAction" value="Acquisto.ordina"/>
                </form>
                
            </main>

            <div id="push"></div>

        </div>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

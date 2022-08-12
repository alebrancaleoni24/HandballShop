<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0, j = 1;

    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");
    int numProd;
    if (prodotti == null) {
        numProd = 0;
    } else {
        numProd = prodotti.size();
    }

    double prezzo = (double) request.getAttribute("prezzo");

    double iva = (double) request.getAttribute("iva");

    ArrayList<Boolean> disponibilita = (ArrayList<Boolean>) request.getAttribute("disponibilita");

    boolean loggedOn = (boolean) request.getAttribute("loggedOn");

    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Carrello";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function prodottoFormSubmit(index, codice) {
                var f = document.forms["prodottoForm" + index];
                f.codiceProdotto.value = codice;
                f.submit();
                return;
            }

            function quantitaProdotto(index) {
                var f = document.forms["quantitaProdotto" + index];
                f.submit();
                return;
            }
        </script>
        <style>
            #quantita{
                height: 30px;
                font-size: medium;
                border-radius: 5px;
                background-color: white;
            }

            #ordina{
                padding: 5px 10px;
                background-color: #228b22;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                width: 100%;
            }

            #cancella{
                padding: 5px 10px;
                background-color: #dc143c;
                color: #ffffff;
                border: 1px solid #000000;
                border-radius: 8px;
                cursor: pointer;
                font-size: large;
                width: 100%;
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

            #formdx{
                float: left;
                width: 17%;
                margin-right: 7%;
                margin-top: 2%;
                text-align: center;
            }
        </style>

        <%@include file="/include/htmlHead.inc" %>

    </head>
    <body>
        <header>
            <%@include file="/include/headerUtente.inc" %>
        </header>

        <main>
            <div class="nome" style="margin-bottom: 15px;">
                <p id="loggedOn">Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
            </div>

            <%if (numProd > 0) {%>
            <!--LISTA PRODOTTI DEL CARRELLO DA VISUALIZZARE-->
            <section>
                <div style="clear: both;"></div>
                    <div style="float: left; width: 65%; margin-left: 7%; margin-right: 4%;">
                        <%for (i = 0; i < numProd; i++) {%>           
                        <article>
                            <div class="clearfix" id="content">

                                <!--IMMAGINE E INFORMAZIONI FLOTTANTI SULLA SINISTRA-->
                                <div style="float: left; margin-right: 1%;">
                                    <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="controllerAction" value="Catalogo.viewProd"/>
                                        <input type="hidden" name="codiceProdotto"/>
                                        <a href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                            <img src="/images/<%=prodotti.get(i).getImage()%>" width="200" height="200" alt="Visualizza prodotto"/>
                                        </a>
                                    </form>
                                </div>
                                <div style="float: left; width: 20%; margin-left: 3%; line-height: 1.5;">
                                    <span><b><%=prodotti.get(i).getMarca()%></b></span>
                                    <br/>
                                    <span><%=prodotti.get(i).getModello()%></span>
                                    <br/>
                                    <%if (disponibilita.get(i)) {%>
                                    <span style='color: green'>Disponibile</span>
                                    <%} else {%>
                                    <span style='color: red'>Non disponibile</span>
                                    <%}%>
                                    <br/>
                                    <span>Taglia: <%=carrello.get(i).getTaglia()%></span>
                                </div>

                                <!--FORMS-->
                                <div style="float: left; width: 40%; text-align: center;">

                                    <!--FORM PER IL CAMBIO DELLA QUANTITA'-->
                                    <form name="quantitaProdotto<%=i%>" action="Dispatcher" method="post">
                                        <label for="quantita">Quantit&agrave;: </label> 
                                        <select id="quantita" name="quantita" onchange="quantitaProdotto(<%=i%>)"> 
                                            <% for (j = 1; j < 30; j++) {%>
                                            <option value="<%=j%>" <% if (j == carrello.get(i).getQuantita()) {%>selected="selected"<%}%>><%=j%></option>
                                            <%}%>
                                        </select>
                                        <input type="hidden" name="codiceProdotto" value="<%=carrello.get(i).getCodiceProd()%>"/>
                                        <input type="hidden" name="taglia" value="<%=carrello.get(i).getTaglia()%>"/>
                                        <input type="hidden" name="controllerAction" value="Acquisto.cambiaQuantita"/>
                                    </form>

                                    </br>

                                    <!--FORM PER LA RIMOZIONE DAL CARRELLO-->
                                    <form name="rimuoviProdotto<%=i%>" action="Dispatcher" method="post">
                                        <input type="hidden" name="codiceProdotto" value="<%=carrello.get(i).getCodiceProd()%>"/>
                                        <input type="hidden" name="taglia" value="<%=carrello.get(i).getTaglia()%>"/>
                                        <input type="hidden" name="controllerAction" value="Acquisto.rimuovi"/>
                                        <input type="submit" value="Rimuovi" class="button">
                                    </form>
                                </div>

                                <!--PREZZO DELL'ARTICOLO-->
                                <div style="float: left; text-align: right; width: 15%; padding-right: 15px;">
                                    <p><b>€<%=prodotti.get(i).getPrezzo()%></b></p>       
                                </div>        
                            </div>
                        </article>
                        <%}%>
                    </div>
            

                    <!--FORM ORDINA E CANCELLA FLOTTANTI SULLA DESTRA-->
                <div id="formdx">
                    <h2>Dettagli ordine</h2>
                    </br>
                    <p style="font-size: large;">Totale: €<%=prezzo%></p>
                    </br>
                    <p style="font-size: large;">di cui IVA: €<%=iva%></p>
                    </br>
                    <!--FORM PER ORDINARE-->
                    <form name="ordina" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="Acquisto.viewPagamento"/>
                        <input type="submit" value="Ordina" id="ordina">
                    </form>
                    </br>
                    <!--FORM PER CANCELLARE IL CARRELLO-->
                    <form name="cancella" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="Acquisto.cancella"/>
                        <input type="submit" value="Cancella" id="cancella">
                    </form>
                </div>
            </section>
            <%} else {%>
            <div style="clear: both;"></div>
            <div style='height: 479px; margin: 2%; line-height: 2;'>
                <h2>Carrello vuoto</h2>
                <p>Il tuo carrello &eacute; vuoto. Per aggiungere articoli al tuo carrello naviga su <a href="Dispatcher?controllerAction=Catalogo.view">Handball Shop</a> e, quando trovi un articolo che ti interessa, clicca sul "Aggiungi al carrello"</p>
            </div>
            <%}%>

            <div style="clear: both; height: 20px;"></div>

        </main>

        <%@include file="/include/footer.inc" %>

    </body>
</html>
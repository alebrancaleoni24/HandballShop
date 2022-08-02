<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
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
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Magazzino";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function prodottoFormSubmit(index, codice){
                var f = document.forms["prodottoForm" + index];
                f.codiceProdotto.value = codice;
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
            
            <!--FORM PER PASSARE ALLA PAGINA DI INSERIMENTO DI UN NUOVO PRODOTTO-->
            <form name="inserisciProdotto" action="Dispatcher" method="post">
                <input type="hidden" name="controllerAction" value="ProdottoManagement.inserisciProdottoView"/>
                <input type="submit" name="inserisciProdotto" value="Nuovo prodotto" class="mainButton">
            </form>
            
            <!--LISTA PRODOTTI NEL MAGAZZINO-->
            <section class="clearfix" id="boxMagazzino">
                <%for(i=0 ; i<numProd ; i++){%>           
                    <article>
                        
                        <div>
                            <!--IMMAGINE SULLA SINISTRA-->
                            <div style="float: left; margin-right: 50px;">
                                <form name="prodottoForm<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="ProdottoManagement.modificaProdottoView"/>
                                    <input type="hidden" name="codiceProdotto"/>
                                    <a href="javascript:prodottoFormSubmit(<%=i%>, <%=prodotti.get(i).getCodiceProdotto()%>);">
                                        <img id="ProdImage" src="/images/<%=prodotti.get(i).getImage()%>" width="170" height="170" alt="Visualizza prodotto"/>
                                    </a>
                                </form>
                            </div>
                                    
                            <!--INFORMAZIONI E FORM DEL PRODOTTO-->
                            <div style="float: left; margin-top: 2%;">
                                <!--INFORMAZIONI PRODOTTO-->
                                <div>
                                    <p><b>Modello:</b> <%=prodotti.get(i).getModello()%></p>
                                    <p><b>Categoria:</b> <%=prodotti.get(i).getCategoria()%></p>
                                    <p><b>Marca:</b> <%=prodotti.get(i).getMarca()%></p>
                                    <p><b>Prezzo:</b> €<%=prodotti.get(i).getPrezzo()%></p>
                                    <p><b>Genere:</b> <%=prodotti.get(i).getGenere()%></p>
                                </div>
                                
                                <!--FORM DEL PRODOTTO-->
                                <div>
                                    <div style="float: left; margin-right: 10px;">
                                        <!--FORM PER LA GESTIONE DELLA DISPONIBILITA' DI MAGAZZINO-->
                                        <form name="disponibilita<%=i%>" action="Dispatcher" method="post">
                                            <input type="hidden" name="codiceProdotto" value="<%=prodotti.get(i).getCodiceProdotto()%>"/>
                                            <input type="hidden" name="controllerAction" value="ProdottoManagement.disponibilitaView"/>
                                            <input type="submit" value="Modifica giacenza" class="button">
                                        </form>
                                    </div>
                                    
                                    <div style="float: left;">
                                        <!--FORM PER IL BLOCCO/SBLOCCO DEL PRODOTTO-->
                                        <%if(prodotti.get(i).isBlocked()){%>
                                            <form name="sbloccaProdotto<%=i%>" action="Dispatcher" method="post">
                                                <input type="hidden" name="codiceProdotto" value="<%=prodotti.get(i).getCodiceProdotto()%>"/>
                                                <input type="hidden" name="controllerAction" value="ProdottoManagement.sbloccaProdotto"/>
                                                <input type="submit" value="Sblocca" class="button">
                                            </form>
                                        <%}else{%>
                                            <form name="bloccaProdotto<%=i%>" action="Dispatcher" method="post">
                                                <input type="hidden" name="codiceProdotto" value="<%=prodotti.get(i).getCodiceProdotto()%>"/>
                                                <input type="hidden" name="controllerAction" value="ProdottoManagement.bloccaProdotto"/>
                                                <input type="submit" value="Blocca" class="button">
                                            </form>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                               
                    </article>
                <%}%>
            </section>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
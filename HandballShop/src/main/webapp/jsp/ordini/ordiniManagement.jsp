<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Ordine"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0, j=0;
    
    ArrayList<Ordine> ordini = (ArrayList<Ordine>) request.getAttribute("ordini");
    int numOrdini;
    if(ordini == null){
        numOrdini = 0;
    }else{
        numOrdini = ordini.size();
    }
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Ordini";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function statoOrdine(index){
                var f = document.forms["aggiornaStato" + index];
                f.submit();
                return;
            }
        </script>
        <style>
            #stato{
                width: 30%;
                height: 22px;
                font-size: medium;
            }
        </style>
        
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
            
            <!--LISTA DEGLI ORDINI DA VISUALIZZARE-->
            <%for(i=0 ; i<ordini.size() ; i++){%>
            
                <article>
                    <hr>
                    <div class="clearfix" style="margin-top: 15px; margin-bottom: 15px;">
                        <div style="float: left; width: 60%">
                        
                            <%for(j=0 ; j<ordini.get(i).getContiene().size() ; j++){%>
                                <div class="clearfix">
                        
                                    <!--IMMAGINE DEL PRODOTTO-->
                                    <div style="float: left; padding-left: 2%; padding-right: 2%;">
                                        <img id="ProdImage" src="/images/<%=ordini.get(i).getContiene().get(j).getProdotto().getImage()%>" width="186" height="186"/>
                                    </div>
                                    
                                    <!--INFORMAZIONI DEL PRODOTTO-->
                                    <div style="float: left; padding-left: 2%; padding-right: 2%; padding-top: 50px;">
                                        <p><b><%=ordini.get(i).getContiene().get(j).getProdotto().getModello()%></b></br>Prezzo unitario: €<%=ordini.get(i).getContiene().get(j).getProdotto().getPrezzo()%></br>Quantit&agrave;: <%=ordini.get(i).getContiene().get(j).getQuantità()%></br>Taglia: <%=ordini.get(i).getContiene().get(j).getTaglia().getTaglia()%></p>
                                    </div>
                                    
                                </div>   
                            <%}%>
                        </div>
                    
                        <!--INFORMAZIONI GENERALI DELL'ORDINE-->
                        <div style="float: left; width: 40%">
                            <h3>Dettagli ordine</h3><br>
                            <p><b>Cliente:</b> <%=ordini.get(i).getUtente().getNome()%> <%=ordini.get(i).getUtente().getCognome()%></p>
                            <p><b>Data ordine: </b><%=ordini.get(i).getDataOrdineString()%></br><b>Data consegna: </b><%=ordini.get(i).getDataConsegnaString()%></br><b>Indirizzo di consegna: </b><%=ordini.get(i).getVia()%> n° <%=ordini.get(i).getNumeroCivico()%>, <%=ordini.get(i).getCittà()%>, <%=ordini.get(i).getNazione()%></br><b>Importo:</b> €<%=ordini.get(i).getPagamento().getImporto()%></br></p>
                            <%if(ordini.get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                <p style="color: green">Consegnato</p>
                            <%}else{%>
                                <p style="color: orangered"><%=ordini.get(i).getStato()%></p>
                            <%}%>
                        
                            <!--FORM PER LA MODIFICA DELLO STATO DELL'ORDINE-->
                            <%if(!ordini.get(i).getStato().equalsIgnoreCase("consegnato")){%>
                                <form name="aggiornaStato<%=i%>" action="Dispatcher" method="post">
                                    <input type="hidden" name="controllerAction" value="Ordini.aggiornaStato"/>
                                    <input type="hidden" name="codiceOrdine" value="<%=ordini.get(i).getCodiceOrdine()%>"/>
                                    <label for="stato">Stato: </label>
                                    <select id="stato" name="stato" onchange="statoOrdine(<%=i%>)">
                                        <option value="In preparazione" <%if(ordini.get(i).getStato().equalsIgnoreCase("In preparazione")){%>selected="selected"<%}%>>In preparazione</option>
                                        <option value="In viaggio" <%if(ordini.get(i).getStato().equalsIgnoreCase("In viaggio")){%>selected="selected"<%}%>>In viaggio</option>
                                        <option value="Consegnato" <%if(ordini.get(i).getStato().equalsIgnoreCase("Consegnato")){%>selected="selected"<%}%>>Consegnato</option>
                                    </select>
                                 </form>
                            <%}%>
                        </div>
                    </div>   
                </article>
                    
            <%}%>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>

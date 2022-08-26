<%@page session = "false"%>
<%@page import="isa.handballshop.model.valueObject.Taglia"%>
<%@page import="isa.handballshop.model.valueObject.Prodotto"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i=0;
    
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    ArrayList<Taglia> taglie = (ArrayList<Taglia>) request.getAttribute("taglie");
    int numTaglie;
    if(taglie == null){
        numTaglie = 0;
    }else{
        numTaglie = taglie.size();
    }
    
    Long codiceProdotto = (Long) request.getAttribute("codiceProdotto");
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Gestisci taglie";
    
    String action=(taglie != null) ? "modify" : "insert";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            var action = "<%=action%>";
            
            function validateAndSubmit(){
                
                var xs = document.taglieForm.xs.value;
                var s = document.taglieForm.s.value;
                var m = document.taglieForm.m.value;
                var l = document.taglieForm.l.value;
                var xl = document.taglieForm.xl.value;
                var xxl = document.taglieForm.xxl.value;
                var xxxl = document.taglieForm.xxxl.value;
                
                /*CONTROLLO IL CAMPO XS*/
                if ((xs == "") || (xs == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.xs.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO S*/
                if ((s == "") || (s == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.s.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO M*/
                if ((m == "") || (m == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.m.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO L*/
                if ((l == "") || (l == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.l.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO XL*/
                if ((xl == "") || (xl == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.xl.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO XXL*/
                if ((xxl == "") || (xxl == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.xxl.focus();
                    return false;
                }
                
                /*CONTROLLO IL CAMPO XXXL*/
                if ((xxxl == "") || (xxxl == "undefined")) {
                    alert("Tutti i campi devono essere compilati");
                    document.taglieForm.xxxl.focus();
                    return false;
                }
                
                else{
                    if(action === "insert"){
                        document.taglieForm.controllerAction.value = "ProdottoManagement.inserisciTaglia";
                    }else{
                        document.taglieForm.controllerAction.value = "ProdottoManagement.modificaDisponibilita";
                    }
                    document.taglieForm.submit();
                }
            }
            
            function goBack(){
                if(action == "insert"){
                    document.backForm.controllerAction.value = "ProdottoManagement.inserisciProdottoView";
                    document.backForm.submit();
                }else{
                    document.backForm.controllerAction.value = "ProdottoManagement.view";
                    document.backForm.submit();
                }
            }

            function mainOnLoadHandler(){
                document.taglieForm.backButton.addEventListener("click", goBack);
                document.taglieForm.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 70%; 
                margin-left: 15%;
            }
            
            table{
                width: 100%;
            }

            #sx{
                width: 40%;
                padding-bottom: 1%;
                padding: 1%;
                font-weight: bold;
            }

            #dx{
                width: 60%;
                padding-bottom: 1%;
                padding: 1%;
            }

            .logT{
                font-size: large;
                border-radius: 5px;
                border: 1px solid #000000;
                width: 100%;
                background-color: white;
            }
        </style>
        
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
            
                    <div class="content">
                        <div style="margin-top: 1%; margin-bottom: 3%;">
                            <h1>Gestione disponibilit&agrave; di magazzino</h1>
                        </div>
                        
                        <!--FORM PER GESTIRE LA DISPONIBILITA' DI MAGAZZINO-->
                        <form id="taglieForm" name="taglieForm" action="Dispatcher" method="post">

                            <div style="width: 60%; margin-left: 20%; line-height: 1.5; font-size: large;">
                                <table>
                                    <tr>
                                        <td id="sx"><label for="xs"><%if(!prodotto.getCategoria().equals("Scarpe")){%>XS <%}else{%>39 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="xs" <% if (action.equals("modify")){%> value="<%=taglie.get(0).getQuantità()%>" <%}%> required size="20"/> </td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="s"><%if(!prodotto.getCategoria().equals("Scarpe")){%>S <%}else{%>40 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="s" <% if (action.equals("modify")){%> value="<%=taglie.get(1).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="m"><%if(!prodotto.getCategoria().equals("Scarpe")){%>M <%}else{%>41 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="m" <% if (action.equals("modify")){%> value="<%=taglie.get(2).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="l"><%if(!prodotto.getCategoria().equals("Scarpe")){%>L <%}else{%>42 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="l" <% if (action.equals("modify")){%> value="<%=taglie.get(3).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="xl"><%if(!prodotto.getCategoria().equals("Scarpe")){%>XL <%}else{%>43 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="xl" <% if (action.equals("modify")){%> value="<%=taglie.get(4).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="xxl"><%if(!prodotto.getCategoria().equals("Scarpe")){%>XXL <%}else{%>44 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="xxl" <% if (action.equals("modify")){%> value="<%=taglie.get(5).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                    <tr>
                                        <td id="sx"><label for="xxxl"><%if(!prodotto.getCategoria().equals("Scarpe")){%>XXXL <%}else{%>45 <%}%></label></td>
                                        <td id="dx"><input type="text" class="logT" name="xxxl" <% if (action.equals("modify")){%> value="<%=taglie.get(6).getQuantità()%>" <%}%> required size="20"/></td>
                                    </tr>
                                </table>
                            </div>
                        
                            <input type="hidden" name="controllerAction"/>
                            <%if(action.equals("insert")){%>
                                <input type="hidden" name="categoria" value="<%=prodotto.getCategoria()%>"/>
                                <input type="hidden" name="marca" value="<%=prodotto.getMarca()%>"/>
                                <input type="hidden" name="modello" value="<%=prodotto.getModello()%>"/>
                                <input type="hidden" name="genere" value="<%=prodotto.getGenere()%>"/>
                                <input type="hidden" name="immagine" value="<%=prodotto.getImage()%>"/>
                                <input type="hidden" name="descrizione" value="<%=prodotto.getDescrizione()%>"/>
                                <input type="hidden" name="prezzo" value="<%=prodotto.getPrezzo()%>"/>
                                <input type="hidden" name="blocked" value="<%=(prodotto.isBlocked()) ? "S" : "N"%>"/>
                                <input type="hidden" name="push" value="<%=(prodotto.isPush()) ? "S" : "N"%>"/>
                            <%}else{%>
                                <input type="hidden" name="codiceProdotto" value="<%=codiceProdotto%>"/>
                            <%}%>
                        
                            <!--BOTTONI PER LA CONFERMA O PER ANNULLARE-->
                            <div style="margin-top: 2%;">
                                <input type="button" name="submitButton" value="Ok" class="button">
                                <input type="button" name="backButton" value="Annulla" class="button">
                            </div>
                        
                        </form>

                        <!--FORM PER ANNULLARE-->
                        <form name="backForm" method="post" action="Dispatcher"> 
                            <input type="hidden" name="controllerAction"/>
                        </form>
                        
                    </div>
                </div>

            </main>

            <div id="push"></div>

        </div>

        <%@include file="/include/footer.inc" %>

    </body>
</html>
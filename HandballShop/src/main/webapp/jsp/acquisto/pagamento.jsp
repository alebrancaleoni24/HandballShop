<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    ArrayList<Carrello> carrello = (ArrayList<Carrello>) request.getAttribute("carrello");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");
    
    String menuActiveLink = "Modulo ordine";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>
        
        <script language="javascript">
            function procedi(){
                var campi = true;
                var f = document.pagamentoForm;
                
                var carta = f.carta.value;
                var nazione = f.nazione.value;
                var citta = f.citta.value;
                var via = f.via.value;
                var numeroCivico = f.numeroCivico.value;
                var CAP = f.CAP.value;
                    
                /*CONTROLLO IL CAMPO CARTA*/
                if ((carta == "") || (carta == "undefined")) {
                    alert("Il campo Carta è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO NAZIONE*/
                if ((nazione == "") || (nazione == "undefined")) {
                    alert("Il campo Nazione è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO CITTA*/
                if ((citta == "") || (citta == "undefined")) {
                    alert("Il campo Citta è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO VIA*/
                if ((via == "") || (via == "undefined")) {
                    alert("Il campo Via è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO NUMERO CIVICO*/
                if ((numeroCivico == "") || (numeroCivico == "undefined")) {
                    alert("Il campo Numero Civico è obbligatorio");
                    campi = false;
                }
                
                /*CONTROLLO IL CAMPO CAP*/
                if ((CAP == "") || (CAP == "undefined")) {
                    alert("Il campo CAP è obbligatorio");
                    campi = false;
                }
                
                if(campi){
                    f.controllerAction.value = "Acquisto.viewRiepilogo";
                    f.submit();
                }
            }
            
            function goBack(){
                var f = document.backForm;
                f.controllerAction.value = "Acquisto.viewCarrello";
                f.submit();
            }

            function mainOnLoadHandler(){
                document.pagamentoForm.backButton.addEventListener("click", goBack);
                document.pagamentoForm.submitButton.addEventListener("click", procedi);
            }
        </script>
        <style>
            .indirizzo{
                width: 46%;
                margin-left: 27%;
                margin-top: 2%;
                margin-bottom: 2%;
                border-width: 1px;
                border-style: solid;
                border-radius: 10px;
                border-color: steelblue;
                padding: 10px 14px 10px 14px;
                box-shadow: 0 3px 2px #777;
            }

            table{
                width: 100%;
                font-size: large;
            }

            #sx{
                width: 32%;
                padding-bottom: 1%;
                padding-left: 5%;
            }

            #dx{
                width: 68%;
                padding-bottom: 1%;
                padding-right: 5%;
            }
            
            #clausola{
                font-size: smaller;
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
                
                <!--FORM PER L'INSERIMENTO DEI DATI PER L'ORDINE-->
                <div class="clearfix">
                    <form name="pagamentoForm" action="Dispatcher" method="post">
                        
                        <!--LATO DI SINISTRA DEDICATO ALL'INDIRIZZO DI CONSEGNA-->
                        <div class="indirizzo">
                            <h2>Informazioni di consegna</h2>
                            </br>
                            
                            <table>
                                <tr>
                                    <td id="sx"><label for="nazione">Nazione </label></td>
                                    <td id="dx"><input type="text" id="nazione" name="nazione" value="" maxlength="20" required placeholder="Italia" class="log"/></td>
                                </tr>
                    
                                <tr>
                                    <td id="sx"><label for="citta">Citt&agrave; </label></td>
                                    <td id="dx"><input type="text" id="citta" name="citta" value="" maxlength="20" required placeholder="Roma" class="log"/></td>
                                </tr>
                        
                                <tr>
                                    <td id="sx"><label for="via">Via </label></td>
                                    <td id="dx"><input type="text" id="via" name="via" value="" maxlength="20" required placeholder="Via Garibaldi" class="log"/></td>
                                </tr>
                        
                                <tr>
                                    <td id="sx"><label for="numeroCivico">Numero civico </label></td>
                                    <td id="dx"><input type="text" id="numeroCivico" name="numeroCivico" value="" maxlength="10" required placeholder="1" class="log"/></td>
                                </tr>
                            
                                <tr>
                                    <td id="sx"><label for="CAP">CAP </label></td>
                                    <td id="dx"><input type="text" id="CAP" name="CAP" value="" maxlength="5" required placeholder="12345" class="log"/></td>
                                </tr>

                                <tr>
                                    <td id="sx"><label for="carta">Numero carta </label></td>
                                    <td id="dx"><input type="text" id="carta" name="carta" value="" maxlength="16" required class="log"/></td>
                                </tr>

                            </table>
                    
                            <input type="hidden" name="controllerAction"/>

                            <div style="margin-top: 10px;">
                                <input type="button" name="submitButton" value="Procedi" class="button">
                                <input type="button" name="backButton" value="Annulla" class="button">
                            </div>

                            <div style="margin-top: 10px; text-align: right;">
                                <p id="clausola">Tutti i campi sono obbligatori</p>
                            </div>
                        </div>
                        
                    </form>
                    
                </div>
                    
                <!--FORM DI ANNULLA => TORNO NELLA VISTA DEL CARRELLO-->
                <form name="backForm" method="post" action="Dispatcher"> 
                    <input type="hidden" name="controllerAction"/>
                </form>
                
            </main>

            <div id="push"></div>

        </div>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
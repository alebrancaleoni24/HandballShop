<%@page session = "false"%>
<%@page import="java.util.ArrayList"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page import="isa.handballshop.model.session.valueObject.Carrello"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");

    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");

    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Logging";
%>
<!DOCTYPE html>
<html lang="it-IT">
    <head>

        <script language="javascript">
            var loggedOn = <%=loggedOn%>;

            function validateAndSubmit() {
                var nome = document.registerForm.nome.value;
                var cognome = document.registerForm.cognome.value;
                var email = document.registerForm.email.value;
                var password = document.registerForm.password.value;
                var conferma = document.registerForm.passwordConf.value;
                var nazione = document.registerForm.nazione.value;
                var citta = document.registerForm.citta.value;
                var via = document.registerForm.via.value;
                var numeroCivico = document.registerForm.numeroCivico.value;
                var CAP = document.registerForm.CAP.value;

                /*CONTROLLO IL CAMPO NOME*/
                if ((nome == "") || (nome == "undefined")) {
                    alert("Il campo Nome è obbligatorio");
                    document.registerForm.nome.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO COGNOME*/
                else if ((cognome == "") || (cognome == "undefined")) {
                    alert("Il campo Cognome è obbligatorio");
                    document.registerForm.cognome.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO EMAIL*/
                else if ((email == "") || (email == "undefined")) {
                    alert("Il campo E-Mail è obbligatorio");
                    document.registerForm.email.focus();
                    return false;
                } else if (!checkEmail(email)) {
                    alert("Il formato dell'email inserita non è valido");
                    document.registerForm.email.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO PASSWORD*/
                else if ((password == "") || (password == "undefined")) {
                    alert("Il campo Password è obbligatorio");
                    document.registerForm.password.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CONFERMA PASSWORD*/
                else if ((conferma == "") || (conferma == "undefined")) {
                    alert("Il campo Conferma password è obbligatorio");
                    document.registerForm.conferma.focus();
                    return false;
                }

                /*CONTROLLO L'UGUAGLIANZA TRA PASSWORD E CONFERMA PASSWORD*/
                else if (password != conferma) {
                    alert("La password confermata è diversa da quella scelta");
                    document.registerForm.conferma.value = "";
                    document.registerForm.conferma.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO NAZIONE*/
                else if ((nazione == "") || (nazione == "undefined")) {
                    alert("Il campo Nazione è obbligatorio");
                    document.registerForm.nazione.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CITTA*/
                else if ((citta == "") || (citta == "undefined")) {
                    alert("Il campo Citta è obbligatorio");
                    document.registerForm.citta.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO VIA*/
                else if ((via == "") || (via == "undefined")) {
                    alert("Il campo Via è obbligatorio");
                    document.registerForm.via.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO NUMERO CIVICO*/
                else if ((numeroCivico == "") || (numeroCivico == "undefined")) {
                    alert("Il campo Numero civico è obbligatorio");
                    document.registerForm.numeroCivico.focus();
                    return false;
                }

                /*CONTROLLO IL CAMPO CAP*/
                else if ((CAP == "") || (CAP == "undefined")) {
                    alert("Il campo CAP è obbligatorio");
                    document.registerForm.CAP.focus();
                    return false;
                }

                /*INVIO IL MODULO*/
                else {
                    document.registerForm.controllerAction.value = "LogOn.registraAdmin";
                    document.registerForm.submit();
                }
            }

            function validaEmail(email) {
                var regexp = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return regexp.test(email);
            }

            function checkEmail(email) {
                if (validaEmail(email)) {
                    return true;
                } else {
                    return false;
                }
            }

            function goBack() {
                var f = document.backForm;
                f.controllerAction.value = "HomeAdmin.view";
                f.submit();
            }

            function mainOnLoadHandler() {
                document.registerForm.backButton.addEventListener("click", goBack);
                document.registerForm.submitButton.addEventListener("click", validateAndSubmit);
            }
        </script>
        <style>
            .content{
                width: 36%; 
                margin-left: 32%;
                margin-top: 1%;
                margin-bottom: 1%;
            }

            table{
                width: 100%;
            }

            #sx{
                width: 38%;
                padding-bottom: 1%;
            }

            #dx{
                width: 62%;
                padding-bottom: 1%;
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
                <%@include file="/include/headerAdmin.inc"%>
            </header>

            <main>
                <div class='nome'>
                    <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
                </div>

                <div style="clear: both"></div>
                
                <div class="content">

                    <!--FORM DI REGISTRAZIONE O DI LOGIN-->
                    <form name="registerForm" action="Dispatcher" method="post">

                        <table>
                            <tr>
                                <td id="sx"><label for="nome">Nome: </label></td>
                                <td id="dx"><input type="text" id="nome" name="nome" value="" maxlength="20" required placeholder="Mario" class="log"/></td>
                            </tr>

                            <tr>
                                <td id="sx"><label for="cognome">Cognome: </label></td>
                                <td id="dx"><input type="text" id="cognome" name="cognome" value="" maxlength="20" required placeholder="Rossi" class="log"/></td>
                            </tr>                    
                        
                            <tr>
                                <td id="sx"><label for="email">E-Mail: </label></td>
                                <td id="dx"><input type="email" id="email" name="email" autocomplete="username email" value="" maxlength="50" required placeholder="mario.rossi@gmail.com" class="log"/></td>
                            </tr>
                     
                            <tr>
                                <td id="sx"><label for="password">Password: </label></td>
                                <td id="dx"><input type="password" id="password" name="password" autocomplete="new-password" maxlength="50" required class="log"/></td>
                            </tr>
                    
                            <tr>
                                <td id="sx"><label for="passwordConf">Conferma Password: </label></td>
                                <td id="dx"><input type="password" id="passwordConf" name="passwordConf" autocomplete="new-password" maxlength="50" required class="log"/></td>
                            </tr>
                    
                            <tr>
                                <td id="sx"><label>Sesso: </label></td>
                                <td id="dx">
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="genere" value="M" checked> Maschio 
                                    </div>
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="genere" value="F"> Femmina
                                    </div>
                                </td>
                            </tr>
                
                            <tr>
                                <td id="sx"><label for="nazione">Nazione: </label></td>
                                <td id="dx"><input type="text" id="nazione" name="nazione" value="" maxlength="20" required placeholder="Italia" class="log"/></td>
                            </tr>
                
                            <tr>
                                <td id="sx"><label for="citta">Citt&agrave;: </label></td>
                                <td id="dx"><input type="text" id="citta" name="citta" value="" maxlength="20" required placeholder="Roma" class="log"/></td>
                            </tr>
                
                            <tr>
                                <td id="sx"><label for="via">Via: </label></td>
                                <td id="dx"><input type="text" id="via" name="via" value="" maxlength="20" required placeholder="Via Garibaldi" class="log"/></td>
                            </tr>
                    
                            <tr>
                                <td id="sx"><label for="numeroCivico">Numero civico: </label></td>
                                <td id="dx"><input type="text" id="numeroCivico" name="numeroCivico" value="" maxlength="10" required placeholder="1" class="log"/></td>
                            </tr>
                    
                            <tr>
                                <td id="sx"><label for="CAP">CAP: </label></td>
                                <td id="dx"><input type="text" id="CAP" name="CAP" value="" maxlength="5" required placeholder="12345" class="log"/></td>
                            </tr>

                            <tr>
                                <td id="sx"><label>Admin: </label></td>
                                <td id="dx">
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="admin" value="S" checked> S&iacute 
                                    </div>
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="admin" value="N"> No
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td id="sx"><label>Bloccato: </label></td>
                                <td id="dx">
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="blocked" value="S" checked> S&iacute 
                                    </div>
                                    <div style="float: left; width: 50%">
                                        <input type="radio" name="blocked" value="N"> No
                                    </div>
                                </td>
                            </tr>
                          
                        </table>

                        <input type="hidden" name="controllerAction"/>

                        <div style="clear: both"></div>

                        <div style="margin-top: 10px;">
                            <input type="button" name="submitButton" value="Ok" class="button">
                            <input type="button" name="backButton" value="Annulla" class="button">
                        </div>

                    </form>

                    <div style="margin-top: 20px;">
                        <p id="clausola">Tutti i campi sono obbligatori</p>
                    </div>
                </div>

                <!--FORM DI ANNULLA => TORNO NELLA HOME DEGLI UTENTI O DELL'ADMIN-->
                <form name="backForm" method="post" action="Dispatcher"> 
                    <input type="hidden" name="controllerAction"/>
                </form>

            </main>

            <div id="push"></div>

        </div>

        <%@include file="/include/footer.inc" %>

    </body>
</html>
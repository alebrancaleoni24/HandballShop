<%@page session = "false"%>
<%@page import="isa.handballshop.model.session.valueObject.UtenteLoggato"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int i = 0;
    
    boolean loggedOn = (boolean) request.getAttribute("loggedOn");
    
    /*Carico i cookie*/
    UtenteLoggato ul = (UtenteLoggato) request.getAttribute("loggedUser");
    
    String applicationMessage = (String) request.getAttribute("applicationMessage");

    String menuActiveLink = "Home amministratore";
%>

<!DOCTYPE html>
<html lang="it-IT">
    <head>
        <script language="javascript">
            function utentiManagementSubmit(){
                var f = document.utentiManagement;
                f.submit();
            }
            
            function ordiniSubmit(){
                var f = document.ordini;
                f.submit();
            }
            
            function magazzinoSubmit(){
                var f = document.magazzino;
                f.submit();
            }
            
        </script>
        <style>
            .content{
                margin-top: 15px;
                margin-left: 5%;
                width: 90%;
            }
            
            .image{
                width: 25%;
                float: left;
            }
            
            #linkImage{
                width: 150px;
                height: 150px;
                margin-left: 22%;
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
            <div class='nome'>
                <p>Benvenuto <%=ul.getNome()%> <%=ul.getCognome()%></p>
            </div>
            
            <div class="content">
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE ORDINI-->
                <div class="image">
                    <form name="ordini" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="Ordini.view"/>
                        <a href="javascript:ordiniSubmit();">
                            <img id="linkImage" src="/images/Ordini.png" alt="Ordini"/></br><p style="text-align: center"><b>GESTIONE ORDINI</b></p>
                        </a>
                    </form>
                </div>
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE UTENTI-->
                <div class="image">
                    <form name="utentiManagement" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="UtentiManagement.view"/>
                        <a href="javascript:utentiManagementSubmit();">
                            <img id="linkImage" src="/images/Utenti.png" alt="Utenti"/></br><p style="text-align: center"><b>GESTIONE UTENTI</b></p>
                        </a>
                    </form>
                </div>
                
                <!--FORM PER PASSARE ALLA SCHERMATA DI GESTIONE PRODOTTI-->
                <div class="image">
                    <form name="magazzino" action="Dispatcher" method="post">
                        <input type="hidden" name="controllerAction" value="ProdottoManagement.view"/>
                        <a id="magazzino" href="javascript:magazzinoSubmit();">
                            <img id="linkImage" src="/images/Magazzino.png" alt="Magazzino"/></br><p style="text-align: center"><b>GESTIONE MAGAZZINO</b></p>
                        </a>
                    </form>
                </div>
                
            </div>
            
            <div style="clear: both; margin-bottom: 15px;"></div>
            
        </main>
        
        <%@include file="/include/footer.inc" %>
        
    </body>
</html>
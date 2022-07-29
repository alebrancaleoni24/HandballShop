<header class="clearfix"><!-- Defining the header section of the page -->

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="LogOn.logout"/>
    </form>
    
    <div style="height: 126px;">
        <nav class="clearfix">
            <div class="logo">
                <a href="Dispatcher?controllerAction=Catalogo.view">
                    <img id="LogoWeb" src="/images/LogoWeb.png" width="370" height="105" alt="Torna alla Home Page"/>
                </a>
            </div>
            <div class="barra">
                <%if(loggedOn) {/*Se non sono loggato faccio vedere i tatsi carrello, ordini e logout*/%>
                    <div class="menu">
                        <a id= "carrello" href="Dispatcher?controllerAction=Acquisto.view">CARRELLO</a>
                    </div>
                    <div class="menu">
                        <a id= "ordini" href="Dispatcher?controllerAction=Ordini.view">ORDINI</a>
                    </div>
                    <div class="menu">
                        <a id= "logout" href="javascript:logoutForm.submit()">LOGOUT</a>
                    </div>
                <%}else{%>
                    <div class="menu">
                        <a id= "login" href="Dispatcher?controllerAction=LogOn.view&opzione=L">LOGIN</a>
                    </div>
                    <div class="menu">
                        <a id= "registrati" href="Dispatcher?controllerAction=LogOn.view&opzione=R">REGISTRATI</a>
                    </div>
                <%}%>
            </div>
        </nav>
    </div>
    
</header>

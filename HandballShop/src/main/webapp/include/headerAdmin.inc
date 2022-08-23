<header class="clearfix"><!-- Defining the header section of the page -->

    <form name="logoutForm" action="Dispatcher" method="post">
        <input type="hidden" name="controllerAction" value="LogOn.logout"/>
    </form>

    <div id="navigation">
        <nav class="clearfix">
            <div class="logo">
                <a href="Dispatcher?controllerAction=HomeAdmin.view">
                    <img id="LogoWeb" src="/images/HandballVectorialWeb.png" height="35%" width="30%" alt="Torna alla Home Page"/>
                </a>
            </div>
            <div class="barra">
                <div class="menu">
                    <a style="color: #f5f5f5" href="Dispatcher?controllerAction=Ordini.view">Ordini</a>
                </div>
                <div class="menu">
                    <a style="color: #f5f5f5" href="Dispatcher?controllerAction=UtentiManagement.view">Utenti</a>
                </div>
                <div class="menu">
                    <a style="color: #f5f5f5" href="Dispatcher?controllerAction=ProdottoManagement.view">Magazzino</a>
                </div>
                <div class="menu">
                    <a style="color: #f5f5f5" href="javascript:logoutForm.submit()">Logout</a>
                </div>
            </div>
        </nav>
    </div>
    
</header>
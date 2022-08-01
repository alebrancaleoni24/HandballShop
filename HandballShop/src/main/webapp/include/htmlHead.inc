<meta charset="utf-8"/>

    <title>Handball Shop: <%=menuActiveLink%></title>
    <link rel="shortcut icon" href="images/HandballVectorialWeb.png" />
    <link rel="stylesheet" href="css/handshop.css" type="text/css" media="screen">
    <script>
        var applicationMessage;
        <%if (applicationMessage != null) {%>
            applicationMessage="<%=applicationMessage%>";
        <%}%>
        function onLoadHandler() {
            try{
                mainOnLoadHandler();
            }catch(e){
            }
            
            //if(applicationMessage!=undefined) alert(applicationMessage);
        }
  
        window.addEventListener("load", onLoadHandler);
    </script> 
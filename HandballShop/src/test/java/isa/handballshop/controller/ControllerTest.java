package isa.handballshop.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class ControllerTest{

    /* Metodo di test per la registrazione di un utente */
    @Disabled
    @Test
    public void registrazioneTest(){
        BasicConfigurator.configure();
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();

        driver.get("http://localhost:8080");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement link = driver.findElement(By.linkText("Registrati"));
        link.click();

        WebElement nomeBox = driver.findElement(By.id("nome"));
        WebElement cognomeBox = driver.findElement(By.id("cognome"));
        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement passwordConfBox = driver.findElement(By.id("passwordConf"));
        WebElement nazioneBox = driver.findElement(By.id("nazione"));
        WebElement cittaBox = driver.findElement(By.id("citta"));
        WebElement viaBox = driver.findElement(By.id("via"));
        WebElement numeroCivicoBox = driver.findElement(By.id("numeroCivico"));
        WebElement capBox = driver.findElement(By.id("CAP"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        nomeBox.sendKeys("Mario");
        cognomeBox.sendKeys("Rossi");
        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        passwordConfBox.sendKeys("mr");
        nazioneBox.sendKeys("Italia");
        cittaBox.sendKeys("Roma");
        viaBox.sendKeys("via Appia");
        numeroCivicoBox.sendKeys("1");
        capBox.sendKeys("12345");
        submit.click();
        

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }


    /* Metodo per testare il login dell'utente */
    //@Disabled
    @Test
    public void loginTest(){
        BasicConfigurator.configure();
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();

        driver.get("http://localhost:8080");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement link = driver.findElement(By.linkText("Login"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        submit.click();

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }

    /* Metodo di test per l'acquisto di un prodotto */
    @Disabled
    @Test
    public void AcquistoTest(){
        BasicConfigurator.configure();
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();

        driver.get("http://localhost:8080");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Faccio il login con l'utente di prova Mario Rossi 
        WebElement link = driver.findElement(By.linkText("Login"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("mario.rossi@gmail.com");
        passwordBox.sendKeys("mr");
        submit.click();

        // Seleziono il prodotto da acquistare 
        WebElement prodotto = driver.findElement(By.id("0"));
        prodotto.click();

        // Aggiungo il prodotto al carrello 
        WebElement carrello = driver.findElement(By.id("ordina"));
        carrello.click();

        // Vado alla pagina carrello 
        WebElement carrelloLink = driver.findElement(By.id("carrello"));
        carrelloLink.click();

        // Clicco per procedere all'ordine
        WebElement ordineButton = driver.findElement(By.id("ordina"));
        ordineButton.click();

        // Inserisco i dati per il pagamento e la consegna
        WebElement nazioneBox = driver.findElement(By.id("nazione"));
        WebElement cittaBox = driver.findElement(By.id("citta"));
        WebElement viaBox = driver.findElement(By.id("via"));
        WebElement numeroCivicoBox = driver.findElement(By.id("numeroCivico"));
        WebElement capBox = driver.findElement(By.id("CAP"));
        WebElement cartaBox = driver.findElement(By.id("carta"));
        WebElement submitPagamento = driver.findElement(By.name("submitButton"));

        nazioneBox.sendKeys("Italia");
        cittaBox.sendKeys("Roma");
        viaBox.sendKeys("via Appia");
        numeroCivicoBox.sendKeys("1");
        capBox.sendKeys("12345");
        cartaBox.sendKeys("1234567890123456");
        submitPagamento.click();

        // Do l'ok dalla pagina di riepilogo
        WebElement submitOrdine = driver.findElement(By.name("submitButton"));
        submitOrdine.click();


        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Mario Rossi", value);

        driver.quit();
    }

    @Disabled
    @Test
    public void inserimentoProdottoTest(){
        BasicConfigurator.configure();
        WebDriverManager.chromedriver().setup();
        ChromeDriver driver = new ChromeDriver();

        driver.get("http://localhost:8080");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Faccio il login con l'utente admin Alessandro Brancaleoni
        WebElement link = driver.findElement(By.linkText("Login"));
        link.click();

        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("password"));
        WebElement submit = driver.findElement(By.name("submitButton"));

        emailBox.sendKeys("ale@gmail.com");
        passwordBox.sendKeys("ab");
        submit.click();

        // Entro nella pagina della giacenza di magazzino
        WebElement magazzinoLink = driver.findElement(By.id("magazzino"));
        magazzinoLink.click();

        // Entro nella pagina per inserire un nuovo prodotto
        WebElement inserisciProdottoButton = driver.findElement(By.id("inserisciProdotto"));
        inserisciProdottoButton.click();

        // Inserisco i dati del nuovo prodotto
        WebElement modelloBox = driver.findElement(By.id("modello"));
        WebElement categoriaBox = driver.findElement(By.id("categoria"));
        WebElement marcaBox = driver.findElement(By.id("marca"));
        WebElement prezzoBox = driver.findElement(By.id("prezzo"));
        Select genere = new Select(driver.findElement(By.id("genere")));
        WebElement immagineBox = driver.findElement(By.id("immagine"));
        WebElement descrizioneBox = driver.findElement(By.id("descrizione"));
        WebElement submitProdotto = driver.findElement(By.name("submitButton"));

        modelloBox.sendKeys("Stabil X");
        categoriaBox.sendKeys("Scarpe");
        marcaBox.sendKeys("Adidas");
        prezzoBox.sendKeys("139.95");
        genere.selectByValue("Uomo");
        immagineBox.sendKeys("stabil_x.png");
        descrizioneBox.sendKeys("Queste scarpe da pallamano sono la base di lancio perfetta per far decollare il tuo gioco. La struttura interna avvolgente offre comfort e traspirabilitá , mentre la tecnologia Sling-Cage, collegata al sistema di allacciatura, consente di regolare il livello di supporto delpiede. La ammortizzazione reattiva offre un ritorno di energia infinito per dare carica al tuo stile. Massima stabilitá grazie alla tecnologia Sling-Cage interna collegata al sistema di allacciatura per un supporto regolabile. Ritorno di energia imbattibile grazie alla tecnologia di ammortizzazione piú reattiva di sempre, in grado di offrire un ritorno di energia incredibile.");
        submitProdotto.click();

        // Inserisco la disponibilità del nuovo prodotto
        WebElement xs = driver.findElement(By.name("xs"));
        WebElement s = driver.findElement(By.name("s"));
        WebElement m = driver.findElement(By.name("m"));
        WebElement l = driver.findElement(By.name("l"));
        WebElement xl = driver.findElement(By.name("xl"));
        WebElement xxl = driver.findElement(By.name("xxl"));
        WebElement xxxl = driver.findElement(By.name("xxxl"));
        WebElement submitTaglie = driver.findElement(By.name("submitButton"));

        xs.sendKeys("10");
        s.sendKeys("10");
        m.sendKeys("10");
        l.sendKeys("10");
        xl.sendKeys("10");
        xxl.sendKeys("10");
        xxxl.sendKeys("10");
        submitTaglie.click();

        WebElement text = driver.findElement(By.id("loggedOn"));
        String value = text.getText();
        assertEquals("Benvenuto Alessandro Brancaleoni", value);

        driver.quit();

    }
    
}
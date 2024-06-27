# PROVA FINALE INGEGNERIA DEL SOFTWARE - AA 2023-2024

# Codex Naturalis

## Descrizione
Prova finale del corso di Ingegneria del Software.
Il progetto consiste nell'implementazione di un sistema distribuito composto da server in grado di gestire più partite 
contemporaneamente a multipli client, i quali possono scegliere se creare una nuova partita
oppure unirsi ad una partita esistente.

Il client può inoltre scegliere se connettersi al server utilizzando protocollo Socket o Java RMI e pattern MVC (Model Controller View).
Il server è quindi in grado di gestire diversi client che si connettono con diversi protocolli. 

## Gruppo
Il progetto è stato sviluppato dai seguenti studenti:

- **Giuseppe Laguardia**
- **Giorgio Mattina**
- **Maximilian Mangosi**
- **Riccardo Lapi**


## Documentazione
Di seguito il diagrammi UML del model: 

-[UML model](https://github.com/MaximiliamMangosi/ing-sw-2024-Mangosi-Laguardia-Lapi-Mattina/blob/main/deliverables/model%20UML.jpg)

All'interno della cartella "deliverables" sono inoltre presenti i sequence diagrams delle interazioni client-server.

## GUI
L'interfaccia grafica è stata realizzata utilizzando javafx e Scene Builder per i file .fxml di ogni scena.

## Funzionalità Avanzate
Oltre che alle funzionalità **BASE** quali:
- Regole Complete + TUI + GUI + RMI o Socket

| Funzionalità                                                         | Descrizione                                                                                                                                                                                              |
|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Supporto Alle Partite Multiple**                                   |                                                                                                                                                                                                          |
| **Chat In-Game sia Globale(nella medesima partita)<br/>che Privata** | **Possibile selezionare la chat globale, leggibile da tutti i giocatori all'interno di una partita, sia un altro giocatore con cui scambiare messaggi privatamente all'interno<br/>della chat privata.** |

## Installazione
1. Installa la versione 21 di Java [cliccando qui](https://www.java.com/en/download/help/download_options.html)
2. Clona il repository:
    ```bash
    git clone https://github.com/MaximiliamMangosi/ing-sw-2024-Mangosi-Laguardia-Lapi-Mattina/tree/main
    ```
3. Naviga nella directory del progetto:
    ```bash
    cd deliverables/final/jar
    ```
4. Lancia il server con il comando 
   ```bash
   java -jar server.jar
    ```
5. Se vuoi lanciare la TUI da terminale, leggi l'indirizzo ip stampato dal server e aggiungilo come parametro con questo comando
   ```bash
   java -jar TUI.jar [Server_ip]
   ```
6. Altrimenti se vuoi lanciare la GUI, leggi l'indirizzo ip stampato dal server e aggiungilo come parametro con questo comando
   ```bash
   java -jar GUI.jar [Server_ip]
   ```
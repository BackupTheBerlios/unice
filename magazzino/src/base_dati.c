/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Definizione dei tipi di variabili e di     *
 * procedure utili al database.               *
 * Utilizzo della libreria liblock per        *
 * l'accesso esclusivo al file.               */

#include "base_dati.h"

/* variabili globali */

int fd; /* file descriptor */

/* ----------------------------- crea_database ------------------------------ */

int crea_database (void) {

  /* creazione del disco per il database */
  if ( (fd = nuovo_disco(nome_file)) < 0 ) {
    fprintf(stderr, "ERRORE nella creazione del database.\n");
    return -1;
  }
  
  /* inizializzazione della memoria */
  inizializza_memoria();

  return 1;
}

/* -------------------------- inizializza_database -------------------------- */

int inizializza_database (void) {
  
  ref_t ref;
  prodotto_t prodotto;

  /* inizializzazione del disco per il database */
  if ( (fd = inizializza_disco(nome_file)) < 0 ) {
    fprintf(stderr, "ERRORE nell'inizializzazione del database.\n");
    return -1;
  }
  
  /* inizializzazione della memoria */
  inizializza_memoria();

  /* riempimento dell'albero in memoria con i dati letti su disco */
  while ( !fine_disco(fd) ) {
    /* memorizzazione della posizione d'inizio del prodotto su disco */
    if ( (ref.offset = lseek(fd, 0, SEEK_CUR)) < 0 ) {
      fprintf(stderr, "ERRORE nella lettura della posizione corrente.\n");
      return -1;
    }

    /* lettura del prodotto su disco */
    prodotto = (prodotto_t)leggi_oggetto(fd, SEEK_CUR, 0, sizeof(prodotto_t));

    if ( prodotto == NULL ) {
      fprintf(stderr, "ERRORE nella lettura del prodotto su disco.\n");
      return -1;
    }
    
    ref.codice = prodotto.codice;

    /* aggiunta delle referenze del prodotto in memoria */
    if ( aggiungi_oggetto(ref, comp) == 0 ) {
      fprintf(stderr, "ERRORE nell'aggiunta in memoria delle referenze.\n");
      return -1;
    }
  }

  return 1
}

/* ---------------------------- aggiungi_prodotto --------------------------- */

int aggiungi_prodotto (prodotto_t prodotto) {

  /* in coso di piu' aggiunte alla fine del file possibile problema */
  ref_t ref;

  /* lock esclusivo della posizione indicata del disco */
  if ( lock_mutex(fd, SEEK_END, 0, sizeof(prodotto_t)) < 0 ) {
    fprintf(stderr, "ERRORE nel lock\n");
    return -1;
  }

  /* --- inizio sezione critica protetta da lock --- */
  
  /* inizializzazione delle referenze del prodotto */
  if ( (ref.offset = lseek(fd, 0, SEEK_END)) < 0 ) {
    fprintf(stderr, "ERRORE nella lettura della posizione corrente.\n");
    return -1;
  }
  ref.codice = prodotto.codice;
  
  /* aggiunta delle referenze del prodotto in memoria */
  if ( aggiungi_oggetto(ref, comp) == 0 ) {
    fprintf(stderr, "ERRORE nell'aggiunta in memoria delle referenze.\n");
    return -1;
  }  

  /* aggiunta del prodotto su disco */
  if ( scrivi_oggetto(fd, prodotto, SEEK_CUR, 0, sizeof(prodotto_t)) < 0 ) {
    fprintf(stderr, "ERRORE nell'aggiunta su disco del prodotto.\n");
    return -1;
  }  
  
  /* --- fine sezione critica protetta da lock --- */

  /* unlock della posizione indicata del disco */
  if ( unlock_mutex(fd, SEEK_SET, ref.offset, sizeof(prodotto_t)) < 0 ) {
    fprintf(stderr, "ERRORE nell'unlock.\n");
    return -1;
  }

  return 1;
}

/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Dichiarazione dei tipi di variabili e di   *
 * procedure utili alla gestione dei dati su  *
 * disco.                                     */

#ifndef _DATI_DISCO_H
#define _DATI_DISCO_H

#include <unistd.h>
#include <sys/types.h>

/* --------------------- Dichiarazione delle procedure ---------------------- */

int nuovo_disco (const char *nome);
/* Requisiti : nessuno                                          *
 * Ruolo: crea e apre, se non esiste, il file per la gestione   *
 *   dei dati su disco.                                         *
 * Valori ritornati: restituisce il numero del file descriptor  *
 *   se l'operazione e' riuscita, -1 altrimenti.                */

int inizializza_disco (const char *nome);
/* Requisiti : nessuno                                          *
 * Ruolo: apertuna, se esiste, del file per la gestione dei     *
 *   dati su disco.                                             *
 * Valori ritornati: restituisce il numero del file descriptor  *
 *   se l'operazione e' riuscita, -1 altrimenti.                */

int fine_disco(int fd);
/* Requisiti : file descriptor corretto                         *
 * Ruolo: determina se si e' alla fine del file mantenendo      *
 *   invariata la posizione della testina di lettura/scrittura. *
 * Valori ritornati: restituisce 1 se si e' alla fine del file, *
 *   0 altrimenti e -1 se c'e' un errore.                       */

int scrivi_oggetto (int fd, void *oggetto, int whence, off_t offset, size_t size);
/* Requisiti : file descriptor corretto                         *
 * Ruolo: scrittura su disco dell'oggetto alla posizione        *
 *   indicata con gli argomenti della funzione lseek, di        *
 *   dimensione size.                                           *
 * Valori ritornati: restituisce 1 se l'operazione ha avuto     *
 *   successo, -1 altrimenti.                                    */

void * leggi_oggetto (int fd, int whence, off_t offset, size_t size);
/* Requisiti : file descriptor corretto                         *
 * Ruolo: lettura su disco dell'oggetto alla posizione          *
 *   indicata con gli argomenti della funzione lseek, di        *
 *   dimensione size.                                           *
 * Valori ritornati: restituisce l'oggetto se l'operazione è    *
 *   riuscita, NULL altrimenti.                                 */

#endif

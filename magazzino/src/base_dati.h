/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Dichiarazione dei tipi di variabili e di   *
 * procedure utili al database.               *
 * Utilizzo della libreria liblock per        *
 * l'accesso esclusivo al file.               */

#ifndef _BASE_DATI_H
#define _BASE_DATI_H

#include "lock.h"  /* libreria per la gestione dell'accesso esclusivo al file */
#include "tipi.h"  /* dichiarazioni dei tipi utili al programma               */ 

/* variabili esterne */

extern const char *nome_file; /* nome del file per la memorizzazione su disco *
			       * del database                                 */
extern const int size;        /* dimensione (in byte) dell'oggetto            */

extern int (*comp) (void *, void *); /* prototipo per la funzione di          *
                                      * comparazione                          */

/* --------------------- Dichiarazione delle procedure ---------------------- */

int crea_database (void);
/* Requisiti : nessuno                                          *
 * Ruolo: se non esiste, crea la base di dati.                  *
 * Valori ritornati: restituisce 1 se l'operazione e' riuscita, *
 *   -1 altrimenti.                                             */

int inizializza_database (void);
/* Requisiti : databese gia' creato                             *
 * Ruolo: inizializza la base di dati.                          *
 * Valori ritornati: restituisce 1 se l'operazione e' riuscita, *
 *   -1 altrimenti.                                             */

int aggiungi_prodotto (prodotto_t);
/* Requisiti : databese gia' creato                             *
 * Ruolo: aggiunge alla base di dati il prodotto passato in     *
 *   argomento alla funzione.                                   *
 * Valori ritornati: restituisce 1 se l'operazione e' riuscita, *
 *   -1 altrimenti.                                             */

int rimuovi_prodotto (codice_t);
/* Requisiti : databese gia' creato                             *
 * Ruolo: rimuove dalla base di dati il prodotto il cui codice  *
 *   e' passato in argomento alla funzione.                     *
 * Valori ritornati: restituisce 1 se l'operazione e' riuscita, *
 *   -1 altrimenti.                                             */

int modifica_prodotto ();
/* PENSARCI BENE */

prodotto_t cerca_prodotto (codice_t);
/* Requisiti : databese gia' creato                             *
 * Ruolo: cerca nella base di dati il prodotto il cui codice e' *
 *   passato in argomento alla funzione.                        *
 * Valori ritornati: restituisce il prodotto se l'operazione e' *
 *   riuscita, -1 altrimenti. !!!!!!                                */
 
#endif

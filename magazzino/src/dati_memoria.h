/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Dichiarazione dei tipi di variabili e di   *
 * procedure utili alla gestione dei dati in  *
 * memoria tramite l'utilizzo di un albero    *
 * binario ordinato.                          */

#ifndef _DATI_MEMORIA_H
#define _DATI_MEMORIA_H

#include "tree.h" /* libreria per la gestione dell'albero ordinato */

/* --------------------- Dichiarazione delle procedure ---------------------- */

void inizializza_memoria (void);
/* Requisiti : nessuno                                       *
 * Ruolo: inizializzazione della memoria                     */

int memoria_vuota (void);
/* Requisiti : memoria inizializzata                         *
 * Ruolo: Restituisce 1 se la memoria è vuota, 0 altrimenti. */

int aggiungi_oggetto (void *, int (*) (void *, void *));
/* Requisiti : memoria inizializzata                         * 
 * Ruolo: aggiunge in memoria l'oggetto se non è             *
 *   presente. Se l'operazione ha avuto successo             *
 *   restituisce 1 altrimenti 0. Per fare cio, ha bisogno    *
 *   di una funzione di comparazione per gli oggetti;        *
 *   questa rende -1, 0, 1 a seconda se il primo oggetto     *
 *   è rispetivamente minore, uguale o maggiore del          *
 *   secondo                                                 */

int rimuovi_oggetto (void *, int (*) (void *, void *));
/* Requisiti : memoria inizializzata                         *
 * Ruolo: rimuove in memoria l'oggetto, se l'operazione      *
 *   ha successo restituisce 1 altrimenti 0.                 *
 *   Per fare cio, ha bisogno di una funzione di             *
 *   comparazione per gli oggetti; questa rende -1, 0, 1     *
 *   a seconda se il primo oggetto è rispetivamente          *
 *   minore, uguale o maggiore del secondo                   */

void ** cerca_oggetto (void *, int (*) (void *, void *));
/* Requisiti : memoria inizializzata e non vuota             *
 * Ruolo: cerca all'interno della memoria l'oggetto, se lo   *
 *   trova restituisce un puntatore sull'oggetto, altrimenti *
 *   restituisce NULL. Per fare cio, ha bisogno di una       *
 *   funzione di comparazione per gli oggetti; questa rende  *
 *   -1, 0, 1 a seconda se il primo oggetto è rispetivamente *
 *   minore, uguale o maggiore del secondo                   */


#endif

/*        Quimera, luglio 2003         *
 *           Versione 1.0.0            *
 *                                     *
 * Libreria per la gestione di oggetti *
 * all'interno di un albero binario.   *
 *                                     *
 * Dichiarazione dei tipi di variabili *
 * e di procedure utili alla libreria. */

#ifndef _TREE_H
#define _TREE_H 

/* --------------------- Dichiarazione delle variabili ---------------------- */
  
struct NODO_T {                /* Struttura del nodo dell'albero */
  void *elem;
  struct NODO_T *ns, *nd;
};
typedef struct NODO_T nodo_t;

typedef nodo_t *tree_t;        /* Albero                         */ 

/* ----------------- Dichiarazione delle funzioni e procedure --------------- */

tree_t new_tree (void);
/* Requisiti : nessuno                                    *
 * Ruolo: crea un albero.                                 */

tree_t right_son (tree_t);
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: Restituisce il figlio destro dell'albero,       *
 *   NULL altrimenti.                                     */

tree_t left_son (tree_t);
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: Restituisce il figlio sinistro dell'albero,     *
 *   NULL altrimenti.                                     */

void * min_of_tree (tree_t);
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: Restituisce l'elemento più piccolo dell'albero  */

void * max_of_tree (tree_t);
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: Restituisce l'elemento più grande dell'albero   */

int tree_is_empty (tree_t);
/* Requisiti : albero inizializzato                       *
 * Ruolo: Restituisce 1 se l'albero è vuoto,              *
 *   0 altrimenti.                                        */

void ** get_elem (tree_t);
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: Restituisce il puntatore sulll'elemento della   *
 *   radice dell'albero                                   */

int add_elem (tree_t *, void *, int (*) (void *, void *));
/* Requisiti : albero inizializzato                       *
 * Ruolo: aggiunge all'albero l'elemento se non è         *
 *   presente. Se l'operazione ha avuto successo          *
 *   restituisce 1 altrimenti 0. Per fare cio, ha bisogno *
 *   di una funzione di comparazione per gli elementi;    *
 *   questa rende -1, 0, 1 a seconda se il primo elemento *
 *   è rispetivamente minore, uguale o maggiore del       *
 *   secondo                                              */

int rem_elem (tree_t *, void *, int (*) (void *, void *));
/* Requisiti : albero inizializzato                       *
 * Ruolo: rimuove dall'albero l'elemento, se              *
 *   l'operazione ha successo restituisce 1 altrimenti 0. *
 *   Per fare cio, ha bisogno di una funzione di          *
 *   comparazione per gli elementi; questa rende -1, 0, 1 *
 *   a seconda se il 1 elemento è rispetivamente minore,  *
 *   uguale o maggiore del secondo                        */

void ** find_elem (tree_t, void *, int (*) (void *, void *));
/* Requisiti : albero inizializzato e non vuoto           *
 * Ruolo: cerca all'interno dell'albero l'elemento, se lo *
 *   trova restituisce un puntatore sull'elemento,        *  
 *   altrimenti restituisce NULL. Per fare cio, ha        *
 *   bisogno di una funzione di comparazione per gli      * 
 *   elementi; questa rende -1, 0, 1 a seconda se il      *
 *   primo elemento è rispetivamente minore, uguale o     *
 *   maggiore del secon                                   */

#endif

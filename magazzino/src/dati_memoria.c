/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Definizione dei tipi di variabili e di     *
 * procedure utili alla gestione dei dati in  *
 * memoria tramite l'utilizzo di un albero    *
 * binario ordinato.                          */

#include "dati_memoria.h" 

/* variabili globali */

tree_t memoria; 

/* ------------------------------ inizializza ------------------------------- */

void inizializza_memoria (void) {

  memoria = new_tree();
}

/* ------------------------------ memoria_vuota ----------------------------- */

int memoria_vuota (void) {

  return tree_is_empty(memoria);
}

/* ----------------------------- aggiungi_oggetto --------------------------- */

int aggiungi_oggetto (void *oggetto, int (* f) (void *, void *)) {

  return add_elem(&memoria, oggetto, f);
}

/* ------------------------------ rimuovi_oggetto --------------------------- */

int rimuovi_oggetto (void *oggetto, int (* f) (void *, void *)) {

  return rem_elem(&memoria, oggetto, f);
}

/* ------------------------------- cerca_oggetto ---------------------------- */

void ** cerca_oggetto (void *oggetto, int (* f) (void *, void *)) {

  return find_elem(memoria, oggetto, f);
}

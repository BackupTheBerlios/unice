/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Dichiarazione dei tipi di variabili utili  *
 * al programma                               */

#ifndef _TIPI_H
#define _TIPI_H

/* macro */
#define NUM_CAR 50; 

/* --------------------- Dichiarazione delle variabili ---------------------- */

typedef int codice_t;         /* tipo per codice del prodotto */

struct REF_T {  
  int offset;                 /* posizione del prodotto all'interno del file */
  codice_t codice;            /* codice del prodotto */
};
typedef struct REF_T ref_t;   /* tipo per il prodotto in memoria */

struct PRODOTTO_T {
  int codice;                 /* codice del prodotto */
  char nome[NUM_CAR];         /* nome del prodoto */
  int quantita;               /* quantita' del prodotto */
  int costo;                  /* costo del prodoto */
};
typedef struct PRODOTTO_T prodotto_t; /* tipo per il prodotto su disco */

#endif

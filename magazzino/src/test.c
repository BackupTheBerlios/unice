#include "dati_memoria.h"
#include <stdlib.h>

int maggiore (void *a, void *b) {

  if ( a > b ) 
    return 1; 
  else 
    if ( a == b )
      return 0;
    else
      return -1;
}

int main (void) {

  int i;

  inizializza_memoria();

  if ( memoria_vuota() )
    printf("SI vuota\n");

  i = 5;
  aggiungi_oggetto((void *)i, maggiore);

  if ( !memoria_vuota() )
    printf("SI nn vuota\n");

  i = 2;
  aggiungi_oggetto((void *)i, maggiore);

  i = 7;
  aggiungi_oggetto((void *)i, maggiore);

  i = 2;
  if ( cerca_oggetto((void *)i, maggiore) != NULL )
    printf("SI trovato\n");
  
  i = 6;
  if ( cerca_oggetto((void *)i, maggiore) == NULL )
    printf("NON trovato\n");

  i = 5;
  rimuovi_oggetto((void *)i, maggiore);

  i = 7;
  rimuovi_oggetto((void *)i, maggiore);

  i = 2;
  rimuovi_oggetto((void *)i, maggiore);

  if ( memoria_vuota() )
    printf("SI vuota\n");

  return 0;
}

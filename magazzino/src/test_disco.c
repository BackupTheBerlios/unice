#include "dati_disco.h"
#include <stdio.h>

int main(void) {

  int fd;
  int oggetto = 384324323;
  int letto, letto1;

  if ( (fd = nuovo_disco("database")) < 0 )
    fd = inizializza_disco("database");

 lseek(fd, 6, SEEK_SET);
 
 if ( fine_disco(fd) )
    printf("Si vuoto\n");
  else
    printf("No vuoto\n");
  write(fd, &oggetto, sizeof(int));  
 
  if ( fine_disco(fd) )
    printf("Si vuoto\n");
  else
    printf("No vuoto\n");
  write(fd, &oggetto, sizeof(int)); 
  letto = (int)leggi_oggetto(fd, SEEK_SET, 6, sizeof(int));
  if ( fine_disco(fd) )
    printf("Si vuoto\n");
  else
    printf("No vuoto\n");
  /* scrivi_oggetto(fd, &oggetto, SEEK_SET, 0, sizeof(int)); */
  letto1 = (int)leggi_oggetto(fd, SEEK_CUR, 0, sizeof(int));
  if ( fine_disco(fd) )
    printf("Si vuoto\n");
  else
    printf("No vuoto\n");
  lseek(fd, 8, SEEK_SET);

  if ( fine_disco(fd) )
    printf("Si vuoto\n");
  else
    printf("No vuoto\n");

  printf("Valore %d\n", letto);
printf("Valore %d\n", letto1);

  return 0;
}

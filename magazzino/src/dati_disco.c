/*            Quimera, luglio 2003            *
 *               Versione 1.0.0               *
 *                                            *
 * Definizione dei tipi di variabili e di     *
 * procedure utili alla gestione dei dati su  *
 * disco.                                     */

#include "dati_disco.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

/* macro */
#define FLAGS_NEW O_CREAT | O_EXCL | O_RDWR | O_SYNC
#define FLAGS_INIT O_RDWR | O_SYNC
#define PERM S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP

/* ---------------------------- nuovo_disco --------------------------------- */

int nuovo_disco (const char *nome) {

  int fd;

  if ( (fd = open(nome, FLAGS_NEW, PERM)) < 0 ) {
    fprintf(stderr, "ERRORE nella creazione del file %s.\n", nome);
    return -1;
  }

  return fd;
}

/* ------------------------- inizializza_disco ------------------------------ */
int inizializza_disco (const char *nome) {
 
  int fd;

  if ( (fd = open(nome, FLAGS_INIT, PERM)) < 0 ) {
    fprintf(stderr, "ERRORE nell'appertura del file %s.\n", nome);
    return -1;
  }

  return fd;
}

/* ----------------------------- fine_disco --------------------------------- */

int fine_disco(int fd) {

  off_t posizione_corrente;
  int valore;
  int tmp;

  if ( (posizione_corrente = lseek(fd, 0, SEEK_CUR)) < 0 ) {
    fprintf(stderr, "ERRORE nella lettura della posizione corrente.\n");
    return -1;
  }

  if ( (valore = read(fd, &tmp, 1)) < 0 ) {
    fprintf(stderr, "ERRORE nella lettura dell'oggetto.\n");
    return -1;
  }

  if ( lseek(fd, posizione_corrente, SEEK_SET) < 0 ) {
    fprintf(stderr, "ERRORE nel posizionamento delle testina.\n");
    return -1;
  }

  if ( valore == 0 )
    return 1;
  else
    return 0;
}

/* --------------------------- scrivi_oggetto ------------------------------- */

int scrivi_oggetto (int fd, void *oggetto, int whence, off_t offset, size_t size) {

  if ( lseek(fd, offset, whence) < 0 ) {
    fprintf(stderr, "ERRORE nel posizionamento della testina (lseek).\n");
    return -1;
  }
  
  if ( write(fd, oggetto, size) < 0 ) {
    fprintf(stderr, "ERRORE nella scrittura dell'oggetto.\n");
    return -1;
  }

  return 1;
}

/* ---------------------------- leggi_oggetto ------------------------------- */

void * leggi_oggetto (int fd, int whence, off_t offset, size_t size) {
  
  void *buf;

  if ( lseek(fd, offset, whence) < 0 ) {
    fprintf(stderr, "ERRORE nel posizionamento della testina (lseek).\n");
    return NULL;
  }
  
  if ( read(fd, &buf, size) < 0 ) {
    fprintf(stderr, "ERRORE nella lettura dell'oggetto.\n");
    return NULL;
  }

  return buf;
}

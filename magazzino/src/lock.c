/*        Quimera, luglio 2003           *
 *           Versione 1.0.0              *
 *                                       *
 * Libreria per la gestione della mutua  *
 * esclusione usando il "file locking".  *
 *                                       *
 * Definizione dei tipi di variabili     *
 * e di procedure utili alla libreria.   */

#include "lock.h"

/* ----------------------------- create_mutex ------------------------------- */
 
int create_mutex (const char *path_name) {
  
  return open(path_name, O_EXCL | O_CREAT); 
}

/* ------------------------------ lock_mutex -------------------------------- */

int lock_mutex (int fd, short whence, off_t start, off_t len) {

  struct flock lock; 

  /* inizializzazione della struttura */
  lock.l_type = F_WRLCK;  /* tipo: read or write */ 
  lock.l_whence = whence; /* posizione d'inizio */ 
  lock.l_start = start;   /* inizio della regione dalla posizione specificata */
  lock.l_len = len;       /* lunghezza in bytes della regione */

  return fcntl(fd, F_SETLKW, &lock);
}

/* ----------------------------- unlock_mutex ------------------------------- */

int unlock_mutex (int fd, short whence, off_t start, off_t len) {

  struct flock lock; 

  /* inizializzazione della struttura */
  lock.l_type = F_UNLCK;  /* tipo: read or write */ 
  lock.l_whence = whence; /* posizione d'inizio */ 
  lock.l_start = start;   /* inizio della regione dalla posizione specificata */
  lock.l_len = len;       /* lunghezza in bytes della regione */
  
  return fcntl(fd, F_SETLK, &lock);
}

/* ----------------------------- remove_mutex ------------------------------- */

int remove_mutex (const char *path_name) {

  return unlink(path_name); 
}

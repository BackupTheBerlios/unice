
#include <sys/types.h>
#include <unistd.h>
#include "lock.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>

int main(int argc, char *argv[]) {

  int fd1, fd2;
  int cw, i;
  int pid;
  int valore = 0, valore1;

  if ( (fd1 = open("file.lock", O_CREAT | O_RDWR)) < 0 ) {
    fprintf(stderr, "ERRORE nell'appertura del file.lock\n");
    exit(1);
  }

  if ( (fd2 = open("file.unlock", O_CREAT | O_RDWR)) < 0 ) {
    fprintf(stderr, "ERRORE nell'appertura del file.unlock\n");
    exit(1);
  }
  if ( read(fd1, &valore1, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella lettura del file.lock\n");
	exit(4);
  }

  printf("%d\n", valore1);
  if ( read(fd2, &valore1, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella lettura del file.lock\n");
	exit(4);
  }
  printf("%d\n", valore1);

  lseek(fd1, 0, SEEK_SET);
  lseek(fd2, 0, SEEK_SET);

  if ( (cw = write(fd1, &valore, sizeof(int))) < 0 ) {
    fprintf(stderr, "ERRORE nella inizializzazione del file.lock\n");
    exit(2);
  }

  if ( (cw = write(fd2, &valore, sizeof(int))) < 0 ) {
    fprintf(stderr, "ERRORE nella inizializzazione del file.unlock\n");
    exit(2);
  }

  close(fd1);
  close(fd2);

  for (i = 0; i < 1300; i++) {
    if ( (pid = fork()) < 0) exit(11);
    
    if ( pid == 0 ) { /* fils */
      
      sleep((1300-i)/100);

      if ( (fd1 = open("file.lock", O_CREAT | O_RDWR | O_SYNC)) < 0 ) {
	fprintf(stderr, "ERRORE nell'appertura del file.lock\n");
	exit(1);
      }
      
      if ( (fd2 = open("file.unlock", O_CREAT | O_RDWR | O_SYNC)) < 0 ) {
	fprintf(stderr, "ERRORE nell'appertura del file.unlock\n");
	exit(1);
      }

            
      if ( lock_mutex(fd1, SEEK_SET, 0, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nel lock del file.lock\n");
	exit(3);
      }
      
      if ( read(fd1, &valore, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella lettura del file.lock\n");
	exit(4);
      }
      printf("LOCK valore %d\n", valore);
      valore++;
      
      lseek(fd1, 0, SEEK_SET);

      if ( write(fd1, &valore, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella scrittura del file.lock\n");
	exit(5);
      }
      
      if ( unlock_mutex(fd1, SEEK_SET, 0, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nel unlock del file.lock\n");
	exit(6);
      }

      if ( read(fd2, &valore, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella lettura del file.unlock\n");
	exit(4);
      }
    
      printf("UNLOCK valore %d\n", valore);
      valore++;

      lseek(fd2, 0, SEEK_SET);

      if ( write(fd2, &valore, sizeof(int)) < 0 ) {
	fprintf(stderr, "ERRORE nella scrittura del file.unlock\n");
	exit(5);
      }
      
      close(fd1);
      close(fd2);
      
      return 0;
    }
  }
  
  /* if ( pid > 0 ) {   /\* pere *\/ */

    

/*     if ( (fd1 = open("file.lock", O_CREAT | O_RDWR)) < 0 ) { */
/* 	fprintf(stderr, "ERRORE nell'appertura del file.lock\n"); */
/* 	exit(1); */
/*       } */
      
/*     if ( (fd2 = open("file.unlock", O_CREAT | O_RDWR)) < 0 ) { */
/*       fprintf(stderr, "ERRORE nell'appertura del file.unlock\n"); */
/*       exit(1); */
/*     } */

/*     if ( lock_mutex(fd1, SEEK_SET, 0, sizeof(int)) < 0 ) { */
/* 	fprintf(stderr, "ERRORE nel lock del file.lock\n"); */
/* 	exit(3); */
/*       } */

/*     if ( read(fd1, &valore, sizeof(int)) < 0 ) { */
/*       fprintf(stderr, "ERRORE nella lettura del file.lock\n"); */
/*       exit(4); */
/*     } */
    
/*     fprintf(stdout, "Il valore del file.lock è %d\n", valore); */

/*     if ( unlock_mutex(fd1, SEEK_SET, 0, sizeof(int)) < 0 ) { */
/* 	fprintf(stderr, "ERRORE nel unlock del file.lock\n"); */
/* 	exit(6); */
/*       } */

/*     if ( lock_mutex(fd2, SEEK_SET, 0, sizeof(int)) < 0 ) { */
/* 	fprintf(stderr, "ERRORE nel lock del file.lock\n"); */
/* 	exit(3); */
/*       } */

/*     if ( read(fd2, &valore, sizeof(int)) < 0 ) { */
/*       fprintf(stderr, "ERRORE nella lettura del file.unlock\n"); */
/*       exit(4); */
/*     } */

/*     fprintf(stdout, "Il valore del file.unlock è %d\n", valore); */
  
/*     if ( unlock_mutex(fd2, SEEK_SET, 0, sizeof(int)) < 0 ) { */
/* 	fprintf(stderr, "ERRORE nel unlock del file.lock\n"); */
/* 	exit(6); */
/*       } */
    
/*   } */

  close(fd1);
  close(fd2);

  return 0;
}

/*        Quimera, luglio 2003         *
 *           Versione 1.0.0            *
 *                                     *
 * Libreria per la gestione di oggetti *
 * all'interno di un albero binario.   *
 *                                     *
 * Definizione delle variabili e delle *
 * procedure utili alla libreria.      */

#include "tree.h"
#include <stdlib.h>
#include <stdio.h>

/* -------------------------------- new_tree -------------------------------- */

tree_t new_tree (void) {
  
  return NULL;
}

/* ------------------------------- right_son -------------------------------- */

tree_t right_son (tree_t tree) {

  return (*tree).nd;
}

/* ------------------------------- left_son --------------------------------- */

tree_t left_son (tree_t tree) {

  return (*tree).ns;
}

/* ------------------------------ tree_is_empty ----------------------------- */

int tree_is_empty (tree_t tree) {

  if ( tree == NULL )
    return 1;
  else 
    return 0;
}

/* -------------------------------- get_elem -------------------------------- */

void ** get_elem (tree_t tree) {

  return &(*tree).elem;
}

/* ------------------------------- min_of_tree ------------------------------ */

void * min_of_tree (tree_t tree) {

  if ( tree_is_empty(left_son(tree)) )
    return *(get_elem(tree));
  else
    return min_of_tree(left_son(tree));
}

/* ------------------------------- min_of_tree ------------------------------ */

void * max_of_tree (tree_t tree) {

  if ( tree_is_empty(right_son(tree)) )
    return *(get_elem(tree));
  else
    return max_of_tree(right_son(tree));
}


/* -------------------------------- add_elem -------------------------------- */

int add_elem (tree_t *tree, void *elem, int (* f) (void *, void *)) {
  
  nodo_t *nodo;

  if ( tree_is_empty(*tree) )
    if ( (nodo = malloc(sizeof(nodo_t))) == NULL )
      return 0; /* problema nell'allocazoine della memoria */ 
    else { /* inizializzazione del nodo */
      (*nodo).elem = elem;
      (*nodo).ns = NULL;
      (*nodo).nd = NULL;
      *tree = nodo;
      return 1;
    }
  else { /* l'albero non è vuoto */
    /* inserire nella giusta posizione l'elemento */
    switch ( f(elem, (**tree).elem) ) {
    case -1 :
      return add_elem(&(**tree).ns, elem, f);
      break;
    case 0 :
      return 0;
      break;
    case 1 : 
      return add_elem(&(**tree).nd, elem, f);
      break;
    default :
      return 0;
    }
  }
}

/* -------------------------------- rem_elem -------------------------------- */

int rem_elem (tree_t *tree, void *elem, int (* f) (void *, void *)) {
  
  tree_t tmp;

  if ( tree_is_empty(*tree) ) 
    /* l'albero è vuoto */
    return 0;
  
  if ( tree_is_empty(left_son(*tree)) ) {
    /* l'albero non ha figli sinistri */
    if ( !f((**tree).elem, elem) ) {
      tmp = *tree;
      *tree = right_son(*tree);;
      free(tmp);
      return 1;
    }
    else
      /* ricorsività sul lato destro dell'albero */
      return rem_elem(&(**tree).nd, elem, f);
  }

  if ( tree_is_empty(right_son(*tree)) ) {
    /* l'albero non ha figli destri */
    if ( !f((**tree).elem, elem) ) {
      tmp = *tree; 
      *tree = left_son(*tree);
      free(tmp);
      return 1;
    }
    else
      /* ricorsività sul lato sinistro dell'albero */
      return rem_elem(&(**tree).ns, elem, f);
  }

  /* albero ha entrambi i figli */
  if ( !f((**tree).elem, elem) ) {
    (**tree).elem = max_of_tree(left_son(*tree));
    return rem_elem(&(**tree).ns, (**tree).elem, f);
  }
  else
    /* ricorsività sul lato giusto dell'albero */
    switch ( f(elem, (**tree).elem) ) {
    case -1 :
      return rem_elem(&(**tree).ns, elem, f);
      break;
    case 0 :
      return 0;
      break;
    case 1 : 
      return rem_elem(&(**tree).nd, elem, f);
      break;
    default :
      return 0;
    }
}

/* -------------------------------- find_elem ------------------------------- */

void ** find_elem (tree_t tree, void *elem, int (* f) (void *, void *)) {

  if ( tree_is_empty(tree) )
     return NULL;
  
  switch ( f(elem, *(get_elem(tree))) ) {
  case -1 : 
    return find_elem (left_son(tree), elem, f);
    break;
  case 0 : 
    return get_elem(tree);
    break;
  case 1 :
    return find_elem (right_son(tree), elem, f);
    break;
  default :
    return NULL; 
  }
}


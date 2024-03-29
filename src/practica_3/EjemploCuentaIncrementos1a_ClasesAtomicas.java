package practica_3;

import java.util.concurrent.atomic.AtomicInteger;

// ============================================================================
class CuentaIncrementos_ClasesAtomicas {
// ============================================================================
  AtomicInteger numIncrementos = new AtomicInteger(0);

  // --------------------------------------------------------------------------
  void incrementaNumIncrementos() {
    numIncrementos.incrementAndGet();
  }

  // --------------------------------------------------------------------------
  int dameNumIncrementos() {
    return( numIncrementos.get() );
  }
}


// ============================================================================
class MiHebra_ClasesAtomicas extends Thread {
// ============================================================================
  int tope;
  CuentaIncrementos_ClasesAtomicas  c;

  // --------------------------------------------------------------------------
  public MiHebra_ClasesAtomicas( int tope, CuentaIncrementos_ClasesAtomicas c ) {
    this.tope  = tope;
    this.c     = c;
  }

  // --------------------------------------------------------------------------
  public void run() {
    for( int i = 0; i < tope; i++ ) {
      c.incrementaNumIncrementos();
    }
  }
}

// ============================================================================
class EjemploCuentaIncrementos_ClasesAtomicas {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    long    t1, t2;
    double  tt;
    int     numHebras, tope;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.err.println( "Uso: java programa <numHebras> <tope>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
      tope      = Integer.parseInt( args[ 1 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      tope      = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }

    System.out.println( "numHebras: " + numHebras );
    System.out.println( "tope:      " + tope );
    
    System.out.println( "Creando y arrancando " + numHebras + " hebras." );
    t1 = System.nanoTime();
    MiHebra_ClasesAtomicas v[] = new MiHebra_ClasesAtomicas[ numHebras ];
    CuentaIncrementos_ClasesAtomicas c = new CuentaIncrementos_ClasesAtomicas();
    for( int i = 0; i < numHebras; i++ ) {
      v[ i ] = new MiHebra_ClasesAtomicas( tope, c );
      v[ i ].start();
    }
    for( int i = 0; i < numHebras; i++ ) {
      try {
        v[ i ].join();
      } catch( InterruptedException ex ) {
        ex.printStackTrace();
      }
    }
    t2 = System.nanoTime();
    tt = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Total de incrementos: " + c.dameNumIncrementos() );
    System.out.println( "Tiempo transcurrido en segs.: " + tt );
  }
}


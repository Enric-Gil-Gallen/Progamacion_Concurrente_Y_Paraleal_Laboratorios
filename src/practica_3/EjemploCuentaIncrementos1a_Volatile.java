package practica_3;

// ============================================================================
class CuentaIncrementos_Volatile {
// ============================================================================
  int numIncrementos = 0;

  // --------------------------------------------------------------------------
  void incrementaNumIncrementos() {
    numIncrementos++;
  }

  // --------------------------------------------------------------------------
  int dameNumIncrementos() {
    return( numIncrementos );
  }
}


// ============================================================================
class MiHebra_Volatile extends Thread {
// ============================================================================
  volatile int tope;
  volatile CuentaIncrementos_Volatile  c;

  // --------------------------------------------------------------------------
  public MiHebra_Volatile( int tope, CuentaIncrementos_Volatile c ) {
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
class EjemploCuentaIncrementos_Volatile {
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
    MiHebra_Volatile v[] = new MiHebra_Volatile[ numHebras ];
    CuentaIncrementos_Volatile c = new CuentaIncrementos_Volatile();
    for( int i = 0; i < numHebras; i++ ) {
      v[ i ] = new MiHebra_Volatile( tope, c );
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


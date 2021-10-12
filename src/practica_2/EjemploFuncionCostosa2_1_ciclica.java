package practica_2;

// ============================================================================
class EjemploFuncionCostosa2_1_ciclica {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int     n, numHebras;
    long    t1, t2;
//    double  tt, sumaX, sumaY;
    double  sumaX, sumaY, ts, tc, tb;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 2 ) {
      System.err.println( "Uso: java programa <numHebras> <tamanyo>" );
//      System.exit( -1 );
    }
    try {
      numHebras = 4;
      n         = 13;
//      numHebras = Integer.parseInt( args[ 0 ] );
//      n         = Integer.parseInt( args[ 1 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      n         = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }

    // Crea los vectores.
    double vectorX[] = new double[ n ];
    double vectorY[] = new double[ n ];

    //
    // Implementacion paralela ciclica.
    //
    inicializaVectorX( vectorX );
    inicializaVectorY( vectorY );
    t1 = System.nanoTime();
    // Gestion de hebras para la implementacion paralela ciclica

    // Crea y arranca el vector de hebras.
    Thread[] vectorHebras = new Thread[numHebras];
    for (int i = 0; i < numHebras; i++) {
      vectorHebras[i] = new MyThread(i,n, numHebras);
    }

    for (int i = 0; i < numHebras; i++) {
      vectorHebras[i].start();
    }

    // Espera a que terminen las hebras.

    for (int i = 0; i < numHebras; i++) {
      try {
        vectorHebras[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    t2 = System.nanoTime();
    tc = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.println( "Tiempo paralela ciclica (seg.):              " + tc );
//    System.out.println( "Incremento paralela ciclica:                 " + ... );
    System.out.println( "Incremento paralela ciclica:                 "  );
    //// imprimeResultado( vectorX, vectorY );
    // Comprueba el resultado. 
    sumaX = sumaVector( vectorX );
    sumaY = sumaVector( vectorY );
    System.out.println( "Suma del vector X:          " + sumaX );
    System.out.println( "Suma del vector Y:          " + sumaY );
    //


    System.out.println( "Fin del programa." );
  }

  // --------------------------------------------------------------------------
  static void inicializaVectorX( double vectorX[] ) {
    if( vectorX.length == 1 ) {
      vectorX[ 0 ] = 0.0;
    } else {
      for( int i = 0; i < vectorX.length; i++ ) {
        vectorX[ i ] = 10.0 * ( double ) i / ( ( double ) vectorX.length - 1 );
      }
    }
  }

  // --------------------------------------------------------------------------
  static void inicializaVectorY( double vectorY[] ) {
    for( int i = 0; i < vectorY.length; i++ ) {
      vectorY[ i ] = 0.0;
    }
  }

  // --------------------------------------------------------------------------
  static double sumaVector( double vector[] ) {
    double  suma = 0.0;
    for( int i = 0; i < vector.length; i++ ) {
      suma += vector[ i ];
    }
    return suma;
  }

  // --------------------------------------------------------------------------
  static double evaluaFuncion( double x ) {
    return Math.sin( Math.exp( -x ) + Math.log1p( x ) );
  }

  // --------------------------------------------------------------------------
  static void imprimeVector( double vector[] ) {
    for( int i = 0; i < vector.length; i++ ) {
      System.out.println( " vector[ " + i + " ] = " + vector[ i ] );
    }
  }

  // --------------------------------------------------------------------------
  static void imprimeResultado( double vectorX[], double vectorY[] ) {
    for( int i = 0; i < Math.min( vectorX.length, vectorY.length ); i++ ) {
      System.out.println( "  i: " + i + 
                          "  x: " + vectorX[ i ] +
                          "  y: " + vectorY[ i ] );
    }
  }

  static class MyThread extends Thread {
    int miId, nElem, numHebdas;

    public MyThread(int miId, int nElem, int numHebdas) {
      this.miId = miId;
      this.nElem = nElem;
      this.numHebdas = numHebdas;
    }

    // Implementacion paralela con distribucion ciclica
    public void run() {
      int ini = miId;
      int fin = nElem;

      for (int i = ini; i < fin; i += numHebdas) {
        System.out.print(i + " ");
      }

      System.out.println("-------------------");
    }
  }
}


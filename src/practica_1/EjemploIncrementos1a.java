package practica_1;

// ============================================================================
class CuentaIncrementos1a {
// ============================================================================
  long contador = 0;

  // --------------------------------------------------------------------------
  void incrementaContador() {
    contador++;
  }

  // --------------------------------------------------------------------------
  long dameContador() {
    return( contador );
  }
}


// ============================================================================
class EjemploIncrementos1a {
// ============================================================================

  // --------------------------------------------------------------------------
  public static void main( String args[] ) {
    int  numHebras;

    // Comprobacion y extraccion de los argumentos de entrada.
    if( args.length != 1 ) {
      System.err.println( "Uso: java programa <numHebras>" );
      System.exit( -1 );
    }
    try {
      numHebras = Integer.parseInt( args[ 0 ] );
    } catch( NumberFormatException ex ) {
      numHebras = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
    }

    System.out.println( "numHebras: " + numHebras );

    CuentaIncrementos1a cont = new CuentaIncrementos1a();

    for (int i = 0; i < numHebras; i++){
      new MiHebra(cont.dameContador()).start();
      cont.incrementaContador();
    }
  }

  static class MiHebra extends Thread {
    long miId;
    int objeto;

    public MiHebra( long miId ) {
      this.miId = miId;
    }

    public void run() {
      int num_impresinos = 1000000;
      System.out.println("Empieza la hebra: " + miId );
      for (int i = 0; i < num_impresinos; i++ ){
        objeto += objeto;
      }
      System.out.println("Valor del objeto: " + objeto );
      System.out.println("Finaliza la hebra: " + miId );
    }
  }

}


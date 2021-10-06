package practica_1;

import java.util.Scanner;

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

      CuentaIncrementos1a cont = new CuentaIncrementos1a();

      Thread[] vecThread = new Thread[numHebras];

      for (int i = 0; i < numHebras; i++){
        vecThread[i] =  new MiHebra(cont.dameContador());
        vecThread[i].start();
        vecThread[i].join();
        cont.incrementaContador();
      }

      System.out.println("El contador vale: " + cont.dameContador());

    } catch(NumberFormatException | InterruptedException ex ) {
      numHebras = -1;
      System.out.println( "ERROR: Argumentos numericos incorrectos." );
      System.exit( -1 );
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
        objeto += i;
      }
      System.out.println("Valor del objeto: " + objeto );
      System.out.println("Finaliza la hebra: " + miId );
    }
  }

}


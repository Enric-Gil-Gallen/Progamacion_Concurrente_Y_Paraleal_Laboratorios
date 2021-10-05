package practica_1;

class MiHebra extends Thread {
  int miId;
  public MiHebra( int miId ) {
    this.miId = miId;
  }

  public void run() {
    int num_impresinos = 1000;
    for (int i = 0; i < num_impresinos; i++ ){
      System.out.println("Mi identificador de hebra es: "+ miId);
    }
  }
}

class EjemploCreacionThread {
  public static void main( String args[] ) {
    new MiHebra(0).start();
    new MiHebra(1).start();
  }
}

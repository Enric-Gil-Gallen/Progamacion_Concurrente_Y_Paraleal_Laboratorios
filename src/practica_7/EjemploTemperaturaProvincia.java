package practica_7;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class EjemploTemperaturaProvincia {
  public static void main(String[] args) {
    int                numHebras, codProvincia, desp;
    String             nombreFichero = "";
    long               t1, t2, tt[];
    double             ts, tp;
    PuebloMaximaMinima MaxMin;
    PuebloMaximaMinimaParalela MaxMinParalela;

    // Comprobacion y extraccion de los argumentos de entrada.
//    if( args.length != 3 ) {
//      System.out.println( "ERROR: numero de argumentos incorrecto.");
//      System.out.println( "Uso: java programa <numHebras> <provincia> <desplazamiento>" );
//      System.exit( -1 );
//    }
    try {
      numHebras    = 4;
      codProvincia = 10;
      desp         =  0;
//      numHebras    = Integer.parseInt( args[ 0 ] );
//      codProvincia = Integer.parseInt( args[ 1 ] );
//      desp         = Integer.parseInt( args[ 2 ] );
    } catch( NumberFormatException ex ) {
      numHebras    = -1;
      codProvincia = -1;
      desp         = -1;
      System.out.println( "ERROR: Numero de entrada incorrecto." );
      System.exit( -1 );
    }
    if (numHebras <= 0) {
      System.out.println( "ERROR: El numero de Hebras debe ser un numero entero mayor que 0." );
      System.exit( -1 );
    }
    if ((codProvincia < 1) || (codProvincia > 50)) {
      System.out.println( "ERROR: El codigo de la provincia debe ser un numero entero " +
                         "comprendido entre 1 y 50." );
      System.exit( -1 );
    }
    if ((desp < 0) || (desp >= 7)) {
      System.out.println( "ERROR: El desplazamiento debe ser un numero entero comprendido " +
                         "entre 0 y 6." );
      System.exit( -1 );
    }
    if (codProvincia < 10) {
      nombreFichero = "codPueblos_0" + codProvincia + ".txt";
    } else {
      nombreFichero = "codPueblos_"  + codProvincia + ".txt";
    }
    
    System.out.println();
    System.out.println( "Obtiene el pueblo de una provincia con mayor diferencia " +
                       "de temperatura." );
    
    // Seleccion del dia elegido
    String fecha;
    Calendar c = Calendar.getInstance();
    Integer dia, mes, anyo;
    
    c.add(Calendar.DAY_OF_MONTH, desp);
    dia = c.get(Calendar.DATE);
    mes = c.get(Calendar.MONTH) + 1;
    anyo = c.get(Calendar.YEAR);
    
    fecha = String.format("%02d", anyo) + "-" + String.format("%02d", mes) + "-" +
    String.format("%02d", dia);
    System.out.println(fecha);
    
    //
    // Implementacion secuencial sin temporizar.
    //
    MaxMin = new PuebloMaximaMinima();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    File f = new File(nombreFichero);
    if (f.exists()) {
      obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 0, numHebras);
    } else {
      obtenMayorDiferenciaAFichero_Secuencial (nombreFichero, fecha, codProvincia, MaxMin);
    }
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " +
                       MaxMin.dameTemperaturaMaxima() + " , Minima = " +
                       MaxMin.dameTemperaturaMinima() );
    
    //
    // Implementacion secuencial.
    //
    System.out.println();
    t1 = System.nanoTime();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 0, numHebras);
    t2 = System.nanoTime();
    ts = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.print( "Implementacion secuencial.                           \n" );
    System.out.println( " Tiempo(s): " + ts );
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " +
                       MaxMin.dameTemperaturaMaxima() + " , Minima = " +
                       MaxMin.dameTemperaturaMinima() );
    
    //
    // Implementacion paralela: Gestion Propia.
    //
    System.out.println();
    t1 = System.nanoTime();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 1, numHebras);    t2 = System.nanoTime();
    tp = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.print( "Implementacion paralela: Gestion Propia.     \n" );
    System.out.println( " Tiempo(s): " + tp + " , Incremento: " + ts/tp );
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " + MaxMin.min + " , Minima = " + MaxMin.max );

    //
    // Implementacion paralela: Thread Pool isTerminated.

    System.out.println();
    t1 = System.nanoTime();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 2, numHebras);
    t2 = System.nanoTime();
    tp = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.print( "Implementacion paralela: Thread Pool isTerminated.     \n" );
    System.out.println( " Tiempo(s): " + tp + " , Incremento: " + ts/tp );
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " + MaxMin.min + " , Minima = " + MaxMin.max );

    //
    // Implementacion paralela: Thread Pool con awaitTermination.
    //
        /*
    System.out.println();
    t1 = System.nanoTime();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 3, numHebras);
    t2 = System.nanoTime();
    tp = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.print( "Implementacion paralela: Thread Pool con awaitTermination.    \n" );
    System.out.println( " Tiempo(s): " + tp + " , Incremento: " + ts/tp );
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " + MaxMin.min + " , Minima = " + MaxMin.max );
    */
    //
    // Implementacion paralela: Thread Pool con Future.
    //
        /*
    System.out.println();
    t1 = System.nanoTime();
    MaxMinParalela = new PuebloMaximaMinimaParalela();
    obtenMayorDiferenciaDeFichero (nombreFichero, fecha, codProvincia, MaxMin, MaxMinParalela, 4, numHebras);
    t2 = System.nanoTime();
    tp = ( ( double ) ( t2 - t1 ) ) / 1.0e9;
    System.out.print( "Implementacion paralela: Thread Pool con Future.    \n" );
    System.out.println( " Tiempo(s): " + tp + " , Incremento: " + ts/tp );
    System.out.println( "  Pueblo: " + MaxMin.damePueblo() + " , Maxima = " + MaxMin.min + " , Minima = " + MaxMin.max );
    */
    
  }
  
  // --------------------------------------------------------------------------
  public static void obtenMayorDiferenciaAFichero_Secuencial (String nombreFichero, 
                       String fecha, int codProvincia, PuebloMaximaMinima MaxMin) {
    FileWriter fichero = null;
    PrintWriter pw = null;
    
    // Verifica todas los codigos de pueblos y escribe el fichero
    try
    {
      // Apertura del fichero y creacion de FileWriter para poder
      // hacer una lectura comoda (disponer del metodo readLine()).
      
      fichero = new FileWriter(nombreFichero);
      pw = new PrintWriter(fichero);
      
      for (int i=codProvincia*1000; i<(codProvincia+1)*1000; i++){
        if (ProcesaPueblo(fecha, i, MaxMin, false) == true) {
          pw.println(i);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        // Nuevamente aprovechamos el finally para
        // asegurarnos que se cierra el fichero.
        if (null != fichero)
          fichero.close();
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
  }
  
  // --------------------------------------------------------------------------
  
  public static void obtenMayorDiferenciaDeFichero (String nombreFichero, String fecha, 
                       int codProvincia, PuebloMaximaMinima MaxMin, PuebloMaximaMinimaParalela MaxMinParalela, int opcion, int numHebras) {
    File fichero = null;
    FileReader fr = null;
    BufferedReader br = null;
    
    // Procesa el fichero
    try
    {
      // Apertura del fichero y creacion de BufferedReader para poder
      // hacer una lectura comoda (disponer del metodo readLine()).
      fichero = new File (nombreFichero);
      fr = new FileReader (fichero);
      br = new BufferedReader(fr);
      
      String         linea;
      ExecutorService    exec;
      switch (opcion) {
        case 0:  // Caso secuencial
          while( ( linea = br.readLine() ) != null ) {
            int codPueblo = Integer.parseInt(linea);
            ProcesaPueblo(fecha, codPueblo, MaxMin, false);
          }
          break;
        case 1:  // Gestion Propia
          // Ejecuto la hebras consumidoras
          final ArrayBlockingQueue<TareaEnColaGestionPropia> cola = new ArrayBlockingQueue<TareaEnColaGestionPropia>(numHebras);

          Hebra_1_GestionPropia v[] = new Hebra_1_GestionPropia[ numHebras ];
          for( int i = 0; i < numHebras; i++ ) {
            v[ i ] = new Hebra_1_GestionPropia(cola, fecha, MaxMinParalela);
            v[ i ].start();
          }

          while( ( linea = br.readLine() ) != null ) {
            int codPueblo = Integer.parseInt(linea);
            cola.put(new TareaEnColaGestionPropia(false, codPueblo));
          }

          // Enviar colas envenenadas
          for (int i = 0; i < numHebras; i++){
            cola.put(new TareaEnColaGestionPropia(true, -1));
          }

          // Espero a que las hebras consumidoras allan terminado
          for( int i = 0; i < numHebras; i++ ) {
            try {
              v[ i ].join();
            } catch( InterruptedException ex ) {
              ex.printStackTrace();
            }
          }

          break;
        case 2: // ThreadPools con isTerminated


          break;
        case 3: // ThreadPools con awaitTermination
          // ...
          break;
        case 4: // ThreadPools + con Future
          // ...
          break;
        default:
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      // En el finally cerramos el fichero, para asegurarnos
      // que se cierra tanto si todo va bien como si salta
      // una excepcion.
      try{
        if( null != fr ){
          fr.close();
        }
      }catch (Exception e2){
        e2.printStackTrace();
      }
    }
  }

  // --------------------------------------------------------------------------
  public static boolean ProcesaPueblo (String fecha, int codPueblo, PuebloMaximaMinima MaxMin,
                                       boolean imprime) {
    URL            url;
    InputStream    is = null;
    BufferedReader br;
    String         line, poblacion = new String (), provincia = new String ();
    int            state, num[]=new int[2];
    boolean        res = false;
    
    // Procesamiento de la informacion XML asociada a codPueblo
    // Actualizacion de MaxMin de acuerdo a los valores obtenidos
    try {
      String urlStr = "http://www.aemet.es/xml/municipios/localidad_" +
      String.format("%05d",codPueblo)+ ".xml";
      url = new URL(urlStr);
      is  = url.openStream();  // throws an IOException
      br  = new BufferedReader(new InputStreamReader(is));
      if (imprime) System.out.println(urlStr);
      
      state = 0;
      while (((line = br.readLine()) != null) && (state < 6)) {
        //        System.out.println (line);
        if ((state == 0) && (line.contains ("nombre"))) {
          poblacion=line.split(">")[1].split("<")[0].split("/")[0];
          state++;
        } else if ((state == 1) && (line.contains ("provincia"))) {
          provincia=line.split(">")[1].split("<")[0].split("/")[0];
          state++;
        } else if ((state == 2) && (line.contains (fecha))) {
          state++;
        } else if ((state == 3) && (line.contains ("temperatura"))) {
          state++;
        } else if ((state > 3) && ((line.contains ("maxima")) || (line.contains ("minima")))) {
          num[state-4] = Integer.parseInt (line.split(">")[1].split("<")[0]);
          state++;
        }
      }
      // System.out.println("(" + codPueblo + ") " + poblacion + "(" + provincia + ") => " +
      //                    "(" + num[0] + " , " + num[1] + ")");
      MaxMin.actualizaMaxMin (poblacion, codPueblo, num[0], num[1]);
      res = true;
    } catch (MalformedURLException mue) {
      mue.printStackTrace();
    } catch (IOException ioe) {
      //      ioe.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (IOException ioe) {
        // nothing to see here
      }
    }
    return res;
  }

  // --------------------------------------------------------------------------
  public static boolean ProcesaPuebloParalela (String fecha, int codPueblo, PuebloMaximaMinimaParalela MaxMin,
                                       boolean imprime) {
    URL            url;
    InputStream    is = null;
    BufferedReader br;
    String         line, poblacion = new String (), provincia = new String ();
    int            state, num[]=new int[2];
    boolean        res = false;

    // Procesamiento de la informacion XML asociada a codPueblo
    // Actualizacion de MaxMin de acuerdo a los valores obtenidos
    try {
      String urlStr = "http://www.aemet.es/xml/municipios/localidad_" +
              String.format("%05d",codPueblo)+ ".xml";
      url = new URL(urlStr);
      is  = url.openStream();  // throws an IOException
      br  = new BufferedReader(new InputStreamReader(is));
      if (imprime) System.out.println(urlStr);

      state = 0;
      while (((line = br.readLine()) != null) && (state < 6)) {
        //        System.out.println (line);
        if ((state == 0) && (line.contains ("nombre"))) {
          poblacion=line.split(">")[1].split("<")[0].split("/")[0];
          state++;
        } else if ((state == 1) && (line.contains ("provincia"))) {
          provincia=line.split(">")[1].split("<")[0].split("/")[0];
          state++;
        } else if ((state == 2) && (line.contains (fecha))) {
          state++;
        } else if ((state == 3) && (line.contains ("temperatura"))) {
          state++;
        } else if ((state > 3) && ((line.contains ("maxima")) || (line.contains ("minima")))) {
          num[state-4] = Integer.parseInt (line.split(">")[1].split("<")[0]);
          state++;
        }
      }
      // System.out.println("(" + codPueblo + ") " + poblacion + "(" + provincia + ") => " +
      //                    "(" + num[0] + " , " + num[1] + ")");
      MaxMin.actualizaMaxMin (poblacion, codPueblo, num[0], num[1]);
      res = true;
    } catch (MalformedURLException mue) {
      mue.printStackTrace();
    } catch (IOException ioe) {
      //      ioe.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (IOException ioe) {
        // nothing to see here
      }
    }
    return res;
  }
}

// ============================================================================
class PuebloMaximaMinima {
  // ============================================================================
  String poblacion;
  int    codigo, max, min;


  // --------------------------------------------------------------------------
  public PuebloMaximaMinima() {
    poblacion = null;
    codigo    = -1;
    max       = -1;
    min       = -1;
  }

  // --------------------------------------------------------------------------
  public void actualizaMaxMin( String poblacion, int codigo, int max, int min ) {
    if ((this.poblacion == null) || ((this.max-this.min) < (max-min)) ||
        (((this.max-this.min) == (max-min)) && (this.min > min)) ||
        (((this.max-this.min) == (max-min)) && (this.min == min) && (this.codigo > codigo))
        ) {
      //      (((this.max-this.min) == (max-min)) && (this.max < max))) {
      this.poblacion = poblacion;
      this.codigo = codigo;
      this.max = max;
      this.min = min;
    }
  }

  // --------------------------------------------------------------------------
  public String damePueblo() {
    return this.poblacion + "(" + this.codigo + ")";
  }

  // --------------------------------------------------------------------------
  public int dameCodigo() {
    return this.codigo;
  }

  // --------------------------------------------------------------------------
  public int dameTemperaturaMaxima() {
    return this.max;
  }

  // --------------------------------------------------------------------------
  public int dameTemperaturaMinima() {
    return this.min;
  }
}

// ============================================================================
class PuebloMaximaMinimaParalela {
  // ============================================================================
  String poblacion;
  int    codigo, max, min;


  // --------------------------------------------------------------------------
  public PuebloMaximaMinimaParalela() {
    poblacion = null;
    codigo    = -1;
    max       = -1;
    min       = -1;
  }

  // --------------------------------------------------------------------------
  public synchronized void actualizaMaxMin( String poblacion, int codigo, int max, int min ) {
    if ((this.poblacion == null) || ((this.max-this.min) < (max-min)) ||
            (((this.max-this.min) == (max-min)) && (this.min > min)) ||
            (((this.max-this.min) == (max-min)) && (this.min == min) && (this.codigo > codigo))
    ) {
      //      (((this.max-this.min) == (max-min)) && (this.max < max))) {
      this.poblacion = poblacion;
      this.codigo = codigo;
      this.max = max;
      this.min = min;
    }
  }

  // --------------------------------------------------------------------------
  public synchronized String damePueblo() {
    return this.poblacion + "(" + this.codigo + ")";
  }

  // --------------------------------------------------------------------------
  public synchronized int dameCodigo() {
    return this.codigo;
  }

  // --------------------------------------------------------------------------
  public synchronized int dameTemperaturaMaxima() {
    return this.max;
  }

  // --------------------------------------------------------------------------
  public synchronized int dameTemperaturaMinima() {
    return this.min;
  }
}
// ============================================================================
  class TareaEnColaGestionPropia {
        boolean esVeneno ;
        int codPueblo ;

  public TareaEnColaGestionPropia(boolean esVeneno, int codPueblo) {
    this.esVeneno = esVeneno;
    this.codPueblo = codPueblo;
  }

  public void procesaPueblo(String fecha, int codPueblo, PuebloMaximaMinimaParalela MaxMin) {
    EjemploTemperaturaProvincia.ProcesaPuebloParalela(fecha, codPueblo, MaxMin, false);
  }
}

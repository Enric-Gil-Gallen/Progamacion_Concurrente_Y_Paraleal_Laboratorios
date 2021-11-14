package practica_7;

import java.util.concurrent.ArrayBlockingQueue;


public class Hebra_1_GestionPropia extends Thread{
    final ArrayBlockingQueue<TareaEnColaGestionPropia> cola;
    final String fecha;
    final PuebloMaximaMinimaParalela MaxMin;

    public Hebra_1_GestionPropia(ArrayBlockingQueue<TareaEnColaGestionPropia> cola, String fecha, PuebloMaximaMinimaParalela maxMin) {
        this.cola = cola;
        this.fecha = fecha;
        MaxMin = maxMin;
    }

    public void run(){
        try {
            TareaEnColaGestionPropia tarea =  cola.take();

            while (!tarea.esVeneno){

                tarea.procesaPueblo(fecha, tarea.codPueblo, MaxMin);
                tarea = cola.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

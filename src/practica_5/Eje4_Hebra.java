package practica_5;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class Eje4_Hebra extends Thread {
    final CanvasCampoTiro1a cnvCampoTiro;
    final LinkedBlockingQueue<NuevoDisparo1a> lD;
    final ArrayList<Proyectil> lP = new ArrayList<Proyectil>();
    Point objetivo;


    public Eje4_Hebra(LinkedBlockingQueue<NuevoDisparo1a> lD, CanvasCampoTiro1a cnvCampoTiro, Point objetivo) {
        this.lD = lD;
        this.cnvCampoTiro = cnvCampoTiro;
        this.objetivo = objetivo;
    }


    public void run() {
        NuevoDisparo1a disparo = null;
        int contador = 0;

        while (true) {
            while ((lP.isEmpty()) || (!lD.isEmpty())) {
                try {
                    disparo = lD.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lP.add(new Proyectil(disparo.velocidadInicial, disparo.anguloInicial, cnvCampoTiro));
            }


            Proyectil p;
            boolean impactado = false;
            for (int i = 0; i < lP.size(); i++) {
                p = lP.get(i);

                p.imprimeEstadoProyectilEnConsola();

                // Mueve el proyectil p.
                p.mueveUnIncremental();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Dibuja el proyectil p.
                        p.actualizaDibujoProyectil();
                    }
                });


                // Comprueba si el proyectil p ha impactado o continua en vuelo.
                impactado = determinaEstadoProyectil(p, objetivo);

                try {
                    Thread.sleep(1L);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (impactado){
                    lP.remove(i);
                    i--;
                }
            }
        }
    }

    public boolean determinaEstadoProyectil(Proyectil p, Point objetivo) {
        // Devuelve cierto si el proyectil ha impactado contra el suelo o contra
        // el objetivo.
        boolean impactado;
        String mensaje;

        if ((p.intPosX == objetivo.x) && (p.intPosY == objetivo.y)) {
            // El proyectil ha acertado el objetivo.
            impactado = true;


        } else if ((p.intPosY <= 0) && (p.velY < 0.0)) {
            // El proyectil ha impactado contra el suelo, pero no ha acertado.
            impactado = true;
        } else {
            // El proyectil continua en vuelo.
            impactado = false;
        }
        return impactado;
    }
}


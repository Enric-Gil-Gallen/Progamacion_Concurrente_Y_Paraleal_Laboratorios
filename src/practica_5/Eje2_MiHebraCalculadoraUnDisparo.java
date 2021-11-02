package practica_5;

import javax.swing.*;
import java.awt.*;

public class Eje2_MiHebraCalculadoraUnDisparo extends Thread{
    CanvasCampoTiro1a cnvCampoTiro;
    JTextField txfInformacion;
    NuevoDisparo1a d;
    Point objetivo;

    public Eje2_MiHebraCalculadoraUnDisparo(CanvasCampoTiro1a cnvCampoTiro, JTextField txfInformacion, NuevoDisparo1a d, Point objetivo) {
        this.cnvCampoTiro = cnvCampoTiro;
        this.txfInformacion = txfInformacion;
        this.d = d;
        this.objetivo = objetivo;
    }

    public void run(){
        Proyectil p;
        boolean      impactado;

        p = new Proyectil( d.velocidadInicial, d.anguloInicial, cnvCampoTiro );
        impactado = false;

        while( ! impactado ) {
            // Muestra en pantalla los datos del proyectil p.
            p.imprimeEstadoProyectilEnConsola();

            // Mueve el proyectil p.
            p.mueveUnIncremental();

            // Dibuja el proyectil p.
            p.actualizaDibujoProyectil();

            // Comprueba si el proyectil p ha impactado o continua en vuelo.
            impactado = determinaEstadoProyectil( p, objetivo );

            try {
                Thread.sleep( 1L );
            } catch( InterruptedException ex ) {
                ex.printStackTrace();
            }
        }

    }

    public boolean determinaEstadoProyectil(Proyectil p, Point objetivo ) {
        // Devuelve cierto si el proyectil ha impactado contra el suelo o contra
        // el objetivo.
        boolean  impactado;
        String   mensaje;

        if ( ( p.intPosX == objetivo.x )&&( p.intPosY == objetivo.y ) ) {
            // El proyectil ha acertado el objetivo.
            impactado = true;

            mensaje = " Destruido!!!";
            muestraMensajeEnCampoInformacion( mensaje );

        } else if( ( p.intPosY <= 0 )&&( p.velY < 0.0 ) ) {
            // El proyectil ha impactado contra el suelo, pero no ha acertado.
            impactado = true;

            mensaje = "Has fallado. Esta en " + objetivo.x + ". " +
                    "Has disparado a " + p.intPosX + ".";
            muestraMensajeEnCampoInformacion( mensaje );
        } else {
            // El proyectil continua en vuelo.
            impactado = false;
        }
        return impactado;
    }

    void muestraMensajeEnCampoInformacion( String mensaje ) {
        // Muestra mensaje en el cuadro de texto de informacion.

        String miMensaje = mensaje;
        txfInformacion.setText( miMensaje );
        System.out.println(mensaje);
    }

}

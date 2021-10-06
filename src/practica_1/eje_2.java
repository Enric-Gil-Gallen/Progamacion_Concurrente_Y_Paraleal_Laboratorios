package practica_1;

class MyThread extends Thread {
    int miId, ini, fin, sol;

    public MyThread(int miId, int ini, int fin) {
        this.miId = miId;
        this.ini = ini;
        this.fin = fin;
    }

    public void run() {
        for (int i = ini; i < fin; i++) {
            sol += i;
        }
        System.out.println("Mi Id: " + miId + " -- Suma total: " + sol);

    }
}

class eje_2 {
    public static void main(String args[]) throws InterruptedException {
        System.out.println("Inicio del programa");

        int hebra_0 = 0;
        int hebra_1 = 1;

        int iteracionesFinal = 1000000;

        MyThread h0 = new MyThread(hebra_0, 1, iteracionesFinal);
        MyThread h1 = new MyThread(hebra_1, 1, iteracionesFinal);

        h0.start();
        h0.join();
        h1.start();
        h1.join();

        System.out.println("Final del programa");

    }
}

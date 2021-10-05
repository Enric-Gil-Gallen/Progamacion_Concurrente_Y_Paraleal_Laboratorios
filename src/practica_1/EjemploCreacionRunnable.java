package practica_1;

class MiRun implements Runnable {
    int miId;

    public MiRun(int miId) {
        this.miId = miId;
    }

    public void run() {
        int num_impresinos = 1000;
        for (int i = 0; i < num_impresinos; i++) {
            System.out.println("Mi identificador de hebra es: " + miId);
        }
    }
}

class EjemploCreacionRunnable {
    public static void main(String args[]) {
        new Thread(new MiRun(0)).start();
        new Thread(new MiRun(1)).start();
    }
}

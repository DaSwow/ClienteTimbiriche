package pantallasproyectoarquitectura;

import conexion.ClientSideConnection;
import generacionTablero.Timbiriche;

public class SalaEspera extends javax.swing.JFrame implements Runnable {

    private ClientSideConnection cliente;
    private int cantidadMax;
    
    public SalaEspera(String n, ClientSideConnection c) {
        cliente = c;
        initComponents();
        esperarSalaLlena();
    }

    public void esperarSalaLlena() {
        Thread hilo = new Thread(this);
        hilo.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        etiquetaEspera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        etiquetaEspera.setText("Esperando que lleguen todos los jugadores");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(etiquetaEspera)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(etiquetaEspera)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void IniciarPartida() {

        int tamanio = 0;
        
        switch (cantidadMax) {
            case 2:
                tamanio = 10;
                break;
            case 3:
                tamanio = 20;
                break;
            case 4:
                tamanio = 20;
                break;
        }

        Timbiriche pj = new Timbiriche(cliente, tamanio + 1);
        pj.setVisible(true);
        this.dispose();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaEspera;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        cantidadMax=cliente.getCantidadMaximaJugadores();
        while (true) {
           
            int jugadoresActuales = cliente.leerJugadoresConectados();

            if (cantidadMax == jugadoresActuales) {
                if (cantidadMax==3 && jugadoresActuales != 3) {
                    cliente.saltarLinea();
                }
                else if(cantidadMax==4 && jugadoresActuales!=4){
                    cliente.saltarLinea();
                }
                
                IniciarPartida();

                break;
            } else if (jugadoresActuales <= 3) {
                cliente.saltarLinea();
            }
        }
    }

}

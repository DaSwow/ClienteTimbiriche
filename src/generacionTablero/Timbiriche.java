package generacionTablero;

import conexion.ClientSideConnection;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Timbiriche extends JFrame implements MouseMotionListener, MouseListener, Runnable {

    public int cantidadJugadores;

    public static int cantidadPuntos;
    public static int espacioEntrePuntos;
    public static int tamanioPuntos;

    public static final Integer jugadorUno = 1;
    public static final Integer jugadorDos = 2;
    public static final Integer jugadorTres = 3;
    public static final Integer jugadorCuatro = 4;

    public static final Color colorUno = Color.BLUE;
    public static final Color colorDos = Color.GREEN;
    public static final Color colorTres = Color.CYAN;
    public static final Color colorCuatro = Color.RED;

    public static ArrayList<Integer> jugadores = new ArrayList<Integer>();
    public static ArrayList<Integer> jugadoresOrdenados = new ArrayList<Integer>();

    private ConnectionSprite[] conexionesHorizontales;
    private ConnectionSprite[] conexionesVerticales;
    private BoxSprite[] cajas;
    private Sprite[] puntos;

    private Dimension dim;

    private int clickx;
    private int clicky;

    private int mousex;
    private int mousey;

    private int centrox;
    private int centroy;

    private int tamanioLadoTablero;
    private int espacio;
    private int jugadorActivo;

    private ClientSideConnection cliente;
    private boolean turno = false;

    public Timbiriche(ClientSideConnection c, int tamanio) {
        super("Timbiriche");
        cliente = c;
       
        setLayout(new CardLayout(espacio, espacio));
        setSize(1400, 1000);
        setFont(new Font("Courier", Font.BOLD, 20));
        setResizable(false);
        switch (tamanio - 1) {
            case 10:
                cantidadJugadores = 2;
                espacioEntrePuntos = 65;
                tamanioPuntos = 10;
                break;
            case 20:
                cantidadJugadores = 3;
                espacioEntrePuntos = 30;
                tamanioPuntos = 10;
                break;

        }
        cantidadPuntos = tamanio;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(this);
        addMouseMotionListener(this);

        cargarPropiedades();
        generarPuntos();
        setVisible(true);
        Thread partida=new Thread(this);
        partida.start();

    }

    private void cargarPropiedades() {
        jugadores.add(jugadorUno);
        jugadores.add(jugadorDos);
        if (cantidadJugadores >= 3) {
            jugadores.add(jugadorTres);
            if (cantidadJugadores == 4) {
                jugadores.add(jugadorCuatro);
            }
        }
        Collections.shuffle(jugadores);

        clickx = 0;
        clicky = 0;
        mousex = 0;
        mousey = 0;

        dim = getSize();
        centrox = dim.width / 2;
        centroy = (dim.height) / 2;

        tamanioLadoTablero = cantidadPuntos * tamanioPuntos + (cantidadPuntos - 1) * espacioEntrePuntos;
        espacio = tamanioPuntos + espacioEntrePuntos;
    }

    private void generarConexiones() {

        conexionesHorizontales = new ConnectionSprite[(cantidadPuntos - 1) * cantidadPuntos];
        conexionesVerticales = new ConnectionSprite[(cantidadPuntos - 1) * cantidadPuntos];

        for (int i = 0; i < conexionesHorizontales.length; i++) {
            int columnasx = i % (cantidadPuntos - 1);
            int filasx = i / (cantidadPuntos - 1);
            int horx = centrox - tamanioLadoTablero / 2 + tamanioPuntos + columnasx * espacio;
            int hory = centroy - tamanioLadoTablero / 2 + filasx * espacio;
            conexionesHorizontales[i] = ConnectionSprite.createConnection(ConnectionSprite.HORZ_CONN, horx, hory);

            int columnasy = i % cantidadPuntos;
            int filasy = i / cantidadPuntos;
            int vertx = centrox - tamanioLadoTablero / 2 + columnasy * espacio;
            int verty = centroy - tamanioLadoTablero / 2 + tamanioPuntos + filasy * espacio;
            conexionesVerticales[i] = ConnectionSprite.createConnection(ConnectionSprite.VERT_CONN, vertx, verty);
        }
    }

    private void generarCajas() {

        cajas = new BoxSprite[(cantidadPuntos - 1) * (cantidadPuntos - 1)];

        for (int i = 0; i < cajas.length; i++) {
            int cols = i % (cantidadPuntos - 1);
            int rows = i / (cantidadPuntos - 1);

            int boxx = centrox - tamanioLadoTablero / 2 + tamanioPuntos + cols * espacio;
            int boxy = centroy - tamanioLadoTablero / 2 + tamanioPuntos + rows * espacio;

            ConnectionSprite[] horConn = new ConnectionSprite[2];
            horConn[0] = conexionesHorizontales[i];
            horConn[1] = conexionesHorizontales[i + (cantidadPuntos - 1)];

            ConnectionSprite[] verConn = new ConnectionSprite[2];		//	This only works if the verticalConnections were put into the array rows then columns
            verConn[0] = conexionesVerticales[i + rows];
            verConn[1] = conexionesVerticales[i + rows + 1];

            cajas[i] = BoxSprite.createBox(boxx, boxy, horConn, verConn);
        }
    }

    private void generarPuntos() {
        puntos = new Sprite[cantidadPuntos * cantidadPuntos];
        for (int rows = 0; rows < cantidadPuntos; rows++) {
            for (int cols = 0; cols < cantidadPuntos; cols++) {
                Sprite dot = new Sprite();
                dot.width = tamanioPuntos;
                dot.height = tamanioPuntos;
                dot.x = centrox - tamanioLadoTablero / 2 + cols * espacio;
                dot.y = centroy - tamanioLadoTablero / 2 + rows * espacio;
                dot.shape.addPoint(-tamanioPuntos / 2, -tamanioPuntos / 2);
                dot.shape.addPoint(-tamanioPuntos / 2, tamanioPuntos / 2);
                dot.shape.addPoint(tamanioPuntos / 2, tamanioPuntos / 2);
                dot.shape.addPoint(tamanioPuntos / 2, -tamanioPuntos / 2);
                int index = rows * cantidadPuntos + cols;
                puntos[index] = dot;
            }
        }
    }

    private void empezarJuego() {
       
            jugadorActivo=jugadorUno;
//            try {
//                turno = cliente.getTurnoValido();
//            } catch (IOException | ClassNotFoundException ex) {
//                Logger.getLogger(Timbiriche.class.getName()).log(Level.SEVERE, null, ex);
//            }
            generarConexiones();
            generarCajas();
       
    }

    private ConnectionSprite getConexion(int x, int y) {
        for (int i = 0; i < conexionesHorizontales.length; i++) {
            if (conexionesHorizontales[i].containsPoint(x, y)) {
                return conexionesHorizontales[i];
            }
        }
        for (int i = 0; i < conexionesVerticales.length; i++) {
            if (conexionesVerticales[i].containsPoint(x, y)) {
                return conexionesVerticales[i];
            }
        }
        return null;
    }

    private boolean[] getEstaoCaja() {
        boolean[] estado = new boolean[cajas.length];
        for (int i = 0; i < estado.length; i++) {
            estado[i] = cajas[i].isBoxed();
        }
        return estado;
    }

    private int[] calcularPuntajes() {
        int[] scores = {0, 0, 0, 0};

        for (int i = 0; i < cajas.length; i++) {
            if (cajas[i].isBoxed() && cajas[i].player != 0) {
                scores[cajas[i].player - 1]++;
            }
        }
        return scores;
    }

    private boolean realizarConexionPuntos(ConnectionSprite conexion) {
        boolean newBox = false;

        boolean[] estadoCajaAntesDeCoenxion = getEstaoCaja();	//	The two boolean arrays are used to see if a new box was created after the connection was made

        conexion.connectionMade = true;

        boolean[] estadoCajaDespuesDeConexion = getEstaoCaja();

        for (int i = 0; i < cajas.length; i++) {
            if (estadoCajaDespuesDeConexion[i] != estadoCajaAntesDeCoenxion[i]) {
                newBox = true;
                cajas[i].player = jugadorActivo;
            }
        }

        if (!newBox) {

            if (jugadorActivo == jugadores.get(0)) {
                jugadorActivo = jugadores.get(1);
            } else if (jugadorActivo == jugadores.get(1)) {

                if (cantidadJugadores == 2) {
                    jugadorActivo = jugadores.get(0);
                } else if (cantidadJugadores >= 3) {
                    jugadorActivo = jugadores.get(2);
                }

            } else if (cantidadJugadores >= 3 && jugadorActivo == jugadores.get(2)) {
                if (cantidadJugadores == 3) {
                    jugadorActivo = jugadores.get(0);
                } else {
                    jugadorActivo = jugadores.get(3);
                }
            } else if (cantidadJugadores >= 3 && jugadorActivo == jugadores.get(3)) {
                jugadorActivo = jugadores.get(0);
            }
        }

        revisarJuegoTerminado();

        return newBox;
    }

    private void revisarJuegoTerminado() {
        int[] puntajes = calcularPuntajes();
        if ((puntajes[0] + puntajes[1] + puntajes[2] + puntajes[3]) == ((cantidadPuntos - 1) * (cantidadPuntos - 1))) {
            JOptionPane.showMessageDialog(this, "Jugador 1: " + puntajes[0] + "\nJugador 2: " + puntajes[1] + "\nJugador 3: " + puntajes[2] + "\nJugador 4: " + puntajes[3], "Fin del juego", JOptionPane.PLAIN_MESSAGE);
            empezarJuego();
            repaint();
        }
    }

    private void handleClick() {
        ConnectionSprite connection = getConexion(clickx, clicky);
        if (connection == null) {
            return;
        }
        if (!connection.connectionMade) {
            
            realizarConexionPuntos(connection);
        }
        repaint();
    }

    public void mouseMoved(MouseEvent event) {
        mousex = event.getX();
        mousey = event.getY();
        repaint();
    }

    public void mouseDragged(MouseEvent event) {
        mouseMoved(event);
    }

    public void mouseClicked(MouseEvent event) {
        clickx = event.getX();
        clicky = event.getY();
        handleClick();
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    private void pintarFondo(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, dim.width, dim.height);
    }

    private void pintarPuntos(Graphics g) {
        for (int i = 0; i < puntos.length; i++) {
            puntos[i].render(g);
        }
    }

    private void pintarConexiones(Graphics g) {
        for (int i = 0; i < conexionesHorizontales.length; i++) {

            if (!conexionesHorizontales[i].connectionMade) {
                if (conexionesHorizontales[i].containsPoint(mousex, mousey)) {
                    conexionesHorizontales[i].color = Color.BLACK;

                } else {
                    conexionesHorizontales[i].color = Color.WHITE;
                }

            }

            conexionesHorizontales[i].render(g);
        }

        for (int i = 0; i < conexionesVerticales.length; i++) {

            if (!conexionesVerticales[i].connectionMade) {
                if (conexionesVerticales[i].containsPoint(mousex, mousey)) {

                    conexionesVerticales[i].color = Color.BLACK;

                } else {
                    conexionesVerticales[i].color = Color.WHITE;
                }
            }
            conexionesVerticales[i].render(g);

        }
    }

    public void pintarCaja(Graphics g) {
        for (int i = 0; i < cajas.length; i++) {
            if (cajas[i].isBoxed()) {
                if (cajas[i].player == jugadorUno) {
                    cajas[i].color = colorUno;
                } else if (cajas[i].player == jugadorDos) {
                    cajas[i].color = colorDos;
                } else if (cajas[i].player == jugadorTres) {
                    cajas[i].color = colorTres;
                } else if (cajas[i].player == jugadorCuatro) {
                    cajas[i].color = colorCuatro;
                }
            } else {
                cajas[i].color = Color.WHITE;
            }

            cajas[i].render(g);

        }
    }

    public void pintarEstado(Graphics g) {
        int[] scores = calcularPuntajes();
        String status = "Es el turno del jugador " + jugadorActivo;
        String status2 = "Jugador 1: " + scores[0];
        String status3 = "Jugador 2: " + scores[1];
        String status4 = null;
        String status5 = null;
        if (cantidadJugadores >= 3) {
            status4 = "Jugador 3: " + scores[2];
            if (cantidadJugadores == 4) {
                status5 = "Jugador 4: " + scores[3];
            }
        }

        g.setColor(Color.BLACK);
        g.drawString(status, 10, dim.height - 100);

        g.setColor(colorUno);
        g.drawString(status2, 10, dim.height - 80);

        g.setColor(colorDos);
        g.drawString(status3, 10, dim.height - 60);
        if (cantidadJugadores >= 3) {
            g.setColor(colorTres);
            g.drawString(status4, 10, dim.height - 40);
            if (cantidadJugadores == 4) {
                g.setColor(colorCuatro);
                g.drawString(status5, 10, dim.height - 20);
            }
        }

    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        //	The double buffer technique is not really necessarry because there is no animation

        Image bufferImage = createImage(dim.width, dim.height);
        Graphics bufferGraphics = bufferImage.getGraphics();

        pintarFondo(bufferGraphics);
        pintarPuntos(bufferGraphics);
        pintarConexiones(bufferGraphics);
        pintarCaja(bufferGraphics);
        pintarEstado(bufferGraphics);

        g.drawImage(bufferImage, 0, 0, null);
    }

    @Override
    public void run() {
        empezarJuego();
    }

}

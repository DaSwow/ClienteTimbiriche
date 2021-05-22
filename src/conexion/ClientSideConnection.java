package conexion;

import generacionTablero.ConnectionSprite;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author carls
 */
public class ClientSideConnection {

    private static  ClientSideConnection csc;

    private Socket socket;
    private ObjectOutputStream dos;
    private ObjectInputStream dis;

    private ClientSideConnection() {
        try {
            socket = new Socket("localhost", 9876);
            dos = new ObjectOutputStream(socket.getOutputStream());
            dos.flush();
            dis = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion del cliente inicializada.");
        } catch (IOException e) {
            System.out.println("Excepcion IO por parte del constructor.");
            JOptionPane.showMessageDialog(null, "No se pudo establecer una conexion con el servidor");
            System.exit(0);
        }
    }

    public static ClientSideConnection getSingleSideConnection() {
        if (csc == null) {
            csc = new ClientSideConnection();
        }
        return csc;
    }

    public void conectarJugador(String nombre) {
        try {
            dos.writeObject(nombre);
            dos.flush();
        } catch (IOException e) {
            System.out.println("Excepcion IO por parte del constructor.");
        }
    }

    public void setCantidadMaxJugadores(Integer cantidad) {
        try {
            dos.writeObject(cantidad);
            dos.flush();
        } catch (IOException e) {
        }
    }

    public int getCantidadMaxJugadores() {
        try {
            Integer jugadores = (Integer) dis.readObject();
            return jugadores;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al recuperar la lista de jugadores.");
        }
        return -1;
    }

    public Integer leerJugadoresConectados() {
        try {
            Integer jugadores = (Integer) dis.readObject();
            return jugadores;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al recuperar la lista de jugadores.");
        }
        return null;
    }

    public void saltarLinea() {
        try {
            dis.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getCantidadMaximaJugadores() {
        try {
            return ((Integer) dis.readObject());

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int getTurnoValido() throws IOException, ClassNotFoundException {
        try {
            return (Integer) dis.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void sendJugada(int[] jugada) {
        try {
            dos.writeObject(jugada);
            dos.flush();

        } catch (IOException ex) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int[] getJugada() {
        try {
            Object object = dis.readObject();
            return (int[]) object;

        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, e);
        } catch (ClassCastException e) {
            System.out.println();
        }
        return null;
    }

    public int getTurno() {
        try {
            return (int) dis.readObject();

        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(ClientSideConnection.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

}

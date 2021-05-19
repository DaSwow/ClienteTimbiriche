/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

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

    private Socket socket;
    private ObjectOutputStream dos;
    private ObjectInputStream dis;

    public ClientSideConnection() {

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

    public ArrayList leerJugadoresConectados() {
        try {
            ArrayList<String> jugadores = (ArrayList) dis.readObject();
            return jugadores;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al recuperar la lista de jugadores.");
        }
        return null;
    }

    public int getCantidadMaximaJugadores() {
        try {
            return (int)((ArrayList) dis.readObject()).get(0);
        } catch (IOException ex) {
            Logger.getLogger(ClientSideConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientSideConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

}
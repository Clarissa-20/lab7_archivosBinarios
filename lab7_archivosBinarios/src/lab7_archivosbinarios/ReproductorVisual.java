/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_archivosbinarios;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javazoom.jl.player.Player;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author HP
 */
public class ReproductorVisual extends JFrame implements AccionesReproductor {
    
    private ArrayList<Cancion> playList;
    private Cancion cancionSeleccionada;
    private DefaultListModel<String> modeloLista;
    private JList<String> lista;
    private JLabel imagen, estado;
    private JSlider barraProgreso;
    private Timer temporizador;

    private Reproductor motor = new Reproductor();

    public ReproductorVisual() {
        playList = GestionDatos.cargarPlayList();
        initComponents();
        configurarTimer();
    }

    private void configurarTimer() {
        temporizador = new Timer(1000, e -> {
            if (motor.estaReproduciendo()) {
                barraProgreso.setValue(barraProgreso.getValue() + 1);
            }
        });
    }

    private void initComponents() {
        setTitle("REPRODUCTOR DE MUSICA");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(15, 15));

        modeloLista = new DefaultListModel<>();
        actualizarLista();
        lista = new JList<>(modeloLista);
        lista.setBackground(new Color(20, 20, 20));
        lista.setForeground(new Color(0, 255, 0));
        lista.setSelectionBackground(new Color(0, 80, 0));
        lista.setSelectionForeground(Color.WHITE);
        lista.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0)));
        scroll.setPreferredSize(new Dimension(280, 0));
        add(scroll, BorderLayout.WEST);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBackground(Color.BLACK);

        imagen = new JLabel("SELECCIONE UNA PISTA", SwingConstants.CENTER);
        imagen.setForeground(new Color(0, 255, 0));
        imagen.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0), 2));
        imagen.setPreferredSize(new Dimension(450, 450));

        estado = new JLabel("SISTEMA: STANDBY", SwingConstants.CENTER);
        estado.setForeground(new Color(0, 255, 0));
        estado.setFont(new Font("Monospaced", Font.BOLD, 14));

        panelCentral.add(imagen, BorderLayout.CENTER);
        panelCentral.add(estado, BorderLayout.SOUTH);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(Color.BLACK);

        barraProgreso = new JSlider(0, 100, 0);
        barraProgreso.setBackground(Color.BLACK);
        barraProgreso.setForeground(new Color(0, 255, 0));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.BLACK);

        JButton btnPlay = crearBoton("PLAY");
        JButton btnPause = crearBoton("PAUSE");
        JButton btnStop = crearBoton("STOP");
        JButton btnAdd = crearBoton("ADD");
        JButton btnRemove = crearBoton("REMOVE");

        panelBotones.add(btnPlay);
        panelBotones.add(btnPause);
        panelBotones.add(btnStop);
        panelBotones.add(btnAdd);
        panelBotones.add(btnRemove);

        panelSur.add(barraProgreso, BorderLayout.NORTH);
        panelSur.add(panelBotones, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        btnPlay.addActionListener(e -> play());
        btnPause.addActionListener(e -> pause());
        btnStop.addActionListener(e -> stop());
        btnAdd.addActionListener(e -> mostrarVentanaAgregar());

        btnRemove.addActionListener(e -> {
            int idx = lista.getSelectedIndex();
            if (idx != -1) {
                playList.remove(idx);
                GestionDatos.guardarPlayList(playList);
                actualizarLista();
            }
        });

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarPreview();
            }
        });
    }

    @Override
    public void play() {
        if (cancionSeleccionada != null) {
            try {
                motor.play(cancionSeleccionada.getRutaArchivo());
                estado.setText("REPRODUCIENDO...");
                temporizador.start();
            } catch (Exception e) {
                estado.setText("ERROR DE SISTEMA");
            }
        }
    }

    @Override
    public void pause() {
        motor.pause();
        if (motor.estaReproduciendo()) {
            temporizador.start();
            estado.setText("REANUDADO");
        } else {
            temporizador.stop();
            estado.setText("PAUSADO EN PUNTO ACTUAL");
        }
    }

    @Override
    public void stop() {
        motor.stop();
        temporizador.stop();
        barraProgreso.setValue(0); 
        estado.setText("DETENIDO - RESETEADO");
    }

    private void mostrarVentanaAgregar() {
        JDialog win = new JDialog(this, "AGREGAR NUEVA CANCIÓN", true);
        win.setSize(600, 500);
        win.getContentPane().setBackground(Color.BLACK);
        win.setLayout(new BorderLayout(10, 10));
        win.setLocationRelativeTo(this);

        JPanel pPrincipal = new JPanel(new GridLayout(6, 2, 10, 10));
        pPrincipal.setBackground(Color.BLACK);
        pPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtT = crearFieldDark();
        JTextField txtA = crearFieldDark();
        JComboBox<Genero> cbG = new JComboBox<>(Genero.values());
        cbG.setBackground(Color.BLACK);
        cbG.setForeground(new Color(0, 255, 0));

        JButton btnMP3 = crearBoton("Elegir MP3");
        JButton btnIMG = crearBoton("Elegir Imagen");
        JButton btnGuardar = crearBoton("Guardar Canción");

        JLabel lblMP3 = crearLabelDark("Ninguno...");
        JLabel lblIMG = crearLabelDark("Ninguna...");

        final String[] r = {"default.png", ""};

        btnMP3.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(win) == JFileChooser.APPROVE_OPTION) {
                r[1] = fc.getSelectedFile().getAbsolutePath();
                lblMP3.setText(fc.getSelectedFile().getName());
                txtT.setText(fc.getSelectedFile().getName().replace(".mp3", ""));
            }
        });

        btnIMG.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(win) == JFileChooser.APPROVE_OPTION) {
                r[0] = fc.getSelectedFile().getAbsolutePath();
                lblIMG.setText(fc.getSelectedFile().getName());
            }
        });

        btnGuardar.addActionListener(e -> {
            if (!r[1].isEmpty()) {
                Cancion nueva = new Cancion(txtT.getText(), txtA.getText(), 0.0, (Genero) cbG.getSelectedItem(), r[0], r[1]);
                playList.add(nueva);
                GestionDatos.guardarPlayList(playList);
                actualizarLista();
                win.dispose();
            }
        });

        pPrincipal.add(crearLabelDark("Título:"));
        pPrincipal.add(txtT);
        pPrincipal.add(crearLabelDark("Artista:"));
        pPrincipal.add(txtA);
        pPrincipal.add(crearLabelDark("Género:"));
        pPrincipal.add(cbG);
        pPrincipal.add(btnMP3);
        pPrincipal.add(lblMP3);
        pPrincipal.add(btnIMG);
        pPrincipal.add(lblIMG);
        pPrincipal.add(new JLabel(""));
        pPrincipal.add(btnGuardar);

        win.add(pPrincipal, BorderLayout.CENTER);
        win.setVisible(true);
    }

    private void actualizarPreview() {
        int idx = lista.getSelectedIndex();
        if (idx != -1) {
            cancionSeleccionada = playList.get(idx);
            ImageIcon icon = new ImageIcon(cancionSeleccionada.getRutaImagen());
            Image img = icon.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
            imagen.setText("");
        }
    }

    private void actualizarLista() {
        modeloLista.clear();
        for (Cancion c : playList) {
            modeloLista.addElement(c.getDetalles());
        }
    }

    private JButton crearBoton(String t) {
        JButton b = new JButton(t);
        b.setBackground(Color.BLACK);
        b.setForeground(new Color(0, 255, 0));
        b.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0)));
        b.setFont(new Font("Monospaced", Font.BOLD, 12));
        b.setFocusPainted(false);
        return b;
    }

    private JTextField crearFieldDark() {
        JTextField f = new JTextField();
        f.setBackground(new Color(30, 30, 30));
        f.setForeground(new Color(0, 255, 0));
        f.setCaretColor(Color.GREEN);
        f.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0)));
        return f;
    }

    private JLabel crearLabelDark(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Monospaced", Font.BOLD, 12));
        return l;
    }

}

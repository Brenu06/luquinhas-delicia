package com.example.mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MapaBrasilSwing extends JFrame {
    private Grafo grafoCapitais;
    private Map<String, Point> coordenadasCapitais = new HashMap<>();
    private JComboBox<String> comboOrigem;
    private JComboBox<String> comboDestino;
    private JPanel painelMapa;
    private java.util.List<String> caminhoAtual = new ArrayList<>();

    public MapaBrasilSwing() {
        setTitle("Mapa do Brasil - Menor Caminho entre Capitais");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        grafoCapitais = MenorCaminhoCapitais.getGrafoCapitais();
        configurarCoordenadas();

        comboOrigem = new JComboBox<>(grafoCapitais.getAdjacencias().keySet().toArray(new String[0]));
        comboDestino = new JComboBox<>(grafoCapitais.getAdjacencias().keySet().toArray(new String[0]));
        comboOrigem.setBounds(30, 20, 180, 25);
        comboDestino.setBounds(230, 20, 180, 25);

        JButton btnCalcular = new JButton("Calcular Caminho");
        btnCalcular.setBounds(430, 20, 160, 25);

        painelMapa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image mapa = new ImageIcon(getClass().getResource("/imagens/mapa_brasil.png")).getImage();
                g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);
                desenharMapa(g);
            }
        };

        painelMapa.setBounds(30, 60, 800, 600);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularECriarCaminho();
            }
        });

        add(comboOrigem);
        add(comboDestino);
        add(btnCalcular);
        add(painelMapa);
    }

    private void configurarCoordenadas() {
        coordenadasCapitais.put("Manaus", new Point(300, 120));
        coordenadasCapitais.put("Belem", new Point(450, 150));
        coordenadasCapitais.put("Porto Velho", new Point(250, 220));
        coordenadasCapitais.put("Rio Branco", new Point(200, 220));
        coordenadasCapitais.put("Macapa", new Point(500, 120));
        coordenadasCapitais.put("Boa Vista", new Point(350, 70));
        coordenadasCapitais.put("Palmas", new Point(400, 260));
        coordenadasCapitais.put("Sao Luis", new Point(500, 170));
        coordenadasCapitais.put("Teresina", new Point(520, 220));
        coordenadasCapitais.put("Fortaleza", new Point(580, 170));
        coordenadasCapitais.put("Natal", new Point(620, 190));
        coordenadasCapitais.put("Joao Pessoa", new Point(640, 210));
        coordenadasCapitais.put("Recife", new Point(630, 230));
        coordenadasCapitais.put("Maceio", new Point(600, 260));
        coordenadasCapitais.put("Aracaju", new Point(580, 280));
        coordenadasCapitais.put("Salvador", new Point(550, 300));
        coordenadasCapitais.put("Brasilia", new Point(450, 320));
        coordenadasCapitais.put("Goiania", new Point(420, 340));
        coordenadasCapitais.put("Cuiaba", new Point(350, 320));
        coordenadasCapitais.put("Campo Grande", new Point(380, 370));
        coordenadasCapitais.put("Sao Paulo", new Point(480, 390));
        coordenadasCapitais.put("Rio de Janeiro", new Point(520, 370));
        coordenadasCapitais.put("Belo Horizonte", new Point(500, 340));
        coordenadasCapitais.put("Vitoria", new Point(550, 330));
        coordenadasCapitais.put("Curitiba", new Point(450, 420));
        coordenadasCapitais.put("Florianopolis", new Point(480, 440));
        coordenadasCapitais.put("Porto Alegre", new Point(420, 470));
    }

    private void desenharMapa(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, painelMapa.getWidth(), painelMapa.getHeight());

        for (Map.Entry<String, Point> entry : coordenadasCapitais.entrySet()) {
            String capital = entry.getKey();
            Point p = entry.getValue();
            g.setColor(Color.BLUE);
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(capital, p.x + 8, p.y + 4);
        }

        if (caminhoAtual != null && caminhoAtual.size() > 1) {
            g.setColor(Color.RED);
            for (int i = 0; i < caminhoAtual.size() - 1; i++) {
                Point a = coordenadasCapitais.get(caminhoAtual.get(i));
                Point b = coordenadasCapitais.get(caminhoAtual.get(i + 1));
                g.drawLine(a.x, a.y, b.x, b.y);
            }
            Point origem = coordenadasCapitais.get(caminhoAtual.get(0));
            Point destino = coordenadasCapitais.get(caminhoAtual.get(caminhoAtual.size() - 1));
            g.setColor(Color.GREEN);
            g.fillOval(origem.x - 8, origem.y - 8, 16, 16);
            g.setColor(Color.RED);
            g.fillOval(destino.x - 8, destino.y - 8, 16, 16);
        }
    }

    private void calcularECriarCaminho() {
        String origem = (String) comboOrigem.getSelectedItem();
        String destino = (String) comboDestino.getSelectedItem();
        if (origem == null || destino == null) return;

        caminhoAtual = grafoCapitais.obterMenorCaminho(origem, destino);
        painelMapa.repaint();
    }

    public static void main(String[] args) {
        MenorCaminhoCapitais.inicializarGrafo();
        SwingUtilities.invokeLater(() -> {
            new MapaBrasilSwing().setVisible(true);
        });
    }
}

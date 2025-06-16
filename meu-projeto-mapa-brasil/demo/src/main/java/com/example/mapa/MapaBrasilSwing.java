package com.example.mapa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MapaBrasilSwing extends JFrame {
    private Grafo grafoCapitais;
    private Map<String, Point> coordenadasCapitais = new HashMap<>();
    private JComboBox<String> comboOrigem;
    private JComboBox<String> comboDestino;
    private JPanel painelMapa;
    private JLabel labelDistancia; // Novo atributo
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

        // Adicionar o label de distância
        labelDistancia = new JLabel("Distância: 0 km");
        labelDistancia.setBounds(600, 20, 200, 25);
        add(labelDistancia);

        painelMapa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Simplifica para usar apenas o caminho correto onde a imagem está
                String imagePath = "/mapa_brasil.jpg";
                
                try {
                    URL imageUrl = getClass().getResource(imagePath);
                    System.out.println("Tentando carregar imagem de: " + imagePath);
                    System.out.println("URL encontrada: " + imageUrl);
                    
                    if (imageUrl != null) {
                        Image mapa = new ImageIcon(imageUrl).getImage();
                        g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);
                        System.out.println("Imagem carregada com sucesso!");
                    } else {
                        System.err.println("Não foi possível encontrar a imagem em: " + imagePath);
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar a imagem: " + e.getMessage());
                }
                
                // Desenha os pontos e linhas por cima da imagem
                desenharPontosELinhas(g);
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
        // Norte
        coordenadasCapitais.put("Manaus", new Point(220, 150));
        coordenadasCapitais.put("Belem", new Point(450, 150));
        coordenadasCapitais.put("Porto Velho", new Point(200, 230));
        coordenadasCapitais.put("Rio Branco", new Point(100, 230));
        coordenadasCapitais.put("Macapa", new Point(450, 50));
        coordenadasCapitais.put("Boa Vista", new Point(250, 70));
        coordenadasCapitais.put("Palmas", new Point(500, 250));

        // Nordeste
        coordenadasCapitais.put("Sao Luis", new Point(570, 170));
        coordenadasCapitais.put("Teresina", new Point(610, 215));
        coordenadasCapitais.put("Fortaleza", new Point(700, 170));
        coordenadasCapitais.put("Natal", new Point(750, 180));
        coordenadasCapitais.put("Joao Pessoa", new Point(730, 200));
        coordenadasCapitais.put("Recife", new Point(720, 220));
        coordenadasCapitais.put("Maceio", new Point(740, 239));
        coordenadasCapitais.put("Aracaju", new Point(735, 255));
        coordenadasCapitais.put("Salvador", new Point(650, 270));

        // Centro-Oeste
        coordenadasCapitais.put("Brasilia", new Point(530, 325));
        coordenadasCapitais.put("Goiania", new Point(450, 345));
        coordenadasCapitais.put("Cuiaba", new Point(380, 370));
        coordenadasCapitais.put("Campo Grande", new Point(350, 270));

        // Sudeste
        coordenadasCapitais.put("Sao Paulo", new Point(500, 430));
        coordenadasCapitais.put("Rio de Janeiro", new Point(600, 430));
        coordenadasCapitais.put("Belo Horizonte", new Point(540, 370));
        coordenadasCapitais.put("Vitoria", new Point(660, 380));

        // Sul
        coordenadasCapitais.put("Curitiba", new Point(450, 450));
        coordenadasCapitais.put("Florianopolis", new Point(480, 500));
        coordenadasCapitais.put("Porto Alegre", new Point(420, 520));
    }

    private void desenharPontosELinhas(Graphics g) {
        // Desenha os pontos das capitais
        for (Map.Entry<String, Point> entry : coordenadasCapitais.entrySet()) {
            String capital = entry.getKey();
            Point p = entry.getValue();
            g.setColor(Color.BLUE);
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(capital, p.x + 8, p.y + 4);
        }

        // Desenha as linhas do caminho
        if (caminhoAtual != null && caminhoAtual.size() > 1) {
            g.setColor(Color.BLACK);
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
        double distancia = grafoCapitais.obterDistanciaTotal(origem, destino);
        labelDistancia.setText(String.format("Distância: %.0f km", distancia));
        System.out.println("Distância total: " + distancia + " km");
        System.out.println("Rotas percorridas:");
        for (int i = 0; i < caminhoAtual.size() - 1; i++) {
            String cidadeA = caminhoAtual.get(i);
            String cidadeB = caminhoAtual.get(i + 1);
            int dist = grafoCapitais.getAdjacencias().get(cidadeA).stream()
                .filter(a -> a.destino.equals(cidadeB))
                .findFirst()
                .map(a -> a.distancia)
                .orElse(0);
            System.out.println("De " + cidadeA + " para " + cidadeB + " (" + dist + " km)");
        }
        painelMapa.repaint();
    }

    public static void main(String[] args) {
        MenorCaminhoCapitais.inicializarGrafo();
        SwingUtilities.invokeLater(() -> {
            new MapaBrasilSwing().setVisible(true);
        });
    }
}

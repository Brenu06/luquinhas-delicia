package com.example.mapa;

import java.util.*;

public class Grafo {
    private final Map<String, List<Aresta>> adjacencias;

    public Grafo() {
        this.adjacencias = new HashMap<>();
    }

    public void adicionarVertice(String capital) {
        adjacencias.putIfAbsent(capital, new ArrayList<>());
    }

    public void adicionarAresta(String origem, String destino, int distancia) {
        adjacencias.putIfAbsent(origem, new ArrayList<>());
        adjacencias.putIfAbsent(destino, new ArrayList<>());
        adjacencias.get(origem).add(new Aresta(destino, distancia));
        adjacencias.get(destino).add(new Aresta(origem, distancia)); // Grafo não direcionado
    }

    // Método público para acessar as adjacências
    public Map<String, List<Aresta>> getAdjacencias() {
        return adjacencias;
    }

    public Map<String, Caminho> dijkstra(String origem) {
        Map<String, Caminho> caminhos = new HashMap<>();
        PriorityQueue<Caminho> filaPrioridade = new PriorityQueue<>();

        for (String capital : adjacencias.keySet()) {
            caminhos.put(capital, new Caminho(capital, Integer.MAX_VALUE, null));
        }

        caminhos.get(origem).distancia = 0;
        filaPrioridade.add(new Caminho(origem, 0, null));

        while (!filaPrioridade.isEmpty()) {
            Caminho atual = filaPrioridade.poll();
            String capitalAtual = atual.destino;

            for (Aresta vizinho : adjacencias.get(capitalAtual)) {
                int novaDistancia = caminhos.get(capitalAtual).distancia + vizinho.distancia;

                if (novaDistancia < caminhos.get(vizinho.destino).distancia) {
                    caminhos.get(vizinho.destino).distancia = novaDistancia;
                    caminhos.get(vizinho.destino).anterior = capitalAtual;
                    filaPrioridade.add(new Caminho(vizinho.destino, novaDistancia, capitalAtual));
                }
            }
        }

        return caminhos;
    }

    public List<String> obterMenorCaminho(String origem, String destino) {
        Map<String, Caminho> caminhos = dijkstra(origem);
        List<String> resultado = new ArrayList<>();
        String atual = destino;
        if (caminhos.get(destino).distancia == Integer.MAX_VALUE) {
            return resultado; // Caminho não existe
        }
        while (atual != null) {
            resultado.add(0, atual);
            atual = caminhos.get(atual).anterior;
        }
        return resultado;
    }

    public double obterDistanciaTotal(String origem, String destino) {
        Map<String, Caminho> caminhos = dijkstra(origem);
        int distancia = caminhos.get(destino).distancia;
        if (distancia == Integer.MAX_VALUE) return 0;
        return distancia;
    }

    public static class Aresta {
        String destino;
        int distancia;

        public Aresta(String destino, int distancia) {
            this.destino = destino;
            this.distancia = distancia;
        }
    }

    public static class Caminho implements Comparable<Caminho> {
        String destino;
        int distancia;
        String anterior;

        public Caminho(String destino, int distancia, String anterior) {
            this.destino = destino;
            this.distancia = distancia;
            this.anterior = anterior;
        }

        @Override
        public int compareTo(Caminho outro) {
            return Integer.compare(this.distancia, outro.distancia);
        }
    }
}





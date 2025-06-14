package com.example.mapa;

import java.util.*;

public class MenorCaminhoCapitais {
    private static Grafo grafoCapitais;
    private static Scanner scanner;

    public static void main(String[] args) {
        inicializarGrafo();
        scanner = new Scanner(System.in);

        System.out.println("Sistema de Cálculo de Menor Caminho entre Capitais Brasileiras");
        System.out.println("-------------------------------------------------------------");

         while (true) {
            exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (opcao) {
                case 1 -> calcularMenorCaminho();
                case 2 -> listarCapitais();
                case 3 -> {
                    System.out.println("Saindo do sistema...");
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    private static void exibirMenu() {
        System.out.println("\nMenu:");
        System.out.println("1 - Calcular menor caminho entre capitais");
        System.out.println("2 - Listar todas as capitais disponíveis");
        System.out.println("3 - Sair");
        System.out.print("Escolha uma opção: ");
    }


private static void calcularMenorCaminho() {
    System.out.print("\nDigite a capital de origem: ");
    String origem = scanner.nextLine();

    System.out.print("Digite a capital de destino: ");
    String destino = scanner.nextLine();

    if (!grafoCapitais.getAdjacencias().containsKey(origem) || !grafoCapitais.getAdjacencias().containsKey(destino)) {
        System.out.println("Uma ou ambas as capitais informadas não estão no sistema.");
        return;
    }

    List<String> caminho = grafoCapitais.obterMenorCaminho(origem, destino);
    double distancia = grafoCapitais.obterDistanciaTotal(origem, destino);

    if (caminho == null || caminho.isEmpty()) {
        System.out.println("\nNão existe caminho entre " + origem + " e " + destino);
    } else {
        System.out.println("\nResultado:");
        System.out.println("Menor caminho: " + String.join(" -> ", caminho));
        System.out.println("Distância total: " + distancia + " km");
        System.out.println("Rotas percorridas:");
        for (int i = 0; i < caminho.size() - 1; i++) {
            String cidadeA = caminho.get(i);
            String cidadeB = caminho.get(i + 1);
            // Descobre a distância entre as cidades
            int dist = grafoCapitais.getAdjacencias().get(cidadeA).stream()
                .filter(a -> a.destino.equals(cidadeB))
                .findFirst()
                .map(a -> a.distancia)
                .orElse(0);
            System.out.println("De " + cidadeA + " para " + cidadeB + " (" + dist + " km)");
        }
    }
}
     private static void listarCapitais() {
        System.out.println("\nCapitais disponíveis:");
        for (String capital : grafoCapitais.getAdjacencias().keySet()) {
            System.out.println("- " + capital);
        }
    }

      static void inicializarGrafo() {
    grafoCapitais = new Grafo();

    String[] capitais = {
        "Sao Paulo", "Rio de Janeiro", "Belo Horizonte", "Vitoria",
        "Salvador", "Fortaleza", "Recife", "Natal", "Teresina", "Aracaju",
        "Maceio", "Joao Pessoa", "Sao Luis",
        "Curitiba", "Porto Alegre", "Florianopolis",
        "Manaus", "Belem", "Porto Velho", "Palmas", "Rio Branco", "Macapa", "Boa Vista",
        "Goiania", "Brasilia", "Campo Grande", "Cuiaba"
    };

    for (String capital : capitais) {
        grafoCapitais.adicionarVertice(capital);
    }

    // Sudeste
    grafoCapitais.adicionarAresta("Sao Paulo", "Rio de Janeiro", 430);
    grafoCapitais.adicionarAresta("Rio de Janeiro", "Belo Horizonte", 437);
    grafoCapitais.adicionarAresta("Belo Horizonte", "Vitoria", 520);
    grafoCapitais.adicionarAresta("Rio de Janeiro", "Vitoria", 520);
    grafoCapitais.adicionarAresta("Sao Paulo", "Curitiba", 408);
    grafoCapitais.adicionarAresta("Sao Paulo", "Belo Horizonte", 586);

    // Sul
    grafoCapitais.adicionarAresta("Curitiba", "Florianopolis", 300);
    grafoCapitais.adicionarAresta("Florianopolis", "Porto Alegre", 476);

    // Nordeste (litoral, de sul para norte)
    grafoCapitais.adicionarAresta("Salvador", "Aracaju", 356);
    grafoCapitais.adicionarAresta("Aracaju", "Maceio", 294);
    grafoCapitais.adicionarAresta("Maceio", "Recife", 285);
    grafoCapitais.adicionarAresta("Recife", "Joao Pessoa", 120);
    grafoCapitais.adicionarAresta("Joao Pessoa", "Natal", 185);
    grafoCapitais.adicionarAresta("Natal", "Fortaleza", 537);

    // Nordeste (interior)
    grafoCapitais.adicionarAresta("Fortaleza", "Teresina", 634);
    grafoCapitais.adicionarAresta("Teresina", "Sao Luis", 446);

    // Nordeste-Norte
    grafoCapitais.adicionarAresta("Sao Luis", "Belem", 806);


    // Centro-Oeste
    grafoCapitais.adicionarAresta("Brasilia", "Goiania", 209);
    grafoCapitais.adicionarAresta("Goiania", "Cuiaba", 934);
    grafoCapitais.adicionarAresta("Goiania", "Campo Grande", 935);
    grafoCapitais.adicionarAresta("Campo Grande", "Cuiaba", 694);

    // Centro-Oeste-Norte
    grafoCapitais.adicionarAresta("Cuiaba", "Porto Velho", 1450);
    grafoCapitais.adicionarAresta("Porto Velho", "Rio Branco", 510);

    // Norte
    grafoCapitais.adicionarAresta("Porto Velho", "Manaus", 901);
    grafoCapitais.adicionarAresta("Manaus", "Boa Vista", 785);

    grafoCapitais.adicionarAresta("Manaus", "Macapa", 1500); // via balsa/barco1

    // Norte-Nordeste
    grafoCapitais.adicionarAresta("Belem", "Macapa", 606); // via balsa/barco
    grafoCapitais.adicionarAresta("Belem", "Palmas", 1600);

    // Centro-Oeste-Norte
    grafoCapitais.adicionarAresta("Palmas", "Goiania", 874);
    grafoCapitais.adicionarAresta("Palmas", "Cuiaba", 1330);

    // Ligações interestaduais importantes (para garantir conectividade realista)
    grafoCapitais.adicionarAresta("Brasilia", "Belo Horizonte", 716);
    grafoCapitais.adicionarAresta("Brasilia", "Goiania", 209);
    grafoCapitais.adicionarAresta("Brasilia", "Cuiaba", 1133);
    grafoCapitais.adicionarAresta("Brasilia", "Salvador", 1444);
    grafoCapitais.adicionarAresta("Brasilia", "Palmas", 973);
    grafoCapitais.adicionarAresta("Brasilia", "Campo Grande", 1134);

    // Sudeste-Nordeste
    grafoCapitais.adicionarAresta("Vitoria", "Salvador", 1080);
    grafoCapitais.adicionarAresta("Belo Horizonte", "Salvador", 1372);



    // Norte-Nordeste
    grafoCapitais.adicionarAresta("Belem", "Sao Luis", 946);

    // Outras ligações importantes para garantir conectividade
    grafoCapitais.adicionarAresta("Palmas", "Teresina", 1250);
    grafoCapitais.adicionarAresta("Palmas", "Sao Luis", 1370);

    // Exemplo de ligação para Macapá (via balsa para Belém)
    grafoCapitais.adicionarAresta("Macapa", "Belem", 606);
}
      public static Grafo getGrafoCapitais() {
        if (grafoCapitais == null) {
        inicializarGrafo();
    }
    return grafoCapitais;
}}

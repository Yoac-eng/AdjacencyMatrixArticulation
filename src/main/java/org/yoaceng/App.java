package org.yoaceng;

import java.util.Scanner;

import static org.yoaceng.GraphReader.readGraphFromAdjacencyListFile;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String FILEPATH = "graph.txt";
        final String NEWFILEPATH = "NewStructureFile.txt";
        Graph graph = GraphReader.readGraphToMatrixFromFile(FILEPATH);

        if (graph != null) {
            boolean running = true;
            while (running) {
                System.out.println();
                System.out.println("=============================== Sistema de gerenciamento de grafo ===============================");
                System.out.println("***IMPORTANTE: Edite o arquivo graph.txt na raiz do projeto com o grafo que deseja gerenciar***");
                System.out.println("Escolha uma opção:");
                System.out.println("1. Printar grafo na matriz de adjacência");
                System.out.println("2. Descobrir quantos vértices de articulação tem no grafo usando dfs");
                System.out.println("3. Verificar se dois vértices são ou não adjacentes");
                System.out.println("4. Calcular o grau de um vértice qualquer");
                System.out.println("5. Buscar todos os vizinhos de um vértice qualquer");
                System.out.println("6. Visitar todas as arestas do grafo");
                System.out.println("7. Gerar um arquivo de texto novo com a estrutura de visualização gráfica do grafo e visualizar ele graficamente");
                System.out.println("8. Sair");
                System.out.println();

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        graph.printGraph();
                        break;
                    case 2:
                        graph.findArticulationPoints();
                        break;
                    case 3:
                        System.out.println("Informe os vértices separados por espaço:");
                        String v1 = scanner.next();
                        String v2 = scanner.next();
                        graph.checkNodesAdjacency(v1, v2);
                        break;
                    case 4:
                        System.out.println("Informe o vértice:");
                        String node = scanner.next();
                        graph.calculateNodeDegree(node);
                        break;
                    case 5:
                        System.out.println("Informe o vértice:");
                        String neighborNode = scanner.next();
                        graph.nodeNeighborsSearch(neighborNode);
                        break;
                    case 6:
                        graph.printAllEdges();
                        break;
                    case 7:
                        graph.saveAdjacencyListToFile(NEWFILEPATH); // Cria um arquivo novo com o formato para gerar o gráfico do grafo
                        Graph graphFromNewFile = readGraphFromAdjacencyListFile(NEWFILEPATH); // Lê esse novo arquivo e armazena o novo grafo
                        GraphVisualizer.displayGraph(graphFromNewFile); // Agora com a lista de adjacencia o gráfico do grafo é gerado
                        break;
                    case 8:
                        running = false;
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }

                // Código para garantir que o input correto sera recebido
                String continueChoice;
                do {
                    System.out.println("\nDeseja realizar outra operação? (s/n)");
                    scanner.nextLine(); // Limpa o buffer do scanner
                    continueChoice = scanner.nextLine();
                    if (continueChoice.equalsIgnoreCase("n")) {
                        running = false;
                        System.out.println("Obrigado! Caso queira fazer algo novamente basta rodar o código de novo.");
                    }
                } while (!continueChoice.equalsIgnoreCase("n") && !continueChoice.equalsIgnoreCase("s"));
            }
        } else {
            System.out.println("Erro ao carregar o grafo.");
        }
        scanner.close();
    }
}

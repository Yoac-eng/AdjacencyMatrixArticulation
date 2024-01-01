package org.yoaceng;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Classe utilizada para ler e armazenar os grafos a partir de arquivos de texto
 * pré-dispostos na raiz do projeto.
 *
 * @author Cayo Cutrim
 */
public class GraphReader {
    /**
     * Lê um grafo de um arquivo contendo um grafo em um formato especifico.
     * @param filePath O caminho do arquivo a ser lido.
     * @return Um objeto Graph construído a partir do grafo informado.
     */
    public static Graph readGraphToMatrixFromFile(String filePath) {
        // Lista de vértices HashSet para que os vértices não se repitam
        Set<String> nodes = new HashSet<>();

        // Pecorre o arquivo uma primeira vez pra identificar todos vértices
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Ignora a primeira linha (D ou ND)
            String line;

            while ((line = br.readLine()) != null) {
                String[] nodeLine = line.split(", ");
                for (String node : nodeLine) {
                    nodes.add(node);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        // Lê o arquivo uma segunda vez para criar as arestas do grafo e instancia-lo
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            // Instancia de um novo grafo identificando se é digrafo e a quantidade de vértices
            Graph graph = new Graph(line.equals("D"), nodes.size());

            // Adiciona todos os vértices contidos no grafo como vértices da matriz
            for (String node : nodes) {
                graph.addNode(node);
            }

            // Adiciona todas as arestas
            while ((line = br.readLine()) != null) {
                String[] nodeLine = line.split(", ");
                graph.addEdge(nodeLine[0], nodeLine[1]);
            }

            return graph;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lê um grafo de um arquivo contendo uma lista de adjacência.
     * @param filePath O caminho do arquivo a ser lido.
     * @return Um objeto Graph construído a partir da lista de adjacência.
     */
    public static Graph readGraphFromAdjacencyListFile(String filePath) {
        // Estrutura para armazenar a lista de adjacência.
        Map<String, List<String>> adjacencyList = new HashMap<>();

        // Lista de vértices HashSet para que os vértices não se repitam
        Set<String> nodes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Lê a primeira linha para determinar se o grafo é dirigido ou não.
            String line = br.readLine();
            boolean isDriven = "D".equals(line);

            while ((line = br.readLine()) != null) {
                // Divide a linha em um vértice e seus vértices adjacentes.
                String[] parts = line.split(": ");
                String node = parts[0];
                nodes.add(node); // Adiciona o vértice ao conjunto de vértices.

                if (parts.length > 1 && !parts[1].isEmpty()) {
                    // Se houver vértices adjacentes, divide e adiciona à lista de adjacência.
                    String[] adjacentNodes = parts[1].split(", ");
                    adjacencyList.put(node, Arrays.asList(adjacentNodes));
                    // Adiciona todos os vértices adjacentes ao conjunto de vértices.
                    Collections.addAll(nodes, adjacentNodes);
                } else {
                    // Se não houver vértices adjacentes, adiciona uma lista vazia.
                    adjacencyList.put(node, new ArrayList<>());
                }
            }

            // Cria uma nova instância de Graph com base nas informações coletadas.
            Graph graph = new Graph(isDriven, nodes.size());
            for (String node : nodes) {
                // Adiciona cada vértice ao grafo.
                graph.addNode(node);
            }

            for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
                for (String adjacentNode : entry.getValue()) {
                    // Adiciona uma aresta para cada par de vértices adjacentes.
                    graph.addEdge(entry.getKey(), adjacentNode);
                }
            }

            return graph;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }
    }
}
 
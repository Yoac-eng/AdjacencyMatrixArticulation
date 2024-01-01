package org.yoaceng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Classe que representa um grafo como uma matriz de adjacência
 * e faz operações para obter informações sobre o mesmo.
 *
 * @author Cayo Cutrim
 */
public class Graph {
    private boolean driven;
    private int[][] adjacencyMatrix;
    private Map<String, Integer> nodesIndexes;
    private int nodesCounter;

    // Variáveis adicionais para a busca em profundidade
    private int time;                   // Para marcar o tempo de descoberta de cada vértice
    private int[] discoveryTime;        // Armazena o tempo de descoberta
    private int[] low;                  // Armazena o menor índice alcançável
    private boolean[] visited;          // Marca se um vértice foi visitado ou não
    private int[] parent;               // Armazena os pais dos vértices na árvore de DFS

    public Graph(boolean driven, int nodesQuantity) {
        this.driven = driven;
        this.adjacencyMatrix = new int[nodesQuantity][nodesQuantity];
        this.nodesIndexes = new HashMap<>();
        this.nodesCounter = 0;
    }

    public void findArticulationPoints() {
        time = 0;
        discoveryTime = new int[nodesCounter];
        low = new int[nodesCounter];
        visited = new boolean[nodesCounter];
        parent = new int[nodesCounter];
        Arrays.fill(parent, -1);

        // Executar DFS em cada vértice não visitado
        for (int i = 0; i < nodesCounter; i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }
    }

    private void dfs(int u) {
        // Marcar o vértice atual como visitado
        visited[u] = true;
        discoveryTime[u] = low[u] = ++time; // Definir tempo de descoberta e valor mínimo
        int children = 0;                  // Contar os filhos na árvore DFS

        // Verificar todos os vértices adjacentes ao vértice atual
        for (int v = 0; v < nodesCounter; v++) {
            if (adjacencyMatrix[u][v] != 0) {  // Verifica se existe uma aresta entre u e v
                if (!visited[v]) {
                    children++;
                    parent[v] = u;
                    dfs(v);  // Chamar DFS recursivamente para o vértice adjacente

                    // Atualizar o valor mínimo do vértice atual
                    low[u] = Math.min(low[u], low[v]);

                    // Verificar se o vértice atual é um ponto de articulação
                    // Caso 1: u é a raiz da DFS e tem dois ou mais filhos
                    if (parent[u] == -1 && children > 1) {
                        System.out.println(getNodeName(u) + " é um vértice de articulação");
                    }

                    // Caso 2: Se u não é a raiz e o valor mínimo de um de seus filhos é maior ou igual ao tempo de descoberta de u
                    if (parent[u] != -1 && low[v] >= discoveryTime[u]) {
                        System.out.println(getNodeName(u) + " é um vértice de articulação");
                    }
                } else if (v != parent[u]) {
                    // Atualiza o valor mínimo de u para o tempo de descoberta de v
                    low[u] = Math.min(low[u], discoveryTime[v]);
                }
            }
        }
    }

    /**
     * Retorna o nome do vértice correspondente ao endereço na lista
     * informado.
     * @param value Valor do vértice no mapeamento vértice - index.
     * @return Nome do vértice.
     */
    public String getNodeName(int value){
        for(Map.Entry<String, Integer> entry : nodesIndexes.entrySet()) {
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Adiciona um vértice inexistente no mapeamento de chave e valor onde
     * a chave é o nome do vértice e o valor é o o endereço do vértice na matriz
     * @param node
     */
    public void addNode(String node) {
        if (!nodesIndexes.containsKey(node)) {
            nodesIndexes.put(node, nodesCounter);
            nodesCounter++;
        }
    }

    /**
     * Método para adicionar uma aresta na matriz de adjacência, utilizando
     * o mapeamento de chave valor dos endereços dos nós na matriz e o nó de origem
     * e de destino da aresta.
     * @param origin
     * @param destiny
     */
    public void addEdge(String origin, String destiny) {
        int originIndex = nodesIndexes.get(origin);
        int destinyIndex = nodesIndexes.get(destiny);

        adjacencyMatrix[originIndex][destinyIndex] = 1;

        // Caso não seja um digrafo, adicionar tanto no destino quanto na origem
        if (!driven) {
            adjacencyMatrix[destinyIndex][originIndex] = 1;
        }
    }

    /**
     * Verifica a adjacência entre dois vértices determinados.
     * A forma de verificar muda se o grafo for dirigido ou não.
     * @param origin O nome do vértice de origem.
     * @param destiny O nome do vértice de destino.
     */
    public void checkNodesAdjacency(String origin, String destiny){
        int nxIndex = nodesIndexes.get(origin);
        int nyIndex = nodesIndexes.get(destiny);

        boolean isAdjacent = driven ? adjacencyMatrix[nxIndex][nyIndex] != 0 :
                                      adjacencyMatrix[nxIndex][nyIndex] != 0 && adjacencyMatrix[nyIndex][nxIndex] != 0;

        if(isAdjacent){
            System.out.println("Os vértices " + origin + " e " + destiny + " são adjacentes");
        } else{
            System.out.println("Os vértices " + origin + " e " + destiny + " não são adjacentes");
        }
    }

    /**
     * Calcula e exibe o grau de um vértice em um grafo.
     * Para grafos direcionados, exibe graus de emissão e recepção.
     * Para grafos não direcionados, exibe o grau do vértice.
     *
     * @param node O nome do vértice para calcular o grau.
     */
    public void calculateNodeDegree(String node){
        if (!nodesIndexes.containsKey(node)) {
            System.out.println("O vértice informado não existe no grafo obtido. Vértices válidos: " + nodesIndexes.keySet());
            return;
        }

        int nIndex= nodesIndexes.get(node);

        // Em caso de digrafo, calcular o gráu de emissão e de recepção
        if(driven){
            int emissionDegree = 0;
            int receptionDegree = 0;

            // Quantos 1 tem na linha do nó na matriz
            for(int i = 0; i < nodesCounter; i++){
                if(adjacencyMatrix[nIndex][i] != 0){
                    emissionDegree++;
                }
            }

            // Quantos 1 tem na coluna do nó na matriz
            for(int i = 0; i < nodesCounter; i++){
                if(adjacencyMatrix[i][nIndex] != 0){
                    receptionDegree++;
                }
            }

            System.out.println("O grau de emissão do vértice " + node + " é: " + emissionDegree);
            System.out.println("O grau de recepção do vértice " + node + " é: " + receptionDegree);
        } else {
            int nodeDegree = 0;
            // No caso do grafo não direcionado o grau pode ser definido
            // tanto pelas linhas quanto pelas colunas
            for(int i = 0; i < nodesCounter; i++){
                if(adjacencyMatrix[nIndex][i] != 0){
                    nodeDegree++;
                }
            }

            System.out.println("O grau do vértice " + node + " é: " + nodeDegree);
        }
    }

    /**
     * Retorna uma lista de vizinhos para um dado vértice.
     * Em um grafo dirigido, retorna tanto os sucessores quanto os predecessores.
     * Em um grafo não dirigido, retorna todos os vértices conectados.
     *
     * @param node O nome do vértice.
     * @return Lista de vizinhos do vértice.
     */
    public void nodeNeighborsSearch(String node) {
        if (node == null || node.isEmpty() || !nodesIndexes.containsKey(node)) {
            System.out.println("O vértice informado não existe no grafo. Vértices válidos: " + nodesIndexes.keySet());
            return;
        }

        int nIndex = nodesIndexes.get(node);
        Set<String> neighbors = new HashSet<>();

        // Para grafos dirigidos, um vértice adjacente é um vértice
        // para o qual existem arestas saindo do vértice especificado
        if (driven) {
            for (int i = 0; i < nodesCounter; i++) {
                if (adjacencyMatrix[nIndex][i] != 0) { // Destinos
                    neighbors.add(getNodeName(i));
                }
            }
        } else {
            // Para grafos não dirigidos, adicionar quaisquer conexões bi-direcionais
            for (int i = 0; i < nodesCounter; i++) {
                if (adjacencyMatrix[nIndex][i] != 0 && adjacencyMatrix[i][nIndex] != 0) {
                    neighbors.add(getNodeName(i));
                }
            }
        }

        // Imprimir os vizinhos
        System.out.println("Vizinhos do vértice " + node + ": " + neighbors);
    }

    /**
     * Imprime todas as arestas do grafo.
     */
    public void printAllEdges() {
        if (driven) {
            // Para grafos dirigidos
            for (int i = 0; i < nodesCounter; i++) {
                for (int j = 0; j < nodesCounter; j++) {
                    if (adjacencyMatrix[i][j] != 0) {
                        System.out.println(getNodeName(i) + " -> " + getNodeName(j));
                    }
                }
            }
        } else {
            // Para grafos não dirigidos
            for (int i = 0; i < nodesCounter; i++) {
                for (int j = i; j < nodesCounter; j++) {
                    if (adjacencyMatrix[i][j] != 0 && adjacencyMatrix[j][i] != 0) {
                        System.out.println(getNodeName(i) + " -- " + getNodeName(j));
                    }
                }
            }
        }
    }

    /**
     * Converte a matriz de adjacência em uma lista de adjacência e salva em um arquivo.
     * Utilizado para a geração de um novo arquivo de texto contendo
     * essa estrutura de dados, não é exatamente necessário no escopo
     * desse projeto, pois a biblioteca jgraphx aceita matriz de adjacencia
     * como estrutura de dados também, mas a conversão é feita pois foi
     * requisitado pelo professor.
     * @param filename O nome do arquivo para salvar a lista de adjacência.
     */
    public void saveAdjacencyListToFile(String filename) {
        // Transforma a matriz de adjacencia em uma lista de adjacencia
        Map<String, List<String>> adjacencyList = toAdjacencyList();
        File file = new File(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { // 'false' para não adicionar ao final do arquivo (sobrescrever)
            // Escreve 'D' para grafo dirigido ou 'ND' para grafo não dirigido
            writer.write(driven ? "D" : "ND");
            writer.newLine();

            for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
                String line = entry.getKey() + ": " + String.join(", ", entry.getValue());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao salvar a lista de adjacência: " + e.getMessage());
        }
    }

    /**
     * Converte a matriz de adjacência em uma lista de adjacência.
     * @return A lista de adjacência do grafo.
     */
    public Map<String, List<String>> toAdjacencyList() {
        Map<String, List<String>> adjacencyList = new HashMap<>();

        for (String node : nodesIndexes.keySet()) {
            int nodeIndex = nodesIndexes.get(node);
            List<String> adjacentNodes = new ArrayList<>();

            for (int i = 0; i < adjacencyMatrix[nodeIndex].length; i++) {
                if (adjacencyMatrix[nodeIndex][i] != 0) {
                    adjacentNodes.add(getNodeName(i));
                }
            }

            adjacencyList.put(node, adjacentNodes);
        }

        return adjacencyList;
    }

    /**
     * Percorre as colunas e linhas da matriz e imprime-a
     * no console.
     */
    public void printGraph() {
        System.out.println("Matriz de adjacência:");
        for (int i = 0; i < nodesCounter; i++) {
            for (int j = 0; j < nodesCounter; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isDriven() {
        return driven;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public Map<String, Integer> getNodesIndexes() {
        return nodesIndexes;
    }

    public int getNodesCounter() {
        return nodesCounter;
    }
}

package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
                        System.out.println(getKeyByValue(u) + " é um vértice de articulação");
                    }

                    // Caso 2: Se u não é a raiz e o valor mínimo de um de seus filhos é maior ou igual ao tempo de descoberta de u
                    if (parent[u] != -1 && low[v] >= discoveryTime[u]) {
                        System.out.println(getKeyByValue(u) + " é um vértice de articulação");
                    }
                } else if (v != parent[u]) {
                    // Atualiza o valor mínimo de u para o tempo de descoberta de v
                    low[u] = Math.min(low[u], discoveryTime[v]);
                }
            }
        }
    }

    public String getKeyByValue(int value){
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
     * @param nx
     * @param ny
     */
    public void checkNodesAdjacency(String nx, String ny){
        int nxIndex = nodesIndexes.get(nx);
        int nyIndex = nodesIndexes.get(ny);

        boolean isAdjacent = driven ? adjacencyMatrix[nxIndex][nyIndex] != 0 :
                                      adjacencyMatrix[nxIndex][nyIndex] != 0 && adjacencyMatrix[nyIndex][nxIndex] != 0;

        if(isAdjacent){
            System.out.println("Os seguintes vértices são adjacentes: " + nx + " e " + ny);
        } else{
            System.out.println("Não são adjacentes");
        }
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
}

package org.yoaceng;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.JFrame;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Classe responsável pelas operações de visualização gráfica
 * do grafo a partir da biblioteca Jgraphx.
 *
 * @author Cayo Cutrim
 */
public class GraphVisualizer {
    public static void displayGraph(Graph myGraph) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        Random random = new Random();

        graph.getModel().beginUpdate();
        int width = 800;
        int height = 600;
        try {
            // Adicionando vértices em posições aleatórias
            Map<String, Integer> nodesIndexes = myGraph.getNodesIndexes();
            Object[] vertices = new Object[nodesIndexes.size()];

            for (String nodeName : nodesIndexes.keySet()) {
                int x = random.nextInt(width - 100); // Deixando espaço para o tamanho do vértice
                int y = random.nextInt(height - 100); // Deixando espaço para o tamanho do vértice
                vertices[nodesIndexes.get(nodeName)] = graph.insertVertex(parent, null, nodeName, x, y, 80, 30);
            }

            // Adicionando arestas baseadas na lista de adjacência
            Map<String, List<String>> adjacencyList = myGraph.toAdjacencyList();
            for (String origin : adjacencyList.keySet()) {
                int originIndex = nodesIndexes.get(origin);
                for (String destination : adjacencyList.get(origin)) {
                    int destinationIndex = nodesIndexes.get(destination);
                    String edgeStyle = myGraph.isDriven() ? "endArrow=classic" : "endArrow=none";
                    graph.insertEdge(parent, null, "", vertices[originIndex], vertices[destinationIndex], edgeStyle);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }

        // Códigos padrão da lib
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
}
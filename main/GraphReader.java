package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GraphReader {
    public static void main(String[] args) {
        String filePath = "file.txt";
        // Lista de vértices HashSet para que os vértices não se repitam
        Set<String> nodes = new HashSet<>(); 

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

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            // Instancia de um novo grafo identificando se é digrafo e a quantidade de vértices
            Graph graph = new Graph(line.equals("D"), nodes.size());

            // Adiciona todos os vértices contidos no grafo como vértices da matriz
            for (String node : nodes) {
                graph.addNode(node);
            }

            while ((line = br.readLine()) != null) {
                String[] nodeLine = line.split(", ");
                graph.addEdge(nodeLine[0], nodeLine[1]);
            }

            graph.printGraph();
            System.out.println();
//            System.out.println("Vertíces de articulação na matriz:");
//            graph.findArticulationPoints();
//            graph.checkNodesAdjacency("A", "B");
//            graph.checkNodesAdjacency("A", "C");
            graph.calculateNodeDegree("A");
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
 
package rs.brightly.route.service;


import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.brightly.route.dto.Graph;
import rs.brightly.route.dto.Node;
import rs.brightly.route.dto.Country;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private static List<Country> countries;
    private Graph graph = new Graph();

    public RouteService() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
        restTemplate.getMessageConverters().add(0, converter);
        Country[] response = restTemplate.getForObject("https://raw.githubusercontent.com/mledoze/countries/master/countries.json", Country[].class);
        countries = Arrays.asList(response);

        for (Country country : countries) {
            graph.addNode(new Node(country.getCca3()));
        }
        for (Country country : countries) {
            Node node = graph.getNodes().stream().filter(n -> n.getName().equals(country.getCca3())).findFirst().orElse(null);
            for (String border : country.getBorders()) {
                Node neighbourNode = graph.getNodes().stream().filter(n -> n.getName().equals(border)).findFirst().orElse(null);
                node.addDestination(neighbourNode, 1);
            }
        }
    }

    public List<String> getRoute(String origin, String destination) {
        if (origin != null && origin.equals(destination)) {
            return Arrays.asList(origin);
        }
        if (countries.stream().filter(c -> c.getCca3().equals(origin)).findFirst().orElse(null) == null ||
                countries.stream().filter(c -> c.getCca3().equals(destination)).findFirst().orElse(null) == null) {
            return null;
        }

        Node sourceNode = graph.getNodes().stream().filter(n -> n.getName().equals(origin)).findFirst().orElse(null);
        if (graph.getStart() == null || !graph.getStart().equals(sourceNode)) {
            graph.setStart(sourceNode);

            //graph reset
            for (Node node : graph.getNodes()) {
                node.setShortestPath(new LinkedList<>());
                node.setDistance(Integer.MAX_VALUE);
            }

            //calculate distances (number of borders crossings)
            calculateShortestPathFromSource(sourceNode);
        }

        //get shortest route in destination node
        List<String> route = Objects.requireNonNull(graph.getNodes()
                        .stream()
                        .filter(n -> n.getName().equals(destination))
                        .findFirst()
                        .orElse(null))
                .getShortestPath()
                .stream()
                .map(Node::getName).collect(Collectors.toList());
        if (route.size() == 0) {
            return null;
        }
        route.add(destination);
        return route;
    }

    public static void calculateShortestPathFromSource(Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}

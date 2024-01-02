package HarmonyHub.Service;

import HarmonyHub.Models.Friendship;
import HarmonyHub.Models.Pair;
import HarmonyHub.Models.User;
import HarmonyHub.Repository.RepositoryDBFriendship;
import HarmonyHub.Repository.RepositoryDBPagingUser;
import HarmonyHub.Repository.RepositoryException;
import HarmonyHub.Repository.Validation.ValidatorFriendship;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceFriendship {
    private final RepositoryDBFriendship repository;
    private final ValidatorFriendship validator;
    private final RepositoryDBPagingUser repositoryUser;

    public ServiceFriendship(RepositoryDBFriendship repository, ValidatorFriendship validator, RepositoryDBPagingUser repositoryUser) {
        this.repository = repository;
        this.validator = validator;
        this.repositoryUser = repositoryUser;
    }

    public Friendship searchFriendshipByID(Pair<UUID, UUID> id) {
        Optional<Friendship> friendship = repository.findOne(id);

        if (friendship.isEmpty()) {
            throw new RepositoryException("There is no entity with the id " + id);
        }

        return friendship.get();
    }

    public Iterable<Friendship> getAll() {
        return repository.findAll();
    }

    public void addOneFriendship(Pair<UUID, UUID> id) {

        Friendship friendship = new Friendship();
        friendship.setId(id);
        friendship.setDateFriendship(LocalDateTime.now());

        validator.validate(friendship);

        if (repository.addOne(friendship).isEmpty()) {
            throw new RepositoryException("There already is an entity with id " + friendship.getId());
        }

    }

    public void removeFriendship(Pair<UUID, UUID> id) {
        Optional<Friendship> friendship = repository.delete(id);

        if (friendship.isEmpty()) {
            throw new RepositoryException("There is no entity with the id " + id);
        }
    }

    public int numberOfCommunities() {
        Map<UUID, Set<UUID>> adjList;
        adjList = new HashMap<>();
        createAdjList(adjList);
        int nrOfCommunities = 0;

        Set<UUID> visited = new HashSet<>();

        for (UUID friend : adjList.keySet()) {
            if (!visited.contains(friend)) {
                DFS(friend, visited, adjList);
                nrOfCommunities++;
            }
        }
        return nrOfCommunities;
    }

    private void DFS(UUID startFriend, Set<UUID> visited, Map<UUID, Set<UUID>> adjacencyList) {
        Stack<UUID> stack = new Stack<>();
        stack.push(startFriend);

        while (!stack.isEmpty()) {
            UUID currentVertex = stack.pop();
            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);

                Set<UUID> friends = adjacencyList.get(currentVertex);
                if (friends != null) {
                    for (UUID friend : friends) {
                        if (!visited.contains(friend)) {
                            stack.push(friend);
                        }
                    }
                }
            }
        }
    }

    private void createAdjList(Map<UUID, Set<UUID>> adjList) {
        Iterable<Friendship> friendships = repository.findAll();

        friendships.forEach(friendship -> {
            adjList.computeIfAbsent(friendship.getId().getLeft(), k -> new HashSet<>()).add(friendship.getId().getRight());
            adjList.computeIfAbsent(friendship.getId().getRight(), k -> new HashSet<>()).add(friendship.getId().getLeft());
        });
    }

    public List<User> mostSociableCommunity() {
        Map<UUID, Set<UUID>> adjacencyList = new HashMap<>();
        createAdjList(adjacencyList);

        List<UUID> largestComponent = new ArrayList<>();
        Set<UUID> visited = new HashSet<>();

        for (UUID vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                List<UUID> currentComponent = new ArrayList<>();
                Stack<UUID> stack = new Stack<>();
                stack.push(vertex);

                while (!stack.isEmpty()) {
                    UUID currentVertex = stack.pop();
                    if (!visited.contains(currentVertex)) {
                        visited.add(currentVertex);
                        currentComponent.add(currentVertex);

                        Set<UUID> neighbors = adjacencyList.get(currentVertex);
                        if (neighbors != null) {
                            for (UUID neighbor : neighbors) {
                                if (!visited.contains(neighbor)) {
                                    stack.push(neighbor);
                                }
                            }
                        }
                    }
                }

                if (currentComponent.size() > largestComponent.size()) {
                    largestComponent = currentComponent;
                }
            }
        }

        List<User> community = new ArrayList<>();

        for (UUID id : largestComponent) {
            community.add(repositoryUser.findOne(id).get());
        }

        return community;

    }

    public List<Friendship> friendsFromMonth(UUID id, int month) {
        return repository.friendsFromMonth(id, month);
    }

}

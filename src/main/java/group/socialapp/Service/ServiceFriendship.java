package group.socialapp.Service;

import group.socialapp.Domain.Friendship;
import group.socialapp.Domain.FriendshipRequest;
import group.socialapp.Domain.Pair;
import group.socialapp.Domain.User;
import group.socialapp.GUI.Events.ChangeEventType;
import group.socialapp.GUI.Events.UserChangeEvent;
import group.socialapp.GUI.Observer.Observable;
import group.socialapp.GUI.Observer.Observer;
import group.socialapp.Repository.FriendshipDBRepository;
import group.socialapp.Repository.RepositoryException;
import group.socialapp.Repository.RequestDBRepository;
import group.socialapp.Repository.UserDBRepository;
import group.socialapp.Validators.Validator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceFriendship implements Observable<UserChangeEvent> {


    private final FriendshipDBRepository repository;

    private final UserDBRepository userRepository;

    private final RequestDBRepository requestRepository;

    private final Validator<Friendship> validator;

    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    public ServiceFriendship(FriendshipDBRepository repository, UserDBRepository user_repository, RequestDBRepository requestRepository, Validator<Friendship> validator) {
        this.repository = repository;
        this.userRepository = user_repository;
        this.requestRepository = requestRepository;
        this.validator = validator;
    }


    public Optional<Friendship> searchFriendshipById(Pair<String, String> id) {
        Optional<Friendship> friendship = repository.findOne(id);
        if(friendship.isEmpty())
        {
            throw new RepositoryException("There is no entity with the id " + id);
        }

        return friendship;
    }

    public Iterable<Friendship> getAll() {
        return repository.findAll();
    }

    public void addOneFriendship(String id_friend1, String id_friend2) {
        Friendship friendship = new Friendship();
        friendship.setId(new Pair<>(id_friend1,id_friend2));
        friendship.setDate(LocalDateTime.now());
        validator.validate(friendship);

        if (repository.addOne(friendship).isEmpty())
        {
            throw new RepositoryException("There already is an entity with id " + friendship.getId());
        }
    }

    public void removeFriendship(Pair<String, String> id){
        Optional<Friendship> friendship = repository.delete(id);

        if(friendship.isEmpty())
        {
            throw new RepositoryException("There is no entity with the id " + id);
        }
    }

    public int getNumberOfFriendships(){return repository.getNrOfEntities();}


    public int numberOfCommunities() {
        Map<String, Set<String>> adj_list;
        adj_list = new HashMap<>();
        createAdjList(adj_list);
        int nr_of_communitites = 0;

        Set<String> visited = new HashSet<>();

        for (String friend : adj_list.keySet()) {
            if (!visited.contains(friend)) {
                DFS(friend, visited,adj_list);
                nr_of_communitites++;
            }
        }

        return nr_of_communitites;

    }

    private void DFS(String startfriend, Set<String> visited, Map<String,Set<String>> adjacencyList) {
        Stack<String> stack = new Stack<>();
        stack.push(startfriend);

        while (!stack.isEmpty()) {
            String currentVertex = stack.pop();
            if (!visited.contains(currentVertex)) {
                visited.add(currentVertex);

                Set<String> friends = adjacencyList.get(currentVertex);
                if (friends != null) {
                    for (String friend : friends) {
                        if (!visited.contains(friend)) {
                            stack.push(friend);
                        }
                    }
                }
            }
        }
    }

    private void createAdjList(Map<String, Set<String>> adjList) {
        Iterable<Friendship> friendships = repository.findAll();

        friendships.forEach(friendship -> {
            adjList.computeIfAbsent(friendship.getId().getLeft(), k -> new HashSet<>()).add(friendship.getId().getRight());
            adjList.computeIfAbsent(friendship.getId().getRight(), k -> new HashSet<>()).add(friendship.getId().getLeft());
        });


    }

    public List<User> mostSociableCommunity() {
        Map<String,Set<String>> adjacencyList = new HashMap<>();
        createAdjList(adjacencyList);

        List<String> largestComponent = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                List<String> currentComponent = new ArrayList<>();
                Stack<String> stack = new Stack<>();
                stack.push(vertex);

                while (!stack.isEmpty()) {
                    String currentVertex = stack.pop();
                    if (!visited.contains(currentVertex)) {
                        visited.add(currentVertex);
                        currentComponent.add(currentVertex);

                        Set<String> neighbors = adjacencyList.get(currentVertex);
                        if (neighbors != null) {
                            for (String neighbor : neighbors) {
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

        for (String id : largestComponent) {
            community.add(userRepository.findOne(id).get());
        }

        return community;

    }

    public Optional<User> deleteUser(String id) {

        Iterable<Friendship> friendships = repository.findAll();

        User user2 = userRepository.findOne(id).get();

        friendships.forEach(friendship -> {
            if (Objects.equals(friendship.getId().getLeft(), user2.getId()) || Objects.equals(friendship.getId().getRight(), user2.getId()))
                repository.delete(friendship.getId());
        });

        Optional<User> user = userRepository.delete(id);

        if(user.isEmpty())
        {
            throw new RepositoryException("There is no entity with the id " + id);
        }
//        notify(new UserChangeEvent(ChangeEventType.DELETE, user.get()));
        return user;
    }

    public List<String> friendsFromMonth(String id, int month) {
        Iterable<Friendship> friendships = repository.findAll();

        return StreamSupport.stream(friendships.spliterator(),false)
                .filter(friendship -> isFrienshipfromMonth(friendship, id, month))
                .map(friendship -> formatFrienship(friendship, id))
                .collect(Collectors.toList());
    }

    private String formatFrienship(Friendship friendship, String id) {
        String str;

        User user;
        if (Objects.equals(friendship.getId().getLeft(), id)){
            user = userRepository.findOne(friendship.getId().getRight()).get();
        }
        else {
            user = userRepository.findOne(friendship.getId().getLeft()).get();
        }
        str = user.getLastName() + "|" + user.getFirstName() + "|" + friendship.getDate().format(DateTimeFormatter.ISO_DATE);

        return str;
    }

    private boolean isFrienshipfromMonth(Friendship friendship, String id, int month) {
        LocalDateTime dateTime = friendship.getDate();
        return dateTime.getMonthValue() == month &&
                (Objects.equals(friendship.getId().getLeft(), id) ||
                        Objects.equals(friendship.getId().getRight(), id));
    }


    @Override
    public void addObserver(Observer<UserChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(UserChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }

    public ArrayList<User> getAllFriendsByEmail(String email) {
        return repository.getAllFriendsByEmail(userRepository.getByEmail(email).get().getId());
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Iterable<User> filterByEmail(String searchText) {
        repository.setUser(this.user);
        return repository.filterByEmail(searchText);
    }


    public void denyRequest(User user1, User user2) {
        FriendshipRequest friendshipRequest = requestRepository.findOne(new Pair<>(user1.getId(), user2.getId())).get();
        friendshipRequest.setStatus("rejected");
        requestRepository.update(friendshipRequest);
        notify(new UserChangeEvent(ChangeEventType.DENY, userRepository.findOne(friendshipRequest.getId().getRight()).get()));
    }

    public Iterable<User> getALlRequests(User user) {
        Iterable<FriendshipRequest> friendshipRequests = requestRepository.findAllRequestOfUser(user);

        List<User> users = new ArrayList<>();

        friendshipRequests.forEach(friendshipRequest -> {
            User user1 = userRepository.findOne(friendshipRequest.getId().getLeft()).get();
            users.add(user1);
        });

        return users;
    }

    public void acceptRequest(User user1, User user2) {
        FriendshipRequest friendshipRequest = requestRepository.findOne(new Pair<>(user2.getId(), user1.getId())).get();
        friendshipRequest.setStatus("accepted");
        requestRepository.update(friendshipRequest);
        addOneFriendship(user1.getId(), user2.getId());
        notify(new UserChangeEvent(ChangeEventType.ACCEPT, userRepository.findOne(friendshipRequest.getId().getRight()).get()));
    }

    public Iterable<User> filterByEmailAndRequest(String searchText) {
        repository.setUser(this.user);
        return repository.filterByEmailAndRequest(searchText);
    }

    public void addRequest(User selected) {

        EventHandler<ActionEvent> acceptHandler = event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Friendship accepted");
            alert.showAndWait();
        };

        EventHandler<ActionEvent> denyHandler = event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Friendship denied");
            alert.showAndWait();
        };

        FriendshipRequest friendshipRequest = new FriendshipRequest(LocalDateTime.now(), "pending", acceptHandler, denyHandler);
        friendshipRequest.setId(new Pair<>(user.getId(), selected.getId()));
        requestRepository.addOne(friendshipRequest);
        notify(new UserChangeEvent(ChangeEventType.PENDING, selected));
    }

    public Iterable<User> filterbyRequest() {
        repository.setUser(this.user);
        return repository.filterByRequest();
    }

    public ArrayList<User> getAllFriendsByEmail2(String email) {
        return repository.getAllFriendsByEmail2(userRepository.getByEmail(email).get().getId());
    }
}

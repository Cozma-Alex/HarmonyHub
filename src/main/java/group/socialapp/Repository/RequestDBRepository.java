package group.socialapp.Repository;

import group.socialapp.Domain.FriendshipRequest;
import group.socialapp.Domain.Pair;
import group.socialapp.Domain.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestDBRepository implements Repository<Pair<String, String>, FriendshipRequest>{

    private final String url;
    private final String username;
    private final String password;

    public RequestDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<FriendshipRequest> findOne(Pair<String, String> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendshiprequest where id_user1 = ? and id_user2 = ?")){

            statement.setString(1, id.getLeft());
            statement.setString(2, id.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String id_user1 = resultSet.getString("id_user1");
                String id_user2 = resultSet.getString("id_user2");
                LocalDateTime friendsSince = resultSet.getTimestamp("daterequest").toLocalDateTime();
                String status = resultSet.getString("status");

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


                FriendshipRequest friendshipRequest = new FriendshipRequest(friendsSince,status, acceptHandler, denyHandler);
                friendshipRequest.setId(new Pair<>(id_user1, id_user2));
                return Optional.of(friendshipRequest);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }

        return Optional.empty();
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        List<FriendshipRequest> friendshipSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendshiprequest");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                String id_user1 = resultSet.getString("id_user1");
                String id_user2 = resultSet.getString("id_user2");
                LocalDateTime dateTime = resultSet.getTimestamp("daterequest").toLocalDateTime();
                String status = resultSet.getString("status");

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


                FriendshipRequest friendshipRequest = new FriendshipRequest(dateTime,status, acceptHandler, denyHandler);
                friendshipRequest.setId(new Pair<>(id_user1, id_user2));
                friendshipSet.add(friendshipRequest);

            }

            return friendshipSet;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public Optional<FriendshipRequest> addOne(FriendshipRequest entity) {
        if (entity == null){
            throw new IllegalArgumentException("Friendship must not be null");
        }

        String insertSQL = "insert into friendshiprequest values (?,?,?,?)";

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getId().getLeft());
            statement.setString(2, entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(4, entity.getStatus());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public Optional<FriendshipRequest> delete(Pair<String, String> stringStringPair) {
        return Optional.empty();
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        String insertSQL = "update friendshiprequest set status = ? where id_user1 = ? and id_user2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getStatus());
            statement.setString(2, entity.getId().getLeft());
            statement.setString(3, entity.getId().getRight());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public int getNrOfEntities() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) from friendshiprequest");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
        return 0;
    }

    @Override
    public Iterable<User> filterByEmail(String searchText) {return null;}

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void setUser(User user) {

    }

    public Iterable<FriendshipRequest> findAllRequestOfUser(User user) {
        List<FriendshipRequest> friendshipSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendshiprequest where id_user2 = ? and status = ?");
             ) {

            statement.setString(1, user.getId());
            statement.setString(2, "pending");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id_user1 = resultSet.getString("id_user1");
                String id_user2 = resultSet.getString("id_user2");
                LocalDateTime dateTime = resultSet.getTimestamp("daterequest").toLocalDateTime();
                String status = resultSet.getString("status");

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

                FriendshipRequest friendshipRequest = new FriendshipRequest(dateTime,status, acceptHandler, denyHandler);
                friendshipRequest.setId(new Pair<>(id_user1, id_user2));
                friendshipSet.add(friendshipRequest);

            }

            return friendshipSet;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }
}

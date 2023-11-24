package group.socialapp.Repository;


import group.socialapp.Domain.Friendship;
import group.socialapp.Domain.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class FriendshipDBRepository implements Repository<Pair<String, String>, Friendship> {

    private final String url;
    private final String username;
    private final String password;


    public FriendshipDBRepository(String url, String user, String password) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public Optional<Friendship> findOne(Pair<String, String> id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendship where id_friend1 = ? and id_friend2 = ?");
             ResultSet resultSet = statement.executeQuery()){

            statement.setString(1, id.getLeft());
            statement.setString(2, id.getRight());
            statement.setString(3, id.getRight());
            statement.setString(4, id.getLeft());
            if (resultSet.next()){
                Friendship friendship = new Friendship();
                String id_friend1 = resultSet.getString("id_friend1");
                String id_friend2 = resultSet.getString("id_friend2");
                LocalDateTime friendsSince = resultSet.getTimestamp("friendssince").toLocalDateTime();
                friendship.setDate(friendsSince);
                friendship.setId(new Pair<>(id_friend1,id_friend2));

                return Optional.of(friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection failes");
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendshipSet = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                String id_friend1 = resultSet.getString("id_friend1");
                String id_friend2 = resultSet.getString("id_friend2");
                LocalDateTime dateTime = resultSet.getTimestamp("friendssince").toLocalDateTime();
                Friendship friendship = new Friendship();
                friendship.setDate(dateTime);
                friendship.setId(new Pair<>(id_friend1, id_friend2));
                friendshipSet.add(friendship);
            }

            return friendshipSet;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public Optional<Friendship> addOne(Friendship entity) {
        if (entity == null){
            throw new IllegalArgumentException("Friendship must not be null");
        }

        String insertSQL = "insert into friendship values (?,?,?)";

        try(Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getId().getLeft());
            statement.setString(2, entity.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }

    }

    @Override
    public Optional<Friendship> delete(Pair<String, String> id) {
        if (id == null){
            throw new IllegalArgumentException("Id must not be null");
        }

        Optional<Friendship> friendship = findOne(id);

        String insertSQL = "delete from friendship where id_friend1 = ? and id_friend2 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, id.getLeft());
            statement.setString(2, id.getRight());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : friendship;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }

    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    @Override
    public int getNrOfEntities() {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(*) from friendship");
            ResultSet resultSet = statement.executeQuery()){

            if (resultSet.next()){
                return resultSet.getInt("count");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }

        return 0;

    }

    @Override
    public Iterable<Friendship> filterByEmail(Pair<String, String> searchText) {
        return null;
    }
}

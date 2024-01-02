package HarmonyHub.Repository;


import HarmonyHub.Models.Friendship;
import HarmonyHub.Models.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryDBFriendship implements Repository<Pair<UUID, UUID>, Friendship> {

    private final String url;
    private final String username;
    private final String password;

    public RepositoryDBFriendship(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Friendship> findOne(Pair<UUID, UUID> id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships where friend_1 = ? and friend_2 = ?")) {

            statement.setObject(1, id.getLeft());
            statement.setObject(2, id.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Friendship friendship = new Friendship();
                LocalDateTime friendsSince = resultSet.getTimestamp("date_friendship").toLocalDateTime();
                friendship.setDateFriendship(friendsSince);
                friendship.setId(id);

                return Optional.of(friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Retrieving data from the database failed");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships");
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id_friend1 = (UUID) resultSet.getObject("friend_1");
                UUID id_friend2 = (UUID) resultSet.getObject("friend_2");
                LocalDateTime dateTime = resultSet.getTimestamp("date_friendship").toLocalDateTime();
                Friendship friendship = new Friendship();
                friendship.setDateFriendship(dateTime);
                friendship.setId(new Pair<>(id_friend1, id_friend2));
                friendships.add(friendship);
            }

            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException("Retrieving data from the database failed");
        }
    }

    @Override
    public Optional<Friendship> addOne(Friendship friendship) {
        if (friendship == null) {
            throw new IllegalArgumentException("Friendship must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("insert into friendships values (?,?,?)");
            statement.setObject(1, friendship.getId().getLeft());
            statement.setObject(2, friendship.getId().getRight());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(friendship);
        } catch (SQLException e) {
            throw new RuntimeException("Adding data to the database failed");
        }
    }

    @Override
    public Optional<Friendship> delete(Pair<UUID, UUID> id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        Optional<Friendship> friendship = findOne(id);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("delete from friendships where friend_1 = ? and friend_2 = ?");
            statement.setObject(1, id.getLeft());
            statement.setObject(2, id.getRight());
            statement.executeUpdate();
            return friendship;

        } catch (SQLException e) {
            throw new RuntimeException("Deleting data from the database failed");
        }
    }

    @Override
    public Optional<Friendship> update(Friendship friendship) {
        return Optional.empty();
    }

    public List<Friendship> friendsFromMonth(UUID id, int month) {
        List<Friendship> friendships = new ArrayList<>();

        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        if (0 > month || month > 12) {
            throw new RuntimeException("Month must be between 1 and 12");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships where (friend_1 = ? or friend_2 = ?) and extract(month from date_friendship) = ?")
        ) {
            statement.setObject(1, id);
            statement.setObject(2, id);
            statement.setInt(3, month);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID id_friend1 = (UUID) resultSet.getObject("friend_1");
                UUID id_friend2 = (UUID) resultSet.getObject("friend_2");
                LocalDateTime dateTime = resultSet.getTimestamp("date_friendship").toLocalDateTime();
                Friendship friendship = new Friendship();
                friendship.setDateFriendship(dateTime);
                friendship.setId(new Pair<>(id_friend1, id_friend2));
                friendships.add(friendship);
            }

            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException("Retrieving data from the database failed");
        }

    }
}

package group.socialapp.Repository;


import group.socialapp.Domain.Friendship;
import group.socialapp.Domain.Pair;
import group.socialapp.Domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
             PreparedStatement statement = connection.prepareStatement("select * from friendship where id_friend1 = ? and id_friend2 = ?")){

            statement.setString(1, id.getLeft());
            statement.setString(2, id.getRight());
            ResultSet resultSet = statement.executeQuery();
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
    public Iterable<User> filterByEmail(String searchText) {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users u join friendship f on u.id = f.id_friend1 or u.id = f.id_friend2 where (f.id_friend1 = ? or f.id_friend2 = ?) and lower(u.email) like ?");
        ) {

            statement.setString(1, user.getId());
            statement.setString(2, user.getId());
            statement.setString(3, "%"+searchText+"%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id_friend1 = resultSet.getString("id_friend1");
                String id_friend2 = resultSet.getString("id_friend2");
                LocalDateTime dateTime = resultSet.getTimestamp("friendssince").toLocalDateTime();
                Friendship friendship = new Friendship();
                friendship.setDate(dateTime);
                friendship.setId(new Pair<>(id_friend1, id_friend2));

            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.empty();
    }

    public ArrayList<User> getAllFriendsByEmail(String id_user) {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users u join friendship on (id_friend1 = u.id or id_friend2 = u.id) where u.id = ?");
             ) {

            statement.setString(1, id_user);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, email);
                user.setPassword(pass2);
                user.setSalt(salt);
                user.setId(id);

                users.add(user);

            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Iterable<User> filterByEmailAndRequest(String searchText) {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select u.* from users u where not exists(select 1 from friendship f where (f.id_friend1 = u.id or f.id_friend2 = u.id)) and not exists(select 1 from friendshiprequest fr join users u2 on u2.id = fr.id_user1 where u2.email like ? and fr.id_user2 = u.id) and u.email like ? and u.id != ?");
        ) {
            statement.setString(1, "%"+searchText+"%");
            statement.setString(2, "%"+searchText+"%");
            statement.setString(3, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, email);
                user.setPassword(pass2);
                user.setSalt(salt);
                user.setPassword(pass2);
                user.setId(id);

                users.add(user);

            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    public Iterable<User> filterByRequest() {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u.*\n" +
                     "FROM users u\n" +
                     "WHERE NOT EXISTS (\n" +
                     "    SELECT 1\n" +
                     "    FROM friendship f\n" +
                     "    WHERE (u.id = f.id_friend1 OR u.id = f.id_friend2)\n" +
                     "       AND (? IN (f.id_friend1, f.id_friend2))\n" +
                     ")\n" +
                     "AND NOT EXISTS (\n" +
                     "    SELECT 1\n" +
                     "    FROM friendshiprequest fr\n" +
                     "    WHERE (u.id = fr.id_user1 OR u.id = fr.id_user2)\n" +
                     "       AND (? IN (fr.id_user1, fr.id_user2))\n" +
                     ");");
        ) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, email);
                user.setPassword(pass2);
                user.setSalt(salt);
                user.setPassword(pass2);
                user.setId(id);

                users.add(user);

            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    public ArrayList<User> getAllFriendsByEmail2(String id) {
        ArrayList<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u.*\n" +
                     "FROM users u\n" +
                     "JOIN friendship f ON u.id = f.id_friend1 OR u.id = f.id_friend2\n" +
                     "WHERE ? IN (f.id_friend1, f.id_friend2) and u.id != ?");
        ) {

            statement.setString(1, id);
            statement.setString(2, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id_user = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, email);
                user.setPassword(pass2);
                user.setSalt(salt);
                user.setId(id_user);

                users.add(user);

            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }
}

package HarmonyHub.Repository;

import HarmonyHub.Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class RepositoryDBUser implements Repository<UUID, User> {

    private final String url;
    private final String username;
    private final String password;

    public RepositoryDBUser(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where id_user = ?")
        ) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String passwordHash = resultSet.getString("password_hash");
                String salt = resultSet.getString("salt");
                String appTheme = resultSet.getString("app_theme");
                User user = new User(firstName, lastName, username, email, passwordHash, salt, appTheme);
                user.setId(id);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Retrieving data from the database failed");
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where id_user = ?")
        ) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String passwordHash = resultSet.getString("password_hash");
                String salt = resultSet.getString("salt");
                String appTheme = resultSet.getString("app_theme");
                User user = new User(firstName, lastName, username, email, passwordHash, salt, appTheme);
                user.setId(id);
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Retrieving data from the database failed");
        }
        return userList;
    }

    @Override
    public Optional<User> addOne(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("insert into users values" +
                    "(?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setObject(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPasswordHash());
            statement.setString(7, user.getSalt());
            statement.setString(8, user.getAppTheme());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(user);

        } catch (SQLException e) {
            throw new RuntimeException("Adding data to the database failed");
        }

    }

    @Override
    public Optional<User> delete(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        Optional<User> user = findOne(id);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from users where id_user = ?")
        ) {

            statement.setObject(1, id);
            statement.executeUpdate();
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Deleting data from the database failed");
        }
    }

    @Override
    public Optional<User> update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        Optional<User> user1 = findOne(user.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("update users set first_name = ?, last_name = ?," +
                        "username = ?, email = ?, password_hash = ?, salt = ?, app_theme = ?  where id_user = ?")
        ) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPasswordHash());
            statement.setString(6, user.getSalt());
            statement.setString(7, user.getAppTheme());
            statement.setObject(8, user.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Updating data in the database failed");
        }
        return user1;

    }
}
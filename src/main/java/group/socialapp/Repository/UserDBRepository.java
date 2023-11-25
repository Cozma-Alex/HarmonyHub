package group.socialapp.Repository;

import group.socialapp.Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBRepository implements Repository<String, User> {

    private final String url;

    private final String username;

    private final String password;

    public UserDBRepository(String url, String user, String password) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(String id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where id = ?")
        ) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(firstName, lastName, email);
                user.setPassword(pass2);
                user.setSalt(salt);
                user.setId(id);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
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

                userList.add(user);
            }

            return userList;

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public Optional<User> addOne(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        String insertSQL = "insert into users(id, first_name, last_name, email, password, salt) values(?,?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPassword());
            statement.setString(6, entity.getSalt());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }

    }

    @Override
    public Optional<User> delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        Optional<User> user = findOne(id);

        String insertSQL = "delete from users where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, id);
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : user;

        } catch (SQLException e) {
            throw new RuntimeException("Conection failed");
        }

    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        String insertSQL = "update users set first_name = ?, last_name = ?, email = ?, password = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setString(5, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
    }

    @Override
    public int getNrOfEntities() {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select count(*) from users");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Connection failed");
        }
        return 0;

    }

    public Iterable<User> filterByEmail(String searchText) {
        List<User> userList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT * FROM users WHERE email LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + searchText + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
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
                        userList.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users from the database", e);
        }

        return userList;
    }

    @Override
    public Optional<User> getByEmail(String emailToSearch) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users where email = ?")
        ) {
            statement.setString(1, emailToSearch);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
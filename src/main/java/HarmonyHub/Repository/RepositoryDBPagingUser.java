package HarmonyHub.Repository;

import HarmonyHub.Models.User;
import HarmonyHub.Repository.Paging.Page;
import HarmonyHub.Repository.Paging.PageImplementation;
import HarmonyHub.Repository.Paging.Pageable;
import HarmonyHub.Repository.Paging.PagingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class RepositoryDBPagingUser extends RepositoryDBUser implements PagingRepository<UUID, User> {

    private final String url;

    private final String username;

    private final String password;

    public RepositoryDBPagingUser(String url, String username, String password) {
        super(url, username, password);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        ArrayList<User> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?")){

            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber()-1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String pass2 = resultSet.getString("password");
                User user = new User(firstName, lastName, email, pass2);
                user.setId(id);

                result.add(user);
            }

            return new PageImplementation<>(pageable, result.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}


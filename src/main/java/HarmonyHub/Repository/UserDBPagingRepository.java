package HarmonyHub.Repository;

import HarmonyHub.Repository.Paging.Page;
import HarmonyHub.Repository.Paging.PagingRepository;
import HarmonyHub.Domain.User;
import HarmonyHub.Repository.Paging.PageImplementation;
import HarmonyHub.Repository.Paging.Pageable;

import java.sql.*;
import java.util.ArrayList;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<String, User> {

    private final String url;

    private final String username;

    private final String password;

    public UserDBPagingRepository(String url, String username, String password, String url1, String username1, String password1) {
        super(url, username, password);
        this.url = url1;
        this.username = username1;
        this.password = password1;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
//        Stream<User> result = StreamSupport.stream(this.findAll().spliterator(), false)
//                .skip(pageable.getPageNumber()  * pageable.getPageSize())
//                .limit(pageable.getPageSize());
        ArrayList<User> result = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?")){

            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber()-1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
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

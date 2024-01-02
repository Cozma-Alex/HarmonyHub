package HarmonyHub.Service;

import HarmonyHub.Models.User;
import HarmonyHub.Repository.RepositoryDBPagingUser;
import HarmonyHub.Repository.RepositoryException;
import HarmonyHub.Repository.Validation.ValidatorUser;
import HarmonyHub.Service.Events.ChangeEventType;
import HarmonyHub.Service.Events.UserChangeEvent;
import HarmonyHub.Service.Observer.Observable;
import HarmonyHub.Service.Observer.Observer;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServiceUser implements Observable<UserChangeEvent> {

    private final ValidatorUser validator;
    private final RepositoryDBPagingUser repository;
    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    public ServiceUser(ValidatorUser validator, RepositoryDBPagingUser repository) {
        this.validator = validator;
        this.repository = repository;
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

    public User searchUserById(UUID id) {
        Optional<User> user = repository.findOne(id);

        if (user.isEmpty()) {
            throw new RepositoryException("There is no entity with the id " + id);
        }

        return user.get();
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    public void addOneUser(String firstName, String lastName, String username, String email, String password) {

        UUID idUser = UUID.randomUUID();
        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(password, salt);
        String appTheme = "light";
        User user = new User(firstName, lastName, username, email, passwordHash, salt, appTheme);
        user.setId(idUser);

        validator.validate(user);

        if (repository.addOne(user).isEmpty()) {
            throw new RepositoryException("There already is an user with id " + user.getId());
        }

        notify(new UserChangeEvent(ChangeEventType.ADD, user));

    }

    public void updateUser(UUID idUser, String firstName, String lastName, String username, String email, String password) {

        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(password, salt);
        String appTheme = "light";
        User user = new User(firstName, lastName, username, email, passwordHash, salt, appTheme);
        user.setId(idUser);

        validator.validate(user);

        Optional<User> userOptional = repository.update(user);

        if (userOptional.isEmpty()) {
            throw new RepositoryException("There is no entity with the id " + user.getId());
        }

        notify(new UserChangeEvent(ChangeEventType.UPDATE, userOptional.get(), user));

    }

    public void deleteUser(UUID idUser){
        Optional<User> user =  repository.delete(idUser);

        if (user.isEmpty()){
            throw new RepositoryException("There is no entity with the id " + idUser);
        }
        notify(new UserChangeEvent(ChangeEventType.DELETE, user.get()));
    }

}

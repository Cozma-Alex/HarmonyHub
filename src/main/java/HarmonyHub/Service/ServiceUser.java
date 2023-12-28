package HarmonyHub.Service;

import HarmonyHub.Repository.Repository;
import HarmonyHub.Validators.Validator;
import HarmonyHub.Domain.User;
import HarmonyHub.GUI.Events.ChangeEventType;
import HarmonyHub.GUI.Events.UserChangeEvent;
import HarmonyHub.GUI.Observer.Observable;
import HarmonyHub.GUI.Observer.Observer;
import HarmonyHub.Repository.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServiceUser implements Observable<UserChangeEvent> {


    private final Validator<User> validator;

    private final Repository<String, User> repository;

    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    public ServiceUser(Repository<String, User> userRepo, Validator<User> userValidator) {
        this.validator = userValidator;
        this.repository = userRepo;
    }

    public Optional<User> searchUserById(String id){
        Optional<User> user = repository.findOne(id);

        if(user.isEmpty())
        {
            throw new RepositoryException("There is no entity with the id " + id);
        }
        notifyAll();
        return user;
    }

    public Iterable<User> getAll(){
        repository.setUser(user);
        return repository.findAll();
    }

    public User addOneUser(String firstName, String lastName, String email, String password){
        String id_user = UUID.randomUUID().toString();

        User user = new User(firstName,lastName, email, password);
        user.setId(id_user);

        validator.validate(user);

        if (repository.addOne(user).isEmpty())
        {
            throw new RepositoryException("There already is an user with id " + user.getId());
        }
        notify(new UserChangeEvent(ChangeEventType.ADD, user));
        return user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User updateAnUser(String id, String firstName, String lastName, String email, String password){
        User user = new User(firstName,lastName, email, password);
        user.setId(id);

        validator.validate(user);
        Optional<User> user2 = repository.update(user);
        if (user2.isEmpty())
        {
            throw new RepositoryException("There is no entity with the id " + user.getId());
        }
        notify(new UserChangeEvent(ChangeEventType.UPDATE, user2.get(), user));
        return user;
    }

    public int getNumberOfUsers(){
        return repository.getNrOfEntities();
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

    public Iterable<User> filterByEmail(String searchText) {
        return repository.filterByEmail(searchText);
    }

    public Optional<User> getByEmail(String email){
        return repository.getByEmail(email);
    }
}
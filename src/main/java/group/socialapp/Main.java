package group.socialapp;

import group.socialapp.Console.UI;
import group.socialapp.Repository.FriendshipDBRepository;
import group.socialapp.Repository.UserDBRepository;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import group.socialapp.Validators.FriendshipValidator;
import group.socialapp.Validators.UserValidator;

public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        UserDBRepository userRepo = new UserDBRepository(args[0], args[1], args[2]);
        ServiceUser ServiceUser = new ServiceUser(userRepo, userValidator);
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository(args[0], args[1], args[2]);
        ServiceFriendship ServiceFriendship = new ServiceFriendship(friendshipRepository,userRepo, friendshipValidator);

        UI ui = UI.getInstance();
        ui.setFriendshipService(ServiceFriendship);
        ui.setUserService(ServiceUser);
        ui.run();
    }
}
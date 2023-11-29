package HarmonyHub;

import HarmonyHub.Console.UI;
import HarmonyHub.Domain.User;
import HarmonyHub.Repository.FriendshipDBRepository;
import HarmonyHub.Repository.Paging.Page;
import HarmonyHub.Repository.Paging.Pageable;
import HarmonyHub.Repository.Paging.PageableImplementation;
import HarmonyHub.Repository.Paging.PagingRepository;
import HarmonyHub.Repository.RequestDBRepository;
import HarmonyHub.Repository.UserDBPagingRepository;
import HarmonyHub.Repository.UserDBRepository;
import HarmonyHub.Service.ServiceFriendship;
import HarmonyHub.Service.ServiceUser;
import HarmonyHub.Validators.FriendshipValidator;
import HarmonyHub.Validators.UserValidator;

public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        UserDBRepository userRepo = new UserDBRepository(args[0], args[1], args[2]);
        PagingRepository<String, User> userDBPagingRepository = new UserDBPagingRepository(args[0], args[1], args[2], args[0], args[1], args[2]);
        ServiceUser ServiceUser = new ServiceUser(userDBPagingRepository, userValidator);
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipDBRepository friendshipRepository = new FriendshipDBRepository(args[0], args[1], args[2]);
        RequestDBRepository requestDBRepository = new RequestDBRepository(args[0], args[1], args[2]);
        ServiceFriendship ServiceFriendship = new ServiceFriendship(friendshipRepository,userRepo, requestDBRepository, friendshipValidator);

        Pageable pageable = new PageableImplementation(2,4);
        Page<User> userPage = userDBPagingRepository.findAll(pageable);
        userPage.getContent().forEach(System.out::println);

        UI ui = UI.getInstance();
        ui.setFriendshipService(ServiceFriendship);
        ui.setUserService(ServiceUser);
        ui.run();
    }
}
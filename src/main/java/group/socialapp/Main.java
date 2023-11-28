package group.socialapp;

import group.socialapp.Console.UI;
import group.socialapp.Domain.User;
import group.socialapp.Repository.FriendshipDBRepository;
import group.socialapp.Repository.Paging.Page;
import group.socialapp.Repository.Paging.Pageable;
import group.socialapp.Repository.Paging.PageableImplementation;
import group.socialapp.Repository.Paging.PagingRepository;
import group.socialapp.Repository.RequestDBRepository;
import group.socialapp.Repository.UserDBPagingRepository;
import group.socialapp.Repository.UserDBRepository;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import group.socialapp.Validators.FriendshipValidator;
import group.socialapp.Validators.UserValidator;

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
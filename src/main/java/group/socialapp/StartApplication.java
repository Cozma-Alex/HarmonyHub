package group.socialapp;

import group.socialapp.Repository.FriendshipDBRepository;
import group.socialapp.Repository.UserDBRepository;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import group.socialapp.Validators.FriendshipValidator;
import group.socialapp.Validators.UserValidator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String url ="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "root";
        String password = "Goacamela4@";

        UserValidator userValidator = new UserValidator();
        UserDBRepository userDBRepository = new UserDBRepository(url,username,password);
        ServiceUser serviceUser = new ServiceUser(userDBRepository,userValidator);

        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url ,username, password);
        ServiceFriendship serviceFriendship = new ServiceFriendship(friendshipDBRepository, userDBRepository, friendshipValidator);

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
        primaryStage.setScene(scene);

        UserController userController = userLoader.getController();
        userController.setService(serviceUser,serviceFriendship);

        primaryStage.setWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}

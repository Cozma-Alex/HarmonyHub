package HarmonyHub;

import HarmonyHub.GUI.Controllers.LandingPageController;
import HarmonyHub.GUI.Controllers.RegisterPageController;
import HarmonyHub.Repository.FriendshipDBRepository;
import HarmonyHub.Repository.RequestDBRepository;
import HarmonyHub.Repository.UserDBRepository;
import HarmonyHub.Service.ServiceFriendship;
import HarmonyHub.Service.ServiceUser;
import HarmonyHub.Validators.FriendshipValidator;
import HarmonyHub.Validators.UserValidator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class StartApplication extends Application {

    private static String url;
    private static String username;
    private static String password;

    @Override
    public void start(Stage primaryStage) throws Exception {

        UserValidator userValidator = new UserValidator();
        UserDBRepository userDBRepository = new UserDBRepository(url,username,password);
        ServiceUser serviceUser = new ServiceUser(userDBRepository,userValidator);

        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url ,username, password);
        RequestDBRepository requestRepository = new RequestDBRepository(url ,username, password);
        ServiceFriendship serviceFriendship = new ServiceFriendship(friendshipDBRepository, userDBRepository, requestRepository, friendshipValidator);

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("GUI/Controllers/views/register-page-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        primaryStage.setScene(scene);

        RegisterPageController landingPageController = userLoader.getController();
        landingPageController.setData(serviceUser, serviceFriendship, primaryStage);

        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.show();
    }

    public static void main(String[] args){
        url = args[0];
        username = args[1];
        password = args[2];
        launch(args);
    }

}

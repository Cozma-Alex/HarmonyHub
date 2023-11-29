package HarmonyHub.Console;

import HarmonyHub.Domain.Friendship;
import HarmonyHub.Service.ServiceFriendship;
import HarmonyHub.Service.ServiceUser;
import HarmonyHub.Domain.Pair;
import HarmonyHub.Domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UI {

    static UI instance;

    public static UI getInstance() {

        if(instance == null)
        {
            instance = new UI();
        }

        return instance;
    }

    private ServiceFriendship friendshipService;

    private ServiceUser userService;

    public void setFriendshipService(ServiceFriendship friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setUserService(ServiceUser userService) {
        this.userService = userService;
    }

    public void run(){

        while (true){
            System.out.println();
            System.out.println("1. Add an entity");
            System.out.println("2. Remove an entity");
            System.out.println("3. Update an entity");
            System.out.println("4. Print all entities");
            System.out.println("5. Number of communities");
            System.out.println("6. The most sociable community");
            System.out.println("7. Search for an entity by id");
            System.out.println("8. Print all friends from one month");
            System.out.println("0. Exit");
            System.out.print("Enter the command:");
            Scanner sc = new Scanner(System.in);
            int command = 0;
            try {
                command = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Comanda incorecta");
            }
            int cmd;
            String str;
            String[] data;
            switch (command){
                case(1):
                    System.out.println();
                    System.out.println("1. Add an User");
                    System.out.println("2. Add a Friendship");
                    System.out.print("Enter the command and data:");
                    cmd = sc.nextInt();

                    try {
                        str = sc.nextLine().split(" ")[1];
                    } catch (Exception e) {
                        System.out.println("Number of parameters is incorrect");
                        break;
                    }

                    data = str.split("\\|");


                    if (cmd == 1){

                        if (data.length != 4)
                        {
                            System.out.println("Number of parameters is incorrect");

                            break;
                        }

                        try {
                            userService.addOneUser(data[0],data[1], data[2], data[3]);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                    }

                    else if (cmd == 2)
                    {

                        if (data.length != 2)
                        {
                            System.out.println("Number of parameters is incorrect");

                            break;
                        }

                        String id1=data[0],id2=data[1];

                        try{
                            friendshipService.addOneFriendship(id1,id2);
                        } catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                    }

                    else{
                        System.out.println("Incorrect command");
                    }

                    break;

                case (2):
                    System.out.println("1. Remove an User");
                    System.out.println("2. Remove a friendship");
                    System.out.print("Enter the command and data:");

                    cmd = sc.nextInt();
                    try {
                        str = sc.nextLine().split(" ")[1];
                    } catch (Exception e) {
                        System.out.println("Number of parameters is incorrect");
                        break;
                    }
                    data = str.split("\\|");

                    if (cmd == 1){

                        if (data.length != 1)
                        {
                            System.out.println("Number of parameters is incorrect");

                            break;
                        }

                        String id = data[0];
                        try {
                            friendshipService.deleteUser(id);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                    }

                    else if (cmd == 2){

                        if (data.length != 2)
                        {
                            System.out.println("Number of parameters is incorrect");
                            break;
                        }

                        String id1,id2;

                        try {
                            id1 = data[0];
                            id2 = data[1];
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ids");
                            break;
                        }

                        try{
                            friendshipService.removeFriendship(new Pair<>(id1,id2));
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                    }

                    else{
                        System.out.println("Incorrect command");
                    }

                    break;

                case (3):
                    System.out.println("1. Update an User");

                    cmd = sc.nextInt();
                    try {
                        str = sc.nextLine().split(" ")[1];
                    } catch (Exception e) {
                        System.out.println("Number of parameters is incorrect");
                        break;
                    }
                    data = str.split("\\|");

                    if (cmd == 1){

                        if (data.length != 5)
                        {
                            System.out.println("Number of parameters is incorrect");

                            break;
                        }

                        try{
                            userService.updateAnUser(data[0],data[1],data[2], data[3], data[4]);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                    else {
                        System.out.println("Incorrect command");
                    }

                    break;

                case (4):

                    System.out.println("1. Print all users");
                    System.out.println("2. Print all friendships");
                    System.out.print("Enter command:");

                    cmd = sc.nextInt();

                    if (cmd == 1)
                    {
                        Iterable<User> users = userService.getAll();

                        System.out.println();

                        if (userService.getNumberOfUsers() == 0)
                        {
                            System.out.println("There are no users");
                        }

                        System.out.println("Users:");

                        users.forEach(System.out::println);

                    }

                    else if (cmd == 2)
                    {
                        Iterable<Friendship> friendships = friendshipService.getAll();

                        System.out.println();

                        if (friendshipService.getNumberOfFriendships() == 0)
                        {
                            System.out.println("There are no friendships");
                        }

                        System.out.println("Friendships:");
                        friendships.forEach(System.out::println);

                    }
                    else {
                        System.out.println("Incorrect command");
                    }

                    break;

                case (5):
                    int number_of_communities = friendshipService.numberOfCommunities();

                    System.out.println();
                    System.out.println("Number of communities is: " + number_of_communities);

                    break;

                case (6):

                    List<User> list = friendshipService.mostSociableCommunity();

                    System.out.println();
                    System.out.println("The most sociable community:");

                    list.forEach(System.out::println);

                    break;

                case (7):

                    System.out.println();
                    System.out.println("1. Search for User");
                    System.out.println("2. Search for Friendship");
                    System.out.print("Enter the command and data:");

                    cmd = sc.nextInt();
                    try {
                        str = sc.nextLine().split(" ")[1];
                    } catch (Exception e) {
                        System.out.println("Number of parameters is incorrect");
                        break;
                    }
                    data = str.split("\\|");

                    if (cmd == 1)
                    {
                        if (data.length != 1)
                        {
                            System.out.println("Number of parameters is incorrect");
                        }

                        try{
                            Optional<User> user = userService.searchUserById(data[0]);
                            System.out.println("The user is: " + user.get());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    else if (cmd == 2)
                    {

                        if (data.length != 1)
                        {
                            System.out.println("Number of parameters is incorrect");
                        }

                        try{
                            Optional<Friendship> friendship = friendshipService.searchFriendshipById(new Pair<>(data[0],data[1]));
                            System.out.println("The user is: " + friendship.get());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    else{
                        System.out.println("Incorrect command");
                    }

                    break;

                case(8):

                    try {
                        str = sc.nextLine().split(" ")[1];
                        data = str.split("\\|");
                    } catch (Exception e) {
                        System.out.println("Number of parameters is incorrect");
                        break;
                    }

                    try {
                        String id = data[0];
                        int month = Integer.parseInt(data[1]);
                        List<String> result = friendshipService.friendsFromMonth(id, month);

                        result.forEach(System.out::println);
                        break;

                    } catch (Exception e) {
                        System.out.println("Invalid data");
                        break;
                    }

                case (0): return;

                default:
                    System.out.println("Invalid command");
            }

        }

    }

}

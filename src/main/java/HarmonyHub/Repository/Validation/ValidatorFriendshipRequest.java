package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.FriendshipRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidatorFriendshipRequest implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest friendshipRequest) throws ValidationException {
        String str = "";

        if (friendshipRequest.getUserSender() == null){
            str += "ID user sender cannot be null \n";
        }

        if (friendshipRequest.getUserReceiver() == null){
            str += "ID user receiver cannot be null \n";
        }

        if (friendshipRequest.getDate().isAfter(LocalDateTime.now())){
            str += "Date cannot be in the future \n";
        }

        List<String> statuses = List.of("Accept","Pending","Deny");

        if(!statuses.contains(friendshipRequest.getStatus())){
            str += "Status should be Accept, Pending or Deny";
        }

        if (!str.isEmpty()){
            throw new ValidationException(str);
        }

    }
}

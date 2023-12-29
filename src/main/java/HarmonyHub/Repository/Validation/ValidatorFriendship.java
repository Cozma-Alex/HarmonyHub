package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.Friendship;

import java.time.LocalDateTime;

public class ValidatorFriendship implements Validator<Friendship> {
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        String str = "";

        if (friendship.getId() == null && friendship.getId().getLeft() == null && friendship.getId().getRight() == null)
        {
            str += "Friendship id cannot be null \n";
        }

        if (friendship.getDateFriendship() == null)
        {
            str += "Date cannot be null \n";
        }

        if (friendship.getDateFriendship().isAfter(LocalDateTime.now())){
            str += "Date cannot in the future \n";
        }

        if (!str.isEmpty()){
            throw new ValidationException(str);
        }

    }

}

package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.Chat;

public class ValidatorChat implements Validator<Chat> {
    @Override
    public void validate(Chat chat) throws ValidationException {
        String str = "";

        if (chat.getChatName().isEmpty()){
            str += "Chat name cannot be empty \n";
        }

        if (chat.getIdTheme() == null){
            str += "ID chat theme cannot be null \n";
        }

        if (chat.getUsers().isEmpty()){
            str += "Chat has to have users \n";
        }

        if (!str.isEmpty()){
            throw new ValidationException(str);
        }

    }
}

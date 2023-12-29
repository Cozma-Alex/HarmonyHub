package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.Message;

import java.time.LocalDateTime;

public class ValidatorMessage implements Validator<Message> {
    @Override
    public void validate(Message message) throws ValidationException {
        String str = "";

        if (message.getIdChat() == null){
            str += "ID chat cannot be null \n";
        }

        if (message.getIdFrom() == null){
            str += "ID from cannot be null \n";
        }

        if (message.getMessage().isEmpty()){
            str += "Message cannot be empty \n";
        }

        if (message.getDate().isAfter(LocalDateTime.now())){
            str += "Date cannot be in the future \n";
        }

        if (message.getReceivers().isEmpty()){
            str += "Message has to have receivers \n";
        }

        if (!str.isEmpty()){
            throw new ValidationException(str);
        }

    }
}

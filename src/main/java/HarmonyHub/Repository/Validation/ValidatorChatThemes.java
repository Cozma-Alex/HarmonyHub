package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.ChatTheme;

public class ValidatorChatThemes implements Validator<ChatTheme> {
    @Override
    public void validate(ChatTheme theme) throws ValidationException {
        String str = "";

        if (theme.getThemeName().isEmpty()){
            str += "Theme name cannot be empty \n";
        }

        if (theme.getBackgroundModel().isEmpty()){
            str += "Background model cannot be empty \n";
        }

        if (theme.getUserColor().isEmpty()){
            str += "User color cannot be empty \n";
        }

        if (theme.getOtherColor().isEmpty()){
            str += "Other color cannot be empty \n";
        }

        if (!str.isEmpty())
        {
            throw new ValidationException(str);
        }
    }
}

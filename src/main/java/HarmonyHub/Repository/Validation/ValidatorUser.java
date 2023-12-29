package HarmonyHub.Repository.Validation;

import HarmonyHub.Models.User;

public class ValidatorUser implements Validator<User> {
    @Override
    public void validate(User user) throws ValidationException{
        String str = "";

        if (user.getFirstName().isEmpty()){
            str += "First name cannot be empty \n";
        }

        if (user.getLastName().isEmpty()){
            str += "Last name cannot be empty \n";
        }

        if (user.getUsername().isEmpty()){
            str += "Username cannot be empty \n";
        }

        if (user.getEmail().isEmpty()){
            str += "Email cannot be empty \n";
        }

        if (user.getPasswordHash().isEmpty()){
            str += "Password cannot be empty \n";
        }

        if (!str.isEmpty()){
            throw new ValidationException(str);
        }

    }
}

package group.socialapp.Validators;

import group.socialapp.Domain.User;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User user) throws ValidationException {

        if (user.getFirstName().isEmpty())
        {
            throw new ValidationException("First name cannot be empty");
        }

        if(user.getLastName().isEmpty()){
            throw new ValidationException("Last name cannot be empty");
        }
    }
}

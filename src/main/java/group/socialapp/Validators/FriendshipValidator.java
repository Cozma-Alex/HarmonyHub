package group.socialapp.Validators;

import group.socialapp.Domain.Friendship;

import java.util.Objects;

public class FriendshipValidator implements Validator<Friendship>{

    @Override
    public void validate(Friendship entity) throws ValidationException {


        if (entity.getDate() == null)
        {
            throw new ValidationException("date must not be null");
        }

        if (entity.getId().getRight().isEmpty()|| entity.getId().getLeft().isEmpty()){
            throw new ValidationException("ID must not be empty");
        }

        if (Objects.equals(entity.getId().getRight(), entity.getId().getLeft()))
        {
            throw new ValidationException("Ids must be different");
        }

    }

}

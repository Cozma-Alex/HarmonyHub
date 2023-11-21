package group.socialapp.Validators;

public interface Validator<T>{

    void validate(T entity) throws ValidationException;

}

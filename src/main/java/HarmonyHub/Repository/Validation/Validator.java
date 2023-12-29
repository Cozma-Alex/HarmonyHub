package HarmonyHub.Repository.Validation;

public interface Validator<T>{

    void validate(T entity) throws ValidationException;

}
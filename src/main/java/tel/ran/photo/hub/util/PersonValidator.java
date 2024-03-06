package tel.ran.photo.hub.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tel.ran.photo.hub.model.Person;
import tel.ran.photo.hub.service.PersonDetailsService;

@Component
public class PersonValidator implements Validator {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (personDetailsService.getByEmail(person.getEmail()).isPresent()){
            errors.rejectValue("email", "", "email already in use");
        }
        if (personDetailsService.getByUsername(person.getUsername()).isPresent()){
            errors.rejectValue("username", "", "username already in use");
        }
    }
}

package tel.ran.photo.hub.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tel.ran.photo.hub.model.AccountType;
import tel.ran.photo.hub.model.Person;
import tel.ran.photo.hub.service.RegistrationService;
import tel.ran.photo.hub.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                                      @RequestParam(name = "type") String type){
        switch (type){
            case "Publisher" -> person.setAccountType(AccountType.PUBLISHER);
            case "Consumer" -> person.setAccountType(AccountType.CONSUMER);
        }

        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        registrationService.register(person);
        return "redirect:/auth/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/auth/login";
    }
}

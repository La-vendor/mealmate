package com.lavendor.mealmate.security;

import com.lavendor.mealmate.user.User;
import com.lavendor.mealmate.user.UserDTO;
import com.lavendor.mealmate.user.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public RedirectView signupUser(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes attributes) {
        String errorMessage = null;

        if (bindingResult.hasErrors()) {
            errorMessage = getErrorMessage(bindingResult);
        }else if(!userService.checkIfPasswordIsEqual(userDTO)){
            errorMessage = "Password and confirm password do not match";
        } else {
            try {
                User savedUser = userService.createUser(userDTO);
                if (savedUser != null) {
                    userService.addStarterDataToUser(savedUser);
                    attributes.addFlashAttribute("signupSuccess", true);
                    return new RedirectView("/login", true);
                }
            } catch (RuntimeException e) {
                errorMessage = e.getMessage();
            }
        }

        errorMessage = (errorMessage == null) ? "Signup failed. Please try again." : errorMessage;
        attributes.addFlashAttribute("signupError", errorMessage);
        return new RedirectView("/signup", true);
    }

    private static String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(". "));
    }
}


package space.artway.artwayuser.controller;

import space.artway.artwayuser.controller.utils.ManagedUser;
import space.artway.artwayuser.service.UserService;
import space.artway.artwayuser.service.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody ManagedUser user){
//        if(!passwordChecker(managedUser.getPassword())){
//            throw new InvalidPasswordException();

         userService.createUser(user, user.getPassword());
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam Long code){
        userService.activate(code);

    }
}

package space.artway.artwayuser.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoPermissionException extends RuntimeException{

    public NoPermissionException(String msg){
        super(msg);
    }
}

package tech.xavi.generalabe.service.user;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.repository.GeneralaUserRepository;

@AllArgsConstructor
@Service
public class CommonUserService {

    private final GeneralaUserRepository generalaUserRepository;


    public void updateUserData(GeneralaUser user){
        generalaUserRepository.save(user);
    }

    public GeneralaUser findByUserId(String id){
        System.out.println(id);
        return generalaUserRepository.findById(id)
                .orElseThrow(
                        () -> new GeneralaException(GeneralaError.UserIdNotFound, HttpStatus.UNAUTHORIZED)
                );
    }

    public GeneralaUser getAuthenticatedPlayer(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByUserId(auth.getPrincipal().toString());
    }
}

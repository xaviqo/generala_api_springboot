package tech.xavi.generalabe.service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.repository.GeneralaUserRepository;
import tech.xavi.generalabe.utils.UUIDGenerala;

@AllArgsConstructor
@Service
public class UserService {

    private final GeneralaUserRepository generalaUserRepository;
    private final BCryptPasswordEncoder bcPasswordEncoder;

    public GeneralaUser createUserAndSave(String nickname, String password, boolean admin){

        String id = "";

        if (admin) {
            id = UUIDGenerala.generateAdminId(nickname);
        } else {
            id = UUIDGenerala.generatePlayerId(nickname);
        }

        return generalaUserRepository.save(
                GeneralaUser.builder()
                        .id(id)
                        .password(bcPasswordEncoder.encode(password))
                        .nickname(nickname)
                        .build()
        );
    }





}

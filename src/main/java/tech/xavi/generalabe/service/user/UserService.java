package tech.xavi.generalabe.service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.constant.Global;
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

    public GeneralaUser createUserAndSave(String nickname, String password){

        if (nickname.isBlank() || nickname.length() < Global.NICKNAME_MIN_LEN)
            throw new GeneralaException(GeneralaError.NicknameMinLength,HttpStatus.BAD_REQUEST);

        if (nickname.length() > Global.NICKNAME_MAX_LEN)
            throw new GeneralaException(GeneralaError.NicknameMaxLength,HttpStatus.BAD_REQUEST);

        if (!nickname.matches(Global.REGEX_LETTERS_NUMBERS))
            throw new GeneralaException(GeneralaError.NicknameOnlyAZ09,HttpStatus.BAD_REQUEST);

        return generalaUserRepository.save(
                GeneralaUser.builder()
                        .password(bcPasswordEncoder.encode(password))
                        .nickname(nickname)
                        .build()
        );
    }







}

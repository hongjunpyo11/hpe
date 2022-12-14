package com.example.hellpyending.user;

import com.example.hellpyending.user.entity.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    String login(UserCreateForm userCreateForm){
        return "user_login";
    }
    @GetMapping("/signup")
    String signUp(UserCreateForm userCreateForm){
        return "user_signup";
    }
    @PostMapping("/signup")
    String signUp(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){

        String birth = "%s-%s-%s".formatted(userCreateForm.getYear(),userCreateForm.getMonth(),userCreateForm.getDay());
        LocalDate birthday = LocalDate.parse(birth, DateTimeFormatter.ISO_DATE);

        if (bindingResult.hasErrors()) {
            return "user_signup";
        }
        if (!addressCheck(userCreateForm.getAddress_1st(), userCreateForm.getAddress_2st(),userCreateForm.getAddress_3st(),userCreateForm.getAddress_4st())) {
            bindingResult.rejectValue("address_1st", "addressNotInput",
                    "주소 입력은 필수 항목입니다.");
            return "user_signup";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user_signup";
        }
        try {
            userService.create(
                    userCreateForm.getUsername(),
                    userCreateForm.getPassword1(),
                    userCreateForm.getSex(),
                    userCreateForm.getEmail(),
                    userCreateForm.getPhoneNumber(),
                    birthday,
                    userCreateForm.getAddress_1st(),
                    userCreateForm.getAddress_2st(),
                    userCreateForm.getAddress_3st(),
                    userCreateForm.getAddress_4st(),
                    userCreateForm.getAddress_detail()
                    );
        }
        catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "user_signup";
        }
        catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "user_signup";
        }
        return "redirect:/";
    }

    private boolean addressCheck(String address_1st, String address_2st, String address_3st, String address_4st) {
        if (address_1st.trim().length() == 0 || address_2st.trim().length() == 0 || address_3st.trim().length() == 0 || address_4st.trim().length() == 0 )
        {
            return false;
        }
        return true;
    }

    //    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    @ResponseBody
    public Authentication user(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    @ResponseBody
    public Authentication admin(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

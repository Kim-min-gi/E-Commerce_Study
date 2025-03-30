package com.study.ecommerce.service;

import com.study.ecommerce.config.jwt.JwtUtil;
import com.study.ecommerce.domain.Member;
import com.study.ecommerce.domain.RefreshToken;
import com.study.ecommerce.exception.AlreadyExistsEmailException;
import com.study.ecommerce.exception.ExpiredRefreshTokenException;
import com.study.ecommerce.exception.NotFoundMemberException;
import com.study.ecommerce.exception.ResignUnauthorizedException;
import com.study.ecommerce.repository.MemberRepository;
import com.study.ecommerce.repository.RefreshTokenRepository;
import com.study.ecommerce.request.MemberRequest;
import com.study.ecommerce.request.MemberSignUp;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    public void signup(MemberSignUp memberSignUp){

        Member saveMember = duplicateCheckAndSignup(memberSignUp,"ROLE_USER");

        memberRepository.save(saveMember);

    }


    public void adminSignup(MemberSignUp memberSignUp){

        Member saveMember = duplicateCheckAndSignup(memberSignUp,"ROLE_ADMIN");

        memberRepository.save(saveMember);

    }

    @Transactional
    public void resign(MemberRequest memberRequest){

        Member member = memberRepository.findById(memberRequest.getId()).orElseThrow(NotFoundMemberException::new);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean matches = passwordEncoder.matches(memberRequest.getPassword(), member.getPassword());


        if (member.getEmail().equals(email) && matches){
            memberRepository.delete(member);
        }else{
            throw new ResignUnauthorizedException();
        }

    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie c : cookies){
            if (c.getName().equals("refresh")){
                refresh = c.getValue();
            }
        }

        if(refresh == null){
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);
        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        if (!category.equals("refresh")){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //refreshToken 만료 확인
        refreshTokenRepository.findById(email).orElseThrow(ExpiredRefreshTokenException::new);

        String newAccessToken = jwtUtil.createToken("Authorization",email,role,600000L);
        String newRefreshToken = jwtUtil.createToken("refresh",email,role,86400000L);

        //redis refreshToken 삭제 후 저장
        refreshTokenRepository.deleteById(email);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(newRefreshToken)
                .email(email)
                .build();

        refreshTokenRepository.save(refreshToken);

        response.setHeader("Authorization","Bearer " + newAccessToken);
        response.addCookie(createCookie("refresh",newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }


    public Member duplicateCheckAndSignup(MemberSignUp memberSignUp,String role){
        Optional<Member> findMember = memberRepository.findByEmail(memberSignUp.getEmail());

        //중복체크
        if (findMember.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(memberSignUp.getPassword());


        return Member.form(memberSignUp,encryptedPassword,role);
    }


    private Cookie createCookie(String key, String value){


        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);


        //cookie.setSecure(true); //https 용
        //cookie.setPath("/"); //쿠키가 적용될 범위

        return cookie;
    }


}

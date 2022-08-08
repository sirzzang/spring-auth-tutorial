package com.eraser.oauth.config.oauth;

import com.eraser.oauth.config.auth.PrincipalDetails;
import com.eraser.oauth.config.oauth.provider.FacebookUserInfo;
import com.eraser.oauth.config.oauth.provider.GoogleUserInfo;
import com.eraser.oauth.config.oauth.provider.NaverUserInfo;
import com.eraser.oauth.config.oauth.provider.Oauth2UserInfo;
import com.eraser.oauth.domain.User;
import com.eraser.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    /**
     * 구글 로그인 userRequest 데이터 후처리
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     *
     * UserRequest 객체
     * 1. 구글 로그인 버튼 클릭
     * 2. 구글 로그인 창
     * 3. 구글 로그인 완료
     * 4. code 리턴(Oauth-Client 라이브러리)
     * 5. AccessToken 요청
     *
     * loadUser 함수: UserRequest 객체를 통해 회원 프로필을 받을 수 있음. OAuth2-Client 라이브러리가 받아 주는 것
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /**
         * OAuth2UserRequest 객체
         * 1. ClientRegistration: 클라이언트(내가 만들고 있는 앱) 정보
         *  - registrationId: 어떤 oauth 로그인하는지
         * 2. AccessToken
         * 3. AdditionalParameters
         */
//        log.info("userRequest.ClientRegistration: {}", userRequest.getClientRegistration());
//        log.info("userRequest.AccessToken: {}", userRequest.getAccessToken());
//        log.info("userRequest.AdditionalParameters: {}", userRequest.getAdditionalParameters());
//        log.info("userRequest.getAttributes: {}, 상위 클래스에 있음", super.loadUser(userRequest).getAttributes());

        // 유저 정보를 받아올 수 있음
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth user info: {}", oAuth2User.getAttributes());

        // 소셜 로그인 제공자 별 회원 정보
        Oauth2UserInfo oauth2UserInfo = null;
        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oauth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            /**
             * 네이버 반환값 형식
             * response: {
             *     resultCode,
             *     message,
             *     response: {
             *         id,
             *         name,
             *         email,
             *         ...
             *     }
             * }
             */
            oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("google, facebook, naver 로그인만 지원합니다.");
        }

        // 유저 객체에 저장할 정보
        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("getInThere"); // oauth2 로그인의 경우 크게 의미가 없음
        String email = oauth2UserInfo.getEmail();
        String role = "ROLE_USER"; // 서비스 별 이용 권한에 맞게 변경

        // 저장할 유저 객체
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            log.info("oauth 계정 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            log.info("oauth 계정 로그인을 진행한 적이 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}

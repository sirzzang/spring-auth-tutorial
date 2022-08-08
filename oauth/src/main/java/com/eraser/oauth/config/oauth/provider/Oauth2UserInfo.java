package com.eraser.oauth.config.oauth.provider;

public interface Oauth2UserInfo {

    /**
     * 소셜 로그인 provider 별로 다른 정보가 제공되기 때문에
     *
     * @return
     */


    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}

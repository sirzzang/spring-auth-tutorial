package com.eraser.oauth.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements Oauth2UserInfo {

    private Map<String, Object> attributes;

    /**
     * NaverUserInfo attribute 예시
     * { id: xxx, email: xxx, name: xxx }
     */
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return null;
    }
}

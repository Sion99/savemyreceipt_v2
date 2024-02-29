package com.smr.savemyreceipt_v2.oauth2;

import java.util.Map;

public class OAuth2UserInfo {

    private Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        // Google OAuth2 경우 "sub"를 사용
        return (String) attributes.get("sub");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getPicture() {
        return (String) attributes.get("picture");
    }

}

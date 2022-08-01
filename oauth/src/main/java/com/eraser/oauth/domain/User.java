package com.eraser.oauth.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    /**
     * Oauth attribute 정보로 강제 회원가입되는 경우
     * username: google_112042671032297239028
     * password: 암호화(겟인데어)
     * email: sir950123@gmail.com
     * role: ROLE_USER
     * provider: google
     * providerId: 112042671032297239028
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private String role;

    @CreationTimestamp
    private Timestamp createDate;

    private String provider; // oauth provider
    private String providerId; // oauth provider id

    @Builder
    public User(String username, String password, String email, String role,
                String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}

package com.ltu.model.response;

import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.CommonRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class LoginResponse extends CommonRequest {

    private static final long serialVersionUID = 1L;

    private UserEntity user;
    /**
     * toke
     */
    private String token;

    public static LoginResponse valueOfSusscess(UserEntity user, String token) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }
}

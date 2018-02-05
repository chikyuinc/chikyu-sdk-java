package net.chikyu.chikyu_sdk_java.model.session.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

public class TokenRequestModel extends ApiDataModel {
    @JsonProperty(value="token_name")
    public String tokenName;

    @JsonProperty(value="email")
    public String email;

    @JsonProperty(value="password")
    public String password;
}


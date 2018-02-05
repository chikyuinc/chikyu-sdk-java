package net.chikyu.chikyu_sdk_java.model.session.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.chikyu.chikyu_sdk_java.model.ApiDataModel;

import java.beans.Transient;

public class SendTokenRequestModel extends ApiDataModel {
    @JsonProperty(value="token_name")
    public String tokenName;

    @JsonProperty(value="login_token")
    public String loginToken;

    @JsonProperty(value="login_secret_token")
    public String loginSecretToken;

    @Transient
    public SendTokenRequestModel withTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    @Transient
    public SendTokenRequestModel withLoginToken(String loginToken) {
        this.loginToken = loginToken;
        return this;
    }

    @Transient
    public SendTokenRequestModel withLoginSecretToken(String loginSecretToken) {
        this.loginSecretToken = loginSecretToken;
        return this;
    }

}

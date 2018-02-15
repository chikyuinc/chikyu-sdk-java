package net.chikyu.chikyu.sdk.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleWithWebIdentityResult;
import net.chikyu.chikyu.sdk.OpenResource;
import net.chikyu.chikyu.sdk.SecureResource;
import net.chikyu.chikyu.sdk.config.Config;
import net.chikyu.chikyu.sdk.exception.ApiCallException;
import net.chikyu.chikyu.sdk.model.ApiDataResponse;
import net.chikyu.chikyu.sdk.model.ApiRequest;
import net.chikyu.chikyu.sdk.model.ApiResponse;
import net.chikyu.chikyu.sdk.model.generic.GenericApiRequest;
import net.chikyu.chikyu.sdk.model.session.ChangeOrganRequestModel;
import net.chikyu.chikyu.sdk.model.session.ChangeOrganResponseModel;
import net.chikyu.chikyu.sdk.model.session.LoginResponseModel;
import net.chikyu.chikyu.sdk.model.session.token.SendTokenRequestModel;

import java.io.IOException;

public class Session {
    private SessionData sessionData;

    private Session(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public static Session login(SendTokenRequestModel model) throws IOException, ApiCallException {
        OpenResource resource = new OpenResource();
        ApiRequest<SendTokenRequestModel> req = new ApiRequest<SendTokenRequestModel>().withData(model);

        LoginResponse res = resource.invoke("/session/login", req, LoginResponse.class);
        BasicSessionCredentials credentials = createCredentials(res.data);

        SessionData sessionData = new SessionData(credentials, res.data.sessionId, res.data.identityId, res.data.apiKey);

        return new Session(sessionData);
    }

    public Session changeOrgan(int organId) throws IOException, ApiCallException {
        ChangeOrganRequestModel data = new ChangeOrganRequestModel();
        data.targetOrganId = organId;
        ApiRequest<ChangeOrganRequestModel> req = new ApiRequest<ChangeOrganRequestModel>().withData(data);

        ChangeOrganResponse res =
            new SecureResource(this).invoke("/session/organ/change", req, ChangeOrganResponse.class);

        this.sessionData = new SessionData(
                sessionData.getCredentials(), sessionData.getSessionId(), sessionData.getIdentityId(), res.data.apiKey);

        return this;
    }

    public boolean logout() throws IOException, ApiCallException {
        ApiResponse res = new SecureResource(this).invoke(
                "/session/logout", new GenericApiRequest(), ApiResponse.class);
        return !res.hasError;
    }

    public SessionData data() {
        return sessionData;
    }

    private static BasicSessionCredentials createCredentials(LoginResponseModel model) {
        class DummyProvider implements AWSCredentialsProvider {
            @Override
            public AWSCredentials getCredentials() {
                return new AnonymousAWSCredentials();
            }

            @Override
            public void refresh() {

            }
        }

        AWSSecurityTokenService stsService = AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new DummyProvider())
                .withRegion(Config.getAwsRegionName())
                .build();

        AssumeRoleWithWebIdentityRequest req = new AssumeRoleWithWebIdentityRequest()
                .withRoleArn(Config.getAwsIamRoleId())
                .withWebIdentityToken(model.cognitoToken)
                .withRoleSessionName(Config.getAwsServiceName());

        AssumeRoleWithWebIdentityResult stsResult = stsService.assumeRoleWithWebIdentity(req);

        return new BasicSessionCredentials(stsResult.getCredentials().getAccessKeyId(),
                stsResult.getCredentials().getSecretAccessKey(), stsResult.getCredentials().getSessionToken());
    }
}


class LoginResponse extends ApiDataResponse<LoginResponseModel> {

}

class ChangeOrganResponse extends ApiDataResponse<ChangeOrganResponseModel> {

}
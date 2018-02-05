package net.chikyu.chikyu_sdk_java;


import net.chikyu.chikyu_sdk_java.exception.ApiCallException;
import net.chikyu.chikyu_sdk_java.helper.RequestHelper;
import net.chikyu.chikyu_sdk_java.model.ApiModel;
import net.chikyu.chikyu_sdk_java.model.ApiRequest;
import net.chikyu.chikyu_sdk_java.model.ApiResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class OpenResource extends ApiResource {
    public OpenResource() {
        super();
    }

    @Override
    public<T> T invoke(String path, ApiRequest req, Class<T> cls) throws IOException, ApiCallException {
        String payload = req.toJson();

        HttpPost post = new HttpPost();
        post.setHeader("content-type", "application/json");
        post.setEntity(new StringEntity(payload, "utf8"));
        post.setURI(RequestHelper.buildUri("open", path));

        return handleResponse(client.execute(post), cls);
    }
}

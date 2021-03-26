package com.vjudge.ecnuvj.remote.provider.scu;

import com.vjudge.ecnuvj.httpclient.DedicatedHttpClient;
import com.vjudge.ecnuvj.httpclient.HttpStatusValidator;
import com.vjudge.ecnuvj.httpclient.SimpleNameValueEntityFactory;
import com.vjudge.ecnuvj.remote.account.RemoteAccount;
import com.vjudge.ecnuvj.remote.common.RemoteOjInfo;
import com.vjudge.ecnuvj.remote.submitter.ComplexSubmitter;
import com.vjudge.ecnuvj.remote.submitter.SubmissionInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SCUSubmitter extends ComplexSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return SCUInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/soj/solutions.action?userId=" + info.remoteAccountId + "&problemId=" + info.remoteProblemId).getBody();
        Matcher matcher = Pattern.compile("<td height=\"44\">(\\d+)</td>").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws ClientProtocolException, IOException {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "problemId", info.remoteProblemId, //
                "source", info.sourceCode, //
                "userId", remoteAccount.getAccountId(), //
                "password", remoteAccount.getPassword(), //
                "validation", getCaptcha(client)
        );
        client.post("/soj/submit.action", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
        return null;
    }

    private String getCaptcha(DedicatedHttpClient client) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet("/soj/validation_code");
        BufferedImage img = client.execute(get, new ResponseHandler<BufferedImage>() {
                    @Override
                    public BufferedImage handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        FileOutputStream fos = null;
                        try {
                            File captchaPic = File.createTempFile("scu", ".jpg");
                            fos = new FileOutputStream(captchaPic);
                            response.getEntity().writeTo(fos);
                            return ImageIO.read(captchaPic);
                        } finally {
                            fos.close();
                        }
                    }
                }
        );
        return SCUCaptchaRecognizer.recognize(img);
    }

}

package team.compass.common.utils;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.compass.user.domain.User;

@Component
@RequiredArgsConstructor
public class MailUtils {
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.mailjet.api-key}")
    private String apiKey;
    @Value("${spring.mail.mailjet.secret-key}")
    private String secretKey;

    private String SUBJECT = "비밀번호 초기화 안내메일 입니다.";


    public void sendMessage(User user) {
        MailjetClient client = new MailjetClient(apiKey, secretKey, new ClientOptions("v3.1"));
        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(
                                new JSONObject()
                                        .put(
                                                Emailv31.Message.FROM,
                                                new JSONObject().put("Email", from)
                                        )
                                        .put(
                                                Emailv31.Message.TO,
                                                new JSONArray().put(new JSONObject().put("Email", user.getEmail()))
                                        )
                                        .put(Emailv31.Message.SUBJECT, SUBJECT)
                                        .put(
                                                Emailv31.Message.HTMLPART,
                                                "<h2>안녕하세요." + user.getNickName() + "님!</h2>"
                                                + "<p>" + user.getResetPasswordKey() + "</p>"
                                                + "<p>비밀번호 초기화 코드입니다.<p>"
                                        )
                        )
                );


        try {
            // trigger the API call
            MailjetResponse response = client.post(request);
            // Read the response data and status
        } catch (MailjetSocketTimeoutException e) {
            throw new RuntimeException(e);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }
    }
}


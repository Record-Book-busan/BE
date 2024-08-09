package busim.kkilogbu.user.socialLogin;


import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class GoogleLoginStrategy implements SocialLoginStrategy {

    private static final String GOOGLE_API_URL = "https://oauth2.googleapis.com/tokeninfo";
    private final RestTemplate restTemplate;

    public GoogleLoginStrategy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JSONObject getUserInfo(String idToken) throws IOException {
        var uriBuilder = UriComponentsBuilder.fromHttpUrl(GOOGLE_API_URL)
                .queryParam("id_token", idToken);

        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return new JSONObject(response.getBody());
        } else {
            throw new IOException("구글 사용자 정보를 가져오는 데 실패했습니다.");
        }
    }
}

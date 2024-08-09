package busim.kkilogbu.user.socialLogin;


import org.json.JSONObject;

import java.io.IOException;

public interface SocialLoginStrategy {
    JSONObject getUserInfo(String token) throws IOException;
}

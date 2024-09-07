package busim.kkilogbu.user.appple.domain.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;


@JsonIgnoreProperties(ignoreUnknown = true)
public record AppleTokenResponse(

        @JsonProperty(value = "access_token")
        String accessToken,

        @JsonProperty(value = "expires_in")
        String expiresIn,

        @JsonProperty(value = "id_token")
        String idToken,

        @JsonProperty(value = "refresh_token")
        String refreshToken,

        @JsonProperty(value = "token_type")
         String tokenType,

         String error
) {
}

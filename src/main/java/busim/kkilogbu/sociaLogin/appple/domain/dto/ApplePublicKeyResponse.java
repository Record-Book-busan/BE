package busim.kkilogbu.sociaLogin.appple.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ApplePublicKeyResponse (List<AppleKeyInfo> keys
){
}

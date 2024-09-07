package busim.kkilogbu.user.appple.controller;


import busim.kkilogbu.user.appple.domain.dto.ApplePublicKeyResponse;
import busim.kkilogbu.user.appple.domain.dto.AppleRevokeRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth")
public interface AppleClient {

    @GetMapping(value = "/keys")
    ApplePublicKeyResponse findAppleAuthPublicKeys();

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenResponse findAppleToken(@RequestBody AppleTokenRequest request);

    @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
    void revoke(AppleRevokeRequest request);


}
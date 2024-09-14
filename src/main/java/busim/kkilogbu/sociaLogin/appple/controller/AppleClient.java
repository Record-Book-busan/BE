package busim.kkilogbu.sociaLogin.appple.controller;


import busim.kkilogbu.sociaLogin.appple.config.FeignConfig;
import busim.kkilogbu.sociaLogin.appple.domain.dto.ApplePublicKeyResponse;
import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleRevokeRequest;
import busim.kkilogbu.sociaLogin.appple.domain.dto.AppleTokenResponse;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignConfig.class)
public interface AppleClient {

    @GetMapping(value = "/keys")
    ApplePublicKeyResponse findAppleAuthPublicKeys();

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenResponse findAppleToken(
            @RequestParam("client_id") String clientId,
                                      @RequestParam("client_secret") String clientSecret,
                                      @RequestParam("grant_type") String grantType,
                                      @RequestParam("code") String code
    );


    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenResponse refreshAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("refresh_token") String refreshToken
    );

    @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
    void revoke(AppleRevokeRequest request);


}


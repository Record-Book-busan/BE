package busim.kkilogbu.user.appple.controller;


import busim.kkilogbu.user.appple.FeignConfig;
import busim.kkilogbu.user.appple.domain.dto.ApplePublicKeyResponse;
import busim.kkilogbu.user.appple.domain.dto.AppleRevokeRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenRequest;
import busim.kkilogbu.user.appple.domain.dto.AppleTokenResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
    void revoke(AppleRevokeRequest request);


}
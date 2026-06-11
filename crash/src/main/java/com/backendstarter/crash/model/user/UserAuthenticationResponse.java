package com.backendstarter.crash.model.user;

/**
 * AccessToken이 들어있는 JSON을 구성하는 Response
 */
public record UserAuthenticationResponse(String accessToken) {

}

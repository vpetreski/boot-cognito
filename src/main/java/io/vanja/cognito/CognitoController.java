package io.vanja.cognito;

import com.google.gson.Gson;
import io.vanja.cognito.dto.Result;
import io.vanja.cognito.dto.UserInfo;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@RestController
public class CognitoController {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final String CORRECT_VID = "2cc40d4d-36c7-4c60-b15c-761dded8abb5";

    @Value("${spring.security.oauth2.client.provider.cognito.user-info-uri}")
    private String userInfoUrl;

    @GetMapping("/unauthenticated")
    public Result authenticated() {
        return new Result("Free for all to see");
    }

    @GetMapping("/authenticated")
    public ResponseEntity<Result> unauthenticated(JwtAuthenticationToken principal) {
        return buildResponse(principal, false, false, "Free for all to see");
    }

    @GetMapping("/vendor-only")
    public ResponseEntity<Result> vendorOnly(JwtAuthenticationToken principal) {
        return buildResponse(principal, true, false, "You're a vendor now!");
    }

    @GetMapping("/vendor-vid-only")
    public ResponseEntity<Result> vendorVidOnly(JwtAuthenticationToken principal) {
        return buildResponse(principal, true, true, "Looks like you're a specific vendor!");
    }

    @SneakyThrows
    private ResponseEntity<Result> buildResponse(JwtAuthenticationToken principal, boolean checkGroup, boolean includeVid, String msg) {
        UserInfo userInfo = GSON.fromJson(HTTP_CLIENT.send(HttpRequest.newBuilder()
                .uri(new URI(userInfoUrl))
                .header("Authorization", "Bearer " + ((Jwt) principal.getPrincipal()).getTokenValue())
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString()).body(), UserInfo.class);

        Result result = new Result()
                .name(userInfo.getGivenName() + " " + userInfo.getFamilyName())
                .grantedAuthorities(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .scopes(Arrays.stream(((String) principal.getTokenAttributes().get("scope")).split(" ")).toList())
                .vid(includeVid ? userInfo.getVid() : null);

        List<String> groups = (List<String>) principal.getTokenAttributes().get("cognito:groups");
        return checkGroup && (groups == null || !groups.contains("VENDOR")) ||
                includeVid && (result.vid() == null || !result.vid().equalsIgnoreCase(CORRECT_VID)) ?
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() : ResponseEntity.ok(result.message(msg));
    }
}

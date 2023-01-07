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

    @Value("${spring.security.oauth2.client.provider.cognito.user-info-uri}")
    private String userInfoUrl;

    @GetMapping("/unauthenticated")
    public Result authenticated() {
        return new Result();
    }

    @GetMapping("/authenticated")
    public Result unauthenticated(JwtAuthenticationToken principal) {
        Result result = buildResult(principal);
        result.setGroups(null);
        result.setVid(null);
        return result;
    }

    @GetMapping("/vendor-only")
    public ResponseEntity<Result> vendorOnly(JwtAuthenticationToken principal) {
        Result result = buildResult(principal);

        if (!groupOk(result)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        result.setMessage("You're a vendor now!");
        result.setGroups(null);
        result.setVid(null);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/vendor-vid-only")
    public ResponseEntity<Result> vendorVidOnly(JwtAuthenticationToken principal) {
        Result result = buildResult(principal);

        if (!groupOk(result) || !vidOk(result)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        result.setMessage("Looks like you're a specific vendor!");
        result.setGroups(null);

        return ResponseEntity.ok(result);
    }

    private Result buildResult(JwtAuthenticationToken principal) {
        Result r = new Result();
        r.setGrantedAuthorities(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        r.setScopes(Arrays.stream(((String) principal.getTokenAttributes().get("scope")).split(" ")).toList());
        UserInfo userInfo = fetchUserInfo(principal);
        r.setName(userInfo.getGivenName() + " " + userInfo.getFamilyName());
        r.setVid(userInfo.getVid());
        r.setGroups((List<String>) principal.getTokenAttributes().get("cognito:groups"));
        return r;
    }

    @SneakyThrows
    private UserInfo fetchUserInfo(JwtAuthenticationToken principal) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(userInfoUrl))
                .header("Authorization", "Bearer " + ((Jwt) principal.getPrincipal()).getTokenValue())
                .GET()
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), UserInfo.class);
    }

    private boolean groupOk(Result result) {
        return result.getGroups() != null && result.getGroups().contains("VENDOR");
    }

    private boolean vidOk(Result result) {
        return result.getVid() != null && result.getVid().equalsIgnoreCase("2cc40d4d-36c7-4c60-b15c-761dded8abb5");
    }
}

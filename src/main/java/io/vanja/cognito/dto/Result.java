package io.vanja.cognito.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private String message;
    private String vid;
    private String name;
    private List<String> grantedAuthorities;
    private List<String> scopes;

    public Result() {
    }

    public Result(String message) {
        this.message = message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }
}

package io.vanja.cognito.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@Getter(onMethod = @__(@JsonProperty))
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
}

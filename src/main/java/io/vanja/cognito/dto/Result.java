package io.vanja.cognito.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    private String message = "Free for all to see";
    private String vid;
    private String name;
    private List<String> grantedAuthorities;
    private List<String> scopes;
    private List<String> groups;
}

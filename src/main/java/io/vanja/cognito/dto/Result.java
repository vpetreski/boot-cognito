package io.vanja.cognito.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
@Getter(onMethod = @__(@JsonProperty))
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {
    @NonNull
    private String message;
    private String vid;
    private String name;
    private List<String> grantedAuthorities;
    private List<String> scopes;
}

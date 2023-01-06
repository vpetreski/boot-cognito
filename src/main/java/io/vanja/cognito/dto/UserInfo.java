package io.vanja.cognito.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserInfo {
    @SerializedName(value = "given_name")
    private String givenName;
    @SerializedName(value = "family_name")
    private String familyName;
    @SerializedName(value = "custom:vid")
    private String vid;
}

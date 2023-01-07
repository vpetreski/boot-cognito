# Boot-Cognito 🔒

Secure REST API via AWS Cognito. You can see it in action [here](https://app.screencast.com/0DP4kaa4OvfXO).

## Requirements

- JDK 17
- Properly configured AWS Cognito and all relevant info provided (secrets, config, urls, etc)

## Usage

Configure sensitive configuration:

```shell
cp src/main/resources/application-private.yml.template src/main/resources/application-private.yml 
```

And then edit `src/main/resources/application-private.yml` and populate it with your secrets and configuration for Cognito.

execute `./mvnw spring-boot:run`

The easiest way to test it is using Postman:

![](./img/1.png)

![](./img/2.png)

^ `Auth URL` is in format `https://<SUB_DOMAIN>.auth.<REGION>.amazoncognito.com/login`

^ `Access Token URL` is in format `https://<SUB_DOMAIN>.auth.<REGION>.amazoncognito.com/oauth2/token`

![](./img/3.png)

## Notes

- Since we are using `Access Token` and not `ID Token`, we have to make API call to Cognito to fetch user details / attributes
- Potential improvement ^ in real life scenario would be to implement login method in our backend and then by decoding the `ID Token` we wouldn't need to make additional call for user info / attributes
- Instead of fetching `UserInfo` in controllers, we could extend Spring Security to populate custom user details
- Instead of checking for `VENDOR` and `VID` in controllers, we could implement a filter in security configuration to protect relevant endpoints 

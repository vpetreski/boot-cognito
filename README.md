# Boot-Cognito 🔒

Secure REST API via AWS Cognito. You can see it in action [here](https://app.screencast.com/0DP4kaa4OvfXO).

## Requirements

- JDK 17
- Properly configured AWS Cognito and all relevant info provided (secrets, config, urls, etc)

## Usage

Configure sensitive configuration:

```shell
cp src/main/resources/application.yml.template src/main/resources/application-private.yml 
```

And then edit `src/main/resources/application-private.yml` and populate it with your secrets and configuration for Cognito.

execute `./mvnw spring-boot:run`

The easiest way to test it is using Postman:

![](./img/1.png)

![](./img/2.png)

^ `Auth URL` is in format `https://<SUB_DOMAIN>.auth.<REGION>.amazoncognito.com/login`

^ `Access Token URL` is in format `https://<SUB_DOMAIN>.auth.<REGION>.amazoncognito.com/oauth2/token`

![](./img/3.png)

## Potential Improvements

- Instead of fetching UserInfo in controllers, we could implement a filter in security configuration to populate custom user details
- Instead of checking for `VENDOR` and `VID`, we could implement a filter in security configuration to protect relevant endpoints 

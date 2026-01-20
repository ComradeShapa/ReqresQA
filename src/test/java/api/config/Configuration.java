package api.config;

import org.aeonbits.owner.Config;

@Config.Sources({"file:./src/test/resources/config.properties"})
public interface Configuration extends Config {

    @Key("api.token")
    String apiToken();

    @Key("url.reqres.api")
    String urlReqresApi();

    @Key("login")
    String login();

    @Key("users")
    String users();

    @Key("users.2")
    String users2();

    @Key("register")
    String register();

    @Key("status200")
    Integer status200();

    @Key("status201")
    Integer status201();

    @Key("status400")
    Integer status400();

    @Key("emailEnding")
    String emailEnding();

    @Key("missingPasswordError")
    String missingPasswordError();
}




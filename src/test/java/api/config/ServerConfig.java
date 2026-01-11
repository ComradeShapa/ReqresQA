package api.config;

import org.aeonbits.owner.Config;

@Config.Sources({"file:./src/test/resources/ServerConfig.properties"})
public interface ServerConfig extends Config {
    @Key("url.reqres")
    String urlReqres();

    @Key("url.reqres.api.login")
    String urlReqresApiLogin();

    @Key("url.reqres.api.users")
    String urlReqresApiUsers();

    @Key("url.reqres.api.users.2")
    String urlReqresApiUsers2();

    @Key("url.reqres.api.register")
    String urlReqresApiRegister();
}



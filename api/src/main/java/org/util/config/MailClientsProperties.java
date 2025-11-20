package org.util.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mail")
public class MailClientsProperties {

    private Map<String, ClientConfig> clients = new HashMap<>();

    public Map<String, ClientConfig> getClients() {
        return clients;
    }

    public void setClients(Map<String, ClientConfig> clients) {
        this.clients = clients;
    }

    public static class ClientConfig {
        private String secret;
        private String to;
        private String from;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}

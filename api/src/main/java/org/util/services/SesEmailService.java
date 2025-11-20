package org.util.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.util.config.MailClientsProperties;
import org.util.records.EmailRequest;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesEmailService {

    private final SesClient sesClient;
    private final MailClientsProperties mailClientsProperties;


    public SesEmailService(
            SesClient sesClient,
            MailClientsProperties mailClientsProperties
    ) {
        this.sesClient = sesClient;
        this.mailClientsProperties = mailClientsProperties;
    }

    public void sendEmail(EmailRequest request) {
        // 1) Look up client config
        var clientConfig = mailClientsProperties.getClients().get(request.clientId());
        if (clientConfig == null) {
            throw new IllegalArgumentException("Unknown client: " + request.clientId());
        }

        // 2) Check secret
        if (!clientConfig.getSecret().equals(request.clientSecret())) {
            throw new IllegalArgumentException("Invalid secret for client: " + request.clientId());
        }
        String fromAddress = clientConfig.getFrom();
        String toAddress = clientConfig.getTo();

        // 3) Build and send SES request as before
        Content subject = Content.builder()
                .data(request.subject() != null ? request.subject() : "(no subject)")
                .charset("UTF-8")
                .build();

        Content textBody = Content.builder()
                .data(request.bodyText() != null ? request.bodyText() : "")
                .charset("UTF-8")
                .build();

        Body body;
        if (request.bodyHtml() != null && !request.bodyHtml().isBlank()) {
            Content htmlBody = Content.builder()
                    .data(request.bodyHtml())
                    .charset("UTF-8")
                    .build();

            body = Body.builder().html(htmlBody).text(textBody).build();
        } else {
            body = Body.builder().text(textBody).build();
        }

        Message message = Message.builder()
                .subject(subject)
                .body(body)
                .build();

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .source(fromAddress)
                .destination(Destination.builder()
                        .toAddresses(toAddress)
                        .build())
                .message(message)
                .build();

        sesClient.sendEmail(sendEmailRequest);
    }
}

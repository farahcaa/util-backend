package org.util.records;
public record EmailRequest(
        String clientId,
        String clientSecret,
        String subject,
        String bodyText,
        String bodyHtml
) {}

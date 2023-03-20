package com.slinkdigital.mail.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.slinkdigital.mail.dto.EventDto;
import com.slinkdigital.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import com.slinkdigital.mail.exception.MailServiceException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Set;
import static javax.mail.Message.RecipientType.TO;
import javax.mail.MessagingException;
import lombok.Builder;

/**
 *
 * @author TEGA
 */
@Service
@RequiredArgsConstructor
@Builder
public class GMailServiceImpl implements MailService{

//    private final Gmail service;
    
//    public GMailServiceImpl() throws Exception {
//        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
//                .setApplicationName("Test Mailer")
//                .build();
//    }

    @Override
    public void sendEmail(EventDto eventDto) throws MailException {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(eventDto.getFrom()));
            email.addRecipient(TO, new InternetAddress(eventDto.getTo()));
            email.setSubject(eventDto.getData().get("subject"));
            email.setText(eventDto.getData().get("content"));

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message msg = new Message();
            msg.setRaw(encodedEmail);

//            try {
//                msg = service.users().messages().send("me", msg).execute();
//                System.out.println("Message id: " + msg.getId());
//                System.out.println(msg.toPrettyString());
//            } catch (GoogleJsonResponseException e) {
//                GoogleJsonError error = e.getDetails();
//                if (error.getCode() == 403) {
//                    System.err.println("Unable to send message: " + e.getDetails());
//                } else {
//                    throw e;
//                }
//            }
        } catch (IOException | MessagingException ex) {
            throw new MailServiceException(ex.getMessage());
        }
    }
    
    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        InputStream in = GMailServiceImpl.class.getResourceAsStream("/client_secret_1042749728847-3crp3uuef1tn3cg2hjhf0g8b0qr4a54e.apps.googleusercontent.com.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

}

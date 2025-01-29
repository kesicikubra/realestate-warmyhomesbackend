package com.team03.service.email;

import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.TourReqStatus;
import com.team03.i18n.MessageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Service
public record EmailService(JavaMailSender mailSender, JavaMailSender advertMailSender, JavaMailSender tourRequestMailSender, @Value("${spring.mail.username}") String from) {

    public boolean sendResetCodeEmail(String to, String userName, String code) {
        try {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(MessageUtil.getMessage("reset.code.email.subject"));
                String emailContent = readEmailTemplate(userName, code);
                helper.setText(emailContent, true);
            };
            mailSender.send(messagePreparator);
            return true;
        } catch (MailException e) {
            return false;
        }
    }

    private String readEmailTemplate(String userName, String code) {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/emailText.html")) {
            if (inputStream == null) {
                throw new FileNotFoundException("HTML template not found.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                content = content.replace("{{Reset Password Email Header Title}}", MessageUtil.getMessage("reset.password.email.header.title"));
                content = content.replace("{{Email Content Greeting}}", MessageUtil.getMessage("email.content.greeting"));
                content = content.replace("{{First Name}}", userName);
                content = content.replace("{{Reset Password Content}}", MessageUtil.getMessage("email.content.reset.password.instruction"));
                content = content.replace("{{code}}", code);
                content = content.replace("{{Code Content}}", MessageUtil.getMessage("email.content.one.time.use.code"));
                content = content.replace("{{Email Content Signature}}", MessageUtil.getMessage("email.content.signature"));
                return content;
            }
        } catch (IOException e) {
            return MessageUtil.getMessage("email.content.error");
        }
    }

    public void sendActivatedOrRejectedAdvertEmail(String email, String firstName, Integer status) {
        String subject;
        String additionalInfo;

        if (status == 1) {
            subject = MessageUtil.getMessage("advert.email.approved.subject");
            additionalInfo = MessageUtil.getMessage("advert.email.approved.content");
        } else if(status == 2){ // status == 2
            subject = MessageUtil.getMessage("advert.email.rejected.subject");
            additionalInfo = MessageUtil.getMessage("advert.email.rejected.content");
        } else {
            return;
        }

        try {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                helper.setFrom(from);
                helper.setTo(email);
                helper.setSubject(subject);
                String emailContent = readAdvertEmailTemplate(firstName, additionalInfo);
                helper.setText(emailContent, true);
            };
            advertMailSender.send(messagePreparator);
        } catch (MailException ignored) {
        }
    }

    private String readAdvertEmailTemplate (String firstName, String additionalInfo) {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/advertStatusEmail.html")) {
            if (inputStream == null) {
                throw new FileNotFoundException("HTML template not found.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                content = content.replace("{{Advert Email Header Title}}", MessageUtil.getMessage("advert.email.header.title"));
                content = content.replace("{{Email Content Greeting}}", MessageUtil.getMessage("email.content.greeting"));
                content = content.replace("{{First Name}}", firstName);
                content = content.replace("{{Additional Info}}", additionalInfo);
                content = content.replace("{{Email Content Signature}}", MessageUtil.getMessage("email.content.signature"));
                return content;
            }
        } catch (IOException e) {
            return MessageUtil.getMessage("email.content.error");
        }
    }

    public void sendApprovedOrDeclinedTourRequestEmail(TourRequest tourRequest) {
        String email = tourRequest.getGuestUser().getEmail();
        String firstName = tourRequest.getGuestUser().getFirstName();
        String subject;
        String additionalInfo;

        if (tourRequest.getTourReqStatus() == TourReqStatus.APPROVED) {
            subject = MessageUtil.getMessage("tour.request.email.approved.subject");
            additionalInfo = MessageUtil.getMessage("tour.request.email.approved.content",
                    tourRequest.getAdvert().getTitle(),
                    tourRequest.getTourDate().toString(),
                    tourRequest.getTourTime().toString());
        } else if (tourRequest.getTourReqStatus() == TourReqStatus.DECLINED){
            subject = MessageUtil.getMessage("tour.request.email.declined.subject");
            additionalInfo =MessageUtil.getMessage("tour.request.email.declined.content",
                    tourRequest.getAdvert().getTitle());
        } else {
            return;
        }

        try {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                helper.setFrom(from);
                helper.setTo(email);
                helper.setSubject(subject);
                String emailContent = readTourRequestEmailTemplate(firstName, additionalInfo);
                helper.setText(emailContent, true);
            };
            tourRequestMailSender.send(messagePreparator);
        } catch (MailException ignored) {
        }
    }

    private String readTourRequestEmailTemplate(String firstName, String additionalInfo) {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/tourRequestNotification.html")) {
            if (inputStream == null) {
                throw new FileNotFoundException("HTML template not found.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                content = content.replace("{{Tour Request Email Header Title}}", MessageUtil.getMessage("tour.request.header.title"));
                content = content.replace("{{Email Content Greeting}}", MessageUtil.getMessage("email.content.greeting"));
                content = content.replace("{{First Name}}", firstName);
                content = content.replace("{{Additional Info}}", additionalInfo);
                content = content.replace("{{Email Content Signature}}", MessageUtil.getMessage("email.content.signature"));
                return content;
            }
        } catch (IOException e) {
            return MessageUtil.getMessage("email.content.error");
        }
    }
    public boolean sendVerificationEmail(String to, String appUrl, String name,
                                         String verificationToken) {
        try {
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject("Account Verification");
                String emailContent = verifyRegisterTemplate(to, appUrl,name,verificationToken);
                helper.setText(emailContent, true);
            };
            mailSender.send(messagePreparator);
            return true;
        } catch (MailException e) {
            return false;
        }
    }

    private String verifyRegisterTemplate(String email, String appUrl, String name, String token) {
        try (InputStream inputStream = getClass().getResourceAsStream("/templates/registerMail.html")) {
            if (inputStream == null) {
                throw new FileNotFoundException("HTML template not found.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                content = content.replace("{{First Name}}", name);
                content = content.replace("{{code}}",
                        appUrl + "/verify?token=" + token);

                return content;
            }
        } catch (IOException e) {
            return "Error reading the email template.";
        }
    }
}

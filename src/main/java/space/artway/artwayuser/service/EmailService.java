package space.artway.artwayuser.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerifyEmail(String email, String name, Long activationCode) {
        String link = "http://localhost:8081/api/user/activate?code=" + activationCode;
        String processedHTMLTemplate = this.constructHTMLTemplate(name, link);
        mailSender.send(prepareEmail(email, "Email verification", processedHTMLTemplate));
    }

    private MimeMessagePreparator prepareEmail(String mailTo, String subject, String processedHTMLTemplate){
        return message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            helper.setFrom("Artway Space <andrew@elmanov.ru>");
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(processedHTMLTemplate, true);
        };
    }

    private String constructHTMLTemplate(String name, String link) {
        Context context = new Context();
        context.setVariable("nameOfUser", name);
        context.setVariable("link", link);
        return templateEngine.process("activationEmail", context);
    }
}

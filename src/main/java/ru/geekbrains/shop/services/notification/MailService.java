package ru.geekbrains.shop.services.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;//непосредственно занимается отправкой писем
    private final TemplateEngine templateEngine;


    public void send(String to, String subject, Context context) {
        //подаем на вход наш шаблон
        String body = templateEngine.process("email-templates/email-customer", context);
        sendMessage(to, subject, body);
    }

    private void sendMessage(String to, String subject, String text) {
        try {

            MimeMessage mail = javaMailSender.createMimeMessage();
            //создаем хелпер который позволяет прикреплять к емайлу файлы если они есть
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);//далее собираем его
            helper.setSubject(subject);//тема
            helper.setText(text, true);//текст письма(true-указывает что параметры будут у нас html)
            javaMailSender.send(mail);
            new MailMessage(to, subject, text).sussess();
        } catch (MessagingException e) {
            e.printStackTrace();
            new MailMessage(to, subject, text).error(e.getMessage());
        }
    }

}
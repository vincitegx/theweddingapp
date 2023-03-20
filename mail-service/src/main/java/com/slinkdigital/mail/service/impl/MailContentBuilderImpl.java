package com.slinkdigital.mail.service.impl;

import com.slinkdigital.mail.service.MailContentBuilder;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author TEGA
 */
@Service
@AllArgsConstructor
public class MailContentBuilderImpl implements MailContentBuilder {

    private final TemplateEngine templateEngine;

    @Override
    public String build(Map<?,?> data) {
        Context context = new Context();
        context.setVariable("token", data.get("token"));
        context.setVariable("expiresAt", data.get("expiresAt"));
        context.setVariable("message", data.get("msg"));
        context.setVariable("subject", data.get("subject"));
        return templateEngine.process(data.get("template").toString(), context);

    }

}

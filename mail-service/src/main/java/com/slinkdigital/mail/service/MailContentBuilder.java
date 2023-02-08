package com.slinkdigital.mail.service;

import java.util.Map;

/**
 *
 * @author TEGA
 */
public interface MailContentBuilder {
    String build(Map<?,?> data);
}

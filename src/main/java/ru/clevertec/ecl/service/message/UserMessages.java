package ru.clevertec.ecl.service.message;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:message/message.properties")
public class UserMessages {

    @Value("${user.not-found}")
    private String notFound;
}

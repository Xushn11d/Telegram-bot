package org.example.bean;

import org.example.backend.service.RemindService;
import org.example.backend.service.UserService;
import org.example.bot.maker.MessageMaker;

public interface Bean {
    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<MessageMaker> messageMakerByThreadLocal = ThreadLocal.withInitial(MessageMaker::new);
    ThreadLocal<RemindService> reminderServiceByTHreadLocal = ThreadLocal.withInitial(RemindService::new);

}



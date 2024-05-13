package org.example.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import org.example.Bot;
import org.example.backend.model.MyUser;
import org.example.backend.service.RemindService;
import org.example.backend.service.UserService;
import org.example.bean.Bean;
import org.example.bot.maker.MessageMaker;
import org.example.bot.sender.SendActiveReminds;
import org.example.bot.states.base.BaseState;
import org.example.bot.states.child.MainState;

public abstract class BaseHandler {
    protected TelegramBot bot;
    protected MyUser curUser;
    protected Update update;
    protected RemindService remindService;
    protected UserService userService;
    protected MessageMaker messageMaker;
    protected SendActiveReminds sendActiveReminds;
    protected int index;

    public BaseHandler() {
        this.bot = new TelegramBot(Bot.BOT_TOKEN);
        this.userService = Bean.userServiceByThreadLocal.get();
        this.messageMaker = Bean.messageMakerByThreadLocal.get();
        this.remindService=Bean.reminderServiceByTHreadLocal.get();
        this.sendActiveReminds=new SendActiveReminds(curUser);
    }

    public abstract void handle(Update update);

    protected MyUser getUserOrCreate(User from){
        MyUser myUser = userService.get(from.id());
        if (myUser==null){
            myUser = MyUser.builder()
                    .id(from.id())
                    .username(from.username())
                    .baseState(BaseState.MAIN_STATE.name())
                    .state(MainState.REGISTER.name())
                    .firstname(from.firstName())
                    .lastname(from.lastName())
                    .build();
            userService.save(myUser);
        }
        return myUser;
    }

    protected void deleteMessage(int messageId){
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }

}

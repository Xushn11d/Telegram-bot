package org.example.bot.manager;

import com.pengrad.telegrambot.model.Update;
import org.example.bot.handlers.BaseHandler;
import org.example.bot.handlers.CallBackQueryHandler;
import org.example.bot.handlers.MessageHandler;

public class UpdateManager {

    private BaseHandler messageHandler;
    private BaseHandler callBackQuerryHandler;

    public UpdateManager() {
        this.messageHandler = new MessageHandler();
        this.callBackQuerryHandler = new CallBackQueryHandler();
    }

    public  void manage(Update update){
        if(update.message()!=null){
            messageHandler.handle(update);
        }else if(update.callbackQuery()!=null){
            callBackQuerryHandler.handle(update);
        }

    }

}

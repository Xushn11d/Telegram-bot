package org.example.bot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.Bot;
import org.example.backend.model.MyUser;
import org.example.backend.model.Remind;
import org.example.backend.service.RemindService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SendActiveReminds {
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(12);
    private static RemindService remindService= new RemindService();
    private static TelegramBot bot= new TelegramBot(Bot.BOT_TOKEN);
    private static MyUser user;

    public SendActiveReminds(MyUser user) {
        this.user = user;
    }

    public static void main(String[] args) {

    }
     public static void test(){
         LocalDateTime time = LocalDateTime.now();
         Runnable task= new Runnable() {
             @Override
             public void run() {
                 List<Remind> reminds = remindService.getAllReminds(user.getId());
                 for (Remind remind : reminds) {
                     if (remind.getSendDate().isBefore(time)){
                         SendMessage sendMessage = new SendMessage(user, remind.getText());
                         bot.execute(sendMessage);
                     }
                 }
             }
         };
         executor.schedule(task,1, TimeUnit.SECONDS);
         executor.execute(task);
     }



}

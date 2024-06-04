package org.example;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.example.bot.manager.UpdateManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot {
    public static final String BOT_TOKEN="7018455083:AAHPXrSVCECn0Rmp4w1B3Bq7d-1hAiQQ4aA";
    static final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    static  ThreadLocal<UpdateManager> updateHandlerThreadLocal = ThreadLocal.withInitial(UpdateManager::new);
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        bot.setUpdatesListener((updates) -> {
            for (Update update : updates) {
                CompletableFuture.runAsync( () -> {
                    try {
                        updateHandlerThreadLocal.get().manage(update);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } ,pool);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        },Throwable::printStackTrace);
    }
}


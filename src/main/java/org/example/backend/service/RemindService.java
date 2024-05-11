package org.example.backend.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.backend.model.MyUser;
import org.example.backend.model.Remind;
import org.example.backend.repository.FileWriterAndLoader;
import org.example.backend.statics.PathConstants;
import org.example.bot.maker.MessageMaker;

import java.util.List;
import java.util.Objects;

public class RemindService implements PathConstants {
   private FileWriterAndLoader<Remind> fileWriterOrLoader;

    public RemindService() {
        this.fileWriterOrLoader = new FileWriterAndLoader<>(REMINDS_PATH);
    }

    public void save(Remind remind){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            if(Objects.equals(remind,reminds.get(i))){
              reminds.set(i,remind);
              fileWriterOrLoader.write(reminds);
              return;
            }

        }
        reminds.add(remind);
        fileWriterOrLoader.write(reminds);
    }
    public void changeRemind(Remind remind){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            if(Objects.equals(remind.getUserId(),reminds.get(i).getUserId())){
                reminds.set(i,remind);
                fileWriterOrLoader.write(reminds);
                return;
            }

        }
        reminds.add(remind);
        fileWriterOrLoader.write(reminds);
    }
    public Remind getByIndex(Long id, int index){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        int count=0;
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                count++;
                if (Objects.equals(index,count)) {
                    return cur;
                }
            }
        }
        return null;
    }
    public Remind get(Long id){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                return cur;
            }
        }
        return null;
    }
    public boolean deleteRemind(Long id,Remind remind){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)&&remind.equals(cur)){
                reminds.remove(cur);
                fileWriterOrLoader.write(reminds);
                return true;
            }
        }
        fileWriterOrLoader.write(reminds);
        return false;
    }
    public boolean sendAllReminds(Long id, TelegramBot bot, MyUser user){
        List<Remind> reminds = fileWriterOrLoader.load(Remind.class);
        int count=0;
        for (int i = 0; i < reminds.size(); i++) {
            Remind cur = reminds.get(i);
            if (Objects.equals(cur.getUserId(),id)){
                count++;
                SendMessage sendMessage= new SendMessage(id,count+": "+cur.getText()+" "+cur.getSendDate());
                bot.execute(sendMessage);
            }
        }
        if(get(id)==null){
            MessageMaker messageMaker= new MessageMaker();
            SendMessage sendMessage = messageMaker.noRemind(user);
            bot.execute(sendMessage);
            return true;
        }
        MessageMaker messageMaker= new MessageMaker();
        SendMessage sendMessage = messageMaker.chooseDeleteRemind(user);
        bot.execute(sendMessage);
        return false;
    }


}

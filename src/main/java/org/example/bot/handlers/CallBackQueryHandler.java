package org.example.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.backend.model.Remind;
import org.example.bot.states.base.BaseState;
import org.example.bot.states.child.DeleteRemindState;
import org.example.bot.states.child.MainState;
import org.example.bot.states.child.SetRemindState;

import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler{
    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser=getUserOrCreate(from);
        super.update = update;
        String baseStateString = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateString);
        if (Objects.equals(baseState,BaseState.MAIN_STATE)){
            mainState();
        }  else if (Objects.equals(baseState, BaseState.SET_REMIND_STATE)){
            SetRemindState();
        }else if(Objects.equals(baseState, BaseState.DELETE_REMIND_STATE)){
            deleteRemind();
        }

    }
    private void deleteRemind() {
        String stateStr = curUser.getState();
        DeleteRemindState state = DeleteRemindState.valueOf(stateStr);
        switch (state){
            case CHOOSE_REMIND ->{
                if (deleteBack(update.callbackQuery().data(),state,update.callbackQuery().message())) {
                    deleteMessage(update.callbackQuery().message().messageId());
                    return;
                }
                remindService.sendAllReminds(curUser.getId(),bot,curUser);
                curUser.setState(DeleteRemindState.CHOOSE_REMIND.name());
            }
            case DELETE_REMIND -> {
                if (deleteBack(update.callbackQuery().data(),state,update.callbackQuery().message())) {
                    checkRemind();
                    deleteMessage(update.callbackQuery().message().messageId());
                }
            }
        }
    }
    private void checkRemind() {
        if(update.message().text()!=null) {
            if (remindService.getByIndex(curUser.getId(), Math.toIntExact(Long.parseLong(update.message().text())))==null) {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "This remind does not exist");
                bot.execute(sendMessage);
            }else {
                Remind index = remindService.getByIndex(curUser.getId(), Integer.parseInt(update.message().text()));
                remindService.deleteRemind(curUser.getId(), index);
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Successfully deleted 🎉🎉🎉");
                bot.execute(sendMessage);
            }
        }else {
            incorrectData("remind");
        }

    }

    private void SetRemindState() {
        String stateStr = curUser.getState();
        SetRemindState state = SetRemindState.valueOf(stateStr);
        switch (state){
            case ENTER_TEXT ->{
                if (backToChooseFunction(update.callbackQuery().data(),state,update.callbackQuery().message())) {
                    deleteMessage(update.callbackQuery().message().messageId());
                    return;
                }
                curUser.setState(SetRemindState.ENTER_TEXT.name());
                SendMessage sendMessage = messageMaker.enterText(curUser);
                bot.execute(sendMessage);
            }
            case ENTER_DATE -> {
                if (backToEnterText(update.callbackQuery().data(),state,update.callbackQuery().message())) {
                    deleteMessage(update.callbackQuery().message().messageId());
                }
            }
        }

    }
    private void mainState() {
        Message message = update.callbackQuery().message();
        deleteMessage(message.messageId());
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        switch (state){
            case MAIN_MENU -> {
                SendMessage sendMessage = messageMaker.chooseFunction(curUser);
                curUser.setState(MainState.CHOOSE_MENU.name());
                bot.execute(sendMessage);
                userService.save(curUser);
            }
            case CHOOSE_MENU-> chooseMenu();
        }
    }

    private void chooseMenu(){
        Message message = update.callbackQuery().message();
        String data = update.callbackQuery().data();
        switch (data){
            case "SET_REMIND" -> {
                curUser.setBaseState(BaseState.SET_REMIND_STATE.name());
                curUser.setState(SetRemindState.ENTER_TEXT.name());
                userService.save(curUser);
                SetRemindState();
                deleteMessage(message.messageId());
            }
            case "DELETE_REMIND"-> {
                curUser.setBaseState(BaseState.DELETE_REMIND_STATE.name());
                curUser.setState(DeleteRemindState.CHOOSE_REMIND.name());
                userService.save(curUser);
                deleteRemind();
                deleteMessage(message.messageId());
            }
            case "BACK"->{
                curUser.setState(MainState.MAIN_MENU.name());
                userService.save(curUser);
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
        }
    }

    private void changeDeleteState(DeleteRemindState state) {
        DeleteRemindState pervState = state.getPrevState();
        if (pervState==null) {
            curUser.setBaseState(BaseState.MAIN_STATE.name());
            curUser.setState(MainState.MAIN_MENU.name());
        }else {
            curUser.setState(pervState.name());
        }
        userService.save(curUser);
    }
    private void changeState(SetRemindState state) {
        SetRemindState pervState = state.getPervState();
        if (pervState==null) {
            curUser.setBaseState(BaseState.MAIN_STATE.name());
            curUser.setState(MainState.MAIN_MENU.name());
        }else {
            curUser.setState(pervState.name());
        }
        userService.save(curUser);
    }

    private boolean backToChooseFunction(String data, SetRemindState state, Message message){
        if (data.equals("BACK")){
            changeState(state);
            SendMessage sendMessage = messageMaker.chooseFunction(curUser);
            bot.execute(sendMessage);
            deleteMessage(message.messageId());
            return true;
        }
        return false;
    }
    private boolean backToEnterText(String data, SetRemindState state, Message message){
        if (data.equals("BACK")){
            changeState(state);
            SendMessage sendMessage = messageMaker.enterText(curUser);
            bot.execute(sendMessage);
            deleteMessage(message.messageId());
            return true;
        }
        return false;
    }
    private boolean deleteBack(String data,DeleteRemindState state,Message message){
        if (data.equals("BACK")){
            changeDeleteState(state);
            SendMessage sendMessage = messageMaker.chooseFunction(curUser);
            bot.execute(sendMessage);
            deleteMessage(message.messageId());
            return true;
        }
        return false;
    }

    private void incorrectData(String data  ) {
        bot.execute(new SendMessage(curUser.getId(),"You entered incorrect " + data));

    }


}

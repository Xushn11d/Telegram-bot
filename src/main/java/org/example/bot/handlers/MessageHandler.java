package org.example.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.backend.model.Remind;
import org.example.backend.service.RemindService;
import org.example.bot.states.base.BaseState;
import org.example.bot.states.child.DeleteRemindState;
import org.example.bot.states.child.MainState;
import org.example.bot.states.child.SetRemindState;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import static java.awt.SystemColor.text;

public class MessageHandler extends BaseHandler{

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();
        if (Objects.equals(text,"/start")){
            if(Objects.isNull( curUser.getPhoneNumber()) ||  curUser.getPhoneNumber().isEmpty()){
                enterPhoneNumber();
            }else {
                mainManu();
            }
        } else {
            String baseStateString = curUser.getBaseState();
            BaseState baseState = BaseState.valueOf(baseStateString);
            if (Objects.equals(baseState,BaseState.MAIN_STATE)){
                mainState();
            }else if (Objects.equals(baseState,BaseState.SET_REMIND_STATE)){
                setRemindState();
            }else if (Objects.equals(baseState,BaseState.DELETE_REMIND_STATE)){
                deleteRemindState();
            }
        }
    }

    private void deleteRemindState() {
        String stateStr = curUser.getState();
        DeleteRemindState state = DeleteRemindState.valueOf(stateStr);
        switch (state){
            case CHOOSE_REMIND ->{
                curUser.setState(DeleteRemindState.DELETE_REMIND.name());
                SendMessage allow = messageMaker.allow(curUser);
                bot.execute(allow);
                userService.save(curUser);
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
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Successfully deleted ");
                bot.execute(sendMessage);
            }
        }else {
            incorrectData("remind");
        }

    }
	
	private void setRemindState() {
		String stateStr = curUser.getState();
		SetRemindState state = SetRemindState.valueOf(stateStr);
		switch (state) {
			case ENTER_TEXT -> {
				String text = update.message().text();
				if (text != null) {
					Remind remind = Remind.builder()
							.presnetDate(LocalDate.now())
							.text(text)
							.userId(curUser.getId())
							.build();
					remindService.save(remind);
					curUser.setState(SetRemindState.ENTER_DATE.name());
					userService.save(curUser);
					SendMessage sendMessage = messageMaker.enterDateForReminder(curUser);
					bot.execute(sendMessage);
				} else {
					incorrectData("Text");
				}
			}
			case ENTER_DATE -> {
				Message message = update.message();
				String text = message.text();
				if (text == null) {
					incorrectData("Date");
					SendMessage sendMessage = messageMaker.enterDateForReminder(curUser);
					bot.execute(sendMessage);
					return;
				}
				
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				try {
					LocalDate date = LocalDate.parse(text, dateFormatter);
					Remind remind = remindService.get(curUser.getId());
					remind.setSendDate(date);
					remindService.changeRemind(remind);
					LocalDate currentDate = LocalDate.now();
					if (date.isBefore(currentDate)) {
						throw new DateTimeException("");
					}
				} catch (DateTimeException e) {
					incorrectData("Date");
					SendMessage sendMessage = messageMaker.enterDateForReminder(curUser);
					bot.execute(sendMessage);
					return;
				}
				SendMessage successMessage = new SendMessage(curUser.getId(), "Successfully added🎉🎉🎉");
				bot.execute(successMessage);
				curUser.setState(MainState.CHOOSE_MENU.name());
				curUser.setBaseState(BaseState.MAIN_STATE.name());
				userService.save(curUser);
				SendMessage chooseFunctionMessage = messageMaker.chooseFunction(curUser);
				bot.execute(chooseFunctionMessage);
			}
		}
	}
	
	
	private void mainState() {
        String stateStr = curUser.getState();
        MainState state = MainState.valueOf(stateStr);
        switch (state){
            case REGISTER -> {
                Message message = update.message();
                Contact contact = message.contact();
                if (contact!=null){
                    String phoneNumber = contact.phoneNumber();
                    curUser.setPhoneNumber(phoneNumber);
                    userService.save(curUser);
                    mainManu();
                }else {
                    incorrectData("Phone Number");
                }

            }
            case MAIN_MENU ->mainManu();
            case CHOOSE_MENU ->{
                SendMessage sendMessage = messageMaker.chooseFunction(curUser);
                bot.execute(sendMessage);
            }

        }
    }
    private void mainManu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
        curUser.setState(MainState.MAIN_MENU.name());
        curUser.setBaseState(BaseState.MAIN_STATE.name());
        userService.save(curUser);
    }

    private void incorrectData(String data  ) {
        bot.execute(new SendMessage(curUser.getId(),"You entered incorrect " + data));

    }

    public void enterPhoneNumber(){
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }




}


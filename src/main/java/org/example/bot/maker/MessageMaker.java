package org.example.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.backend.model.MyUser;

public class MessageMaker {

    public SendMessage mainMenu(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Main Menu");
        InlineKeyboardButton[][] buttons = {
                {
                    new InlineKeyboardButton("\uD83D\uDCDCRemind").callbackData("REMIND"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterPhoneNumber(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "☎️Enter Phone Number");
        KeyboardButton[][] buttons ={
                { new KeyboardButton("\uD83D\uDCDEPhone Number").requestContact(true) }
        };
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterDateForReminder(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "\uD83D\uDCC6Enter Date (dd/mm/yyyy):");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("⬅️Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage allow(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "🤔Do you really want delete");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("✅Yes").callbackData("YES"),
                        new InlineKeyboardButton("❌No").callbackData("NO"),
                },
                {

                        new InlineKeyboardButton("❌Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage chooseFunction(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Choose function: ");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("✅Add remind").callbackData("SET_REMIND"),
                        new InlineKeyboardButton("\uD83D\uDDD1Delete remind").callbackData("DELETE_REMIND"),
                },
                {

                        new InlineKeyboardButton("🏷️Show reminds").callbackData("SHOW_REMIND"),
                        new InlineKeyboardButton("✅ToDo Picture").callbackData("TODO_PICTURE"),
                },
                {
                        new InlineKeyboardButton("⬅️Back").callbackData("BACK"),
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage enterText(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "✅Enter a remind");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("❌Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage noRemind(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "🗓️You do not have any reminds");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("❌Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;
    }

    public SendMessage deleteRemind(MyUser curUser){
        SendMessage sendMessage = new SendMessage(curUser.getId(), "🗓Choose what you want to delete");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("❌Back").callbackData("BACK")
                }
        };
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(markup);
        return sendMessage;

    }



}

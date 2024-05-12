package org.example.bot.states.child;

import lombok.Getter;

@Getter
public enum DeleteRemindState {
    CHOOSE_REMIND(null),
    DELETE_REMIND(CHOOSE_REMIND);

    public DeleteRemindState prevState;

    DeleteRemindState(DeleteRemindState prevState) {
        this.prevState = prevState;
    }

}

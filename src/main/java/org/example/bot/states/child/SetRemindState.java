package org.example.bot.states.child;

import com.sun.source.tree.SwitchExpressionTree;
import lombok.Getter;

@Getter
public enum SetRemindState {
    ENTER_TEXT(null),
    ENTER_DATE(ENTER_TEXT);

public SetRemindState pervState;
    SetRemindState(SetRemindState pervState) {
        this.pervState=pervState;
    }

}

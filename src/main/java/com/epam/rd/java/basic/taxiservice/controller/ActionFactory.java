package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.MessageManager;
import com.epam.rd.java.basic.taxiservice.controller.command.*;
import com.epam.rd.java.basic.taxiservice.controller.command.tripCommands.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private final Map<String, ActionCommand> actionCommandMap;

    {
        actionCommandMap = new HashMap<>();
        actionCommandMap.put("/login.do", new LoginCommand());
        actionCommandMap.put("/loginPage.do", new ViewLoginPageCommand());
        actionCommandMap.put("/logout.do", new LogoutCommand());
        actionCommandMap.put("/trips/new.do", new NewTripCommand());
        actionCommandMap.put("/trips/edit.do", new EditTripCommand());
        actionCommandMap.put("/trips/save.do", new SaveTripCommand());
        actionCommandMap.put("/trips/start.do", new StartTripCommand());
        actionCommandMap.put("/trips/finish.do", new FinishTripCommand());
        actionCommandMap.put("/trips/delete.do", new DeleteTripCommand());
        actionCommandMap.put("/trips/list.do", new ViewTripsListCommand());
        actionCommandMap.put("/trips/view.do", new ViewTripCommand());
    }

    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String action = request.getRequestURI();
        try {
            current = actionCommandMap.get(action);
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action
                    + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}
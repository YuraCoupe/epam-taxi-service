package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.config.MessageManager;
import com.epam.rd.java.basic.taxiservice.controller.command.*;
import com.epam.rd.java.basic.taxiservice.controller.command.carCommands.*;
import com.epam.rd.java.basic.taxiservice.controller.command.clientCommands.*;
import com.epam.rd.java.basic.taxiservice.controller.command.driverCommands.*;
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

        actionCommandMap.put("/cars/new.do", new NewCarCommand());
        actionCommandMap.put("/cars/edit.do", new EditCarCommand());
        actionCommandMap.put("/cars/save.do", new SaveCarCommand());
        actionCommandMap.put("/cars/update.do", new UpdateCarCommand());
        actionCommandMap.put("/cars/delete.do", new DeleteCarCommand());
        actionCommandMap.put("/cars/list.do", new ViewCarsListCommand());
        actionCommandMap.put("/cars/view.do", new ViewCarCommand());

        actionCommandMap.put("/clients/new.do", new NewClientCommand());
        actionCommandMap.put("/clients/edit.do", new EditClientCommand());
        actionCommandMap.put("/clients/save.do", new SaveClientCommand());
        actionCommandMap.put("/clients/delete.do", new DeleteClientCommand());
        actionCommandMap.put("/clients/list.do", new ViewClientsListCommand());
        actionCommandMap.put("/clients/view.do", new ViewClientCommand());

        actionCommandMap.put("/drivers/new.do", new NewDriverCommand());
        actionCommandMap.put("/drivers/edit.do", new EditDriverCommand());
        actionCommandMap.put("/drivers/save.do", new SaveDriverCommand());
        actionCommandMap.put("/drivers/delete.do", new DeleteDriverCommand());
        actionCommandMap.put("/drivers/list.do", new ViewDriversListCommand());
        actionCommandMap.put("/drivers/view.do", new ViewDriverCommand());
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
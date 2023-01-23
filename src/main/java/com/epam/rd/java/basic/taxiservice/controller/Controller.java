package com.epam.rd.java.basic.taxiservice.controller;

import com.epam.rd.java.basic.taxiservice.controller.command.*;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.CommandResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.ForwardResult;
import com.epam.rd.java.basic.taxiservice.controller.commandResult.RedirectResult;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("*.do")
public class Controller extends HttpServlet {

    private static final Map<Class<?>, View> views = new HashMap<>();

    static {
        views.put(ForwardResult.class, (result, request, response) -> {
            RequestDispatcher disp =
                    request.getRequestDispatcher(result.getResource());
            disp.forward(request, response);
        });
        views.put(RedirectResult.class, (result, request, response) -> {
            response.sendRedirect(request.getContextPath() + result.getResource());
        });
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        String page = null;
        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);

        CommandResult commandResult = command.execute(request);
        views.get(commandResult.getClass()).render(commandResult, request, response);

//        if (page != null) {
//            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page); // вызов страницы ответа на запрос
//            dispatcher.forward(request, response);
//        } else {
//            page = ConfigurationManager.getProperty("path.page.index");
//            request.getSession().setAttribute("nullPage", MessageManager.getProperty("message.nullpage"));
//            response.sendRedirect(request.getContextPath() + page);
//        }
    }
}

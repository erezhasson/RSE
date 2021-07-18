package servlets.dashboard;

import com.google.gson.Gson;
import datafiles.dto.UserDto;
import logicinterface.RizpaInterface;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class OnlineUsersListSerlvet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            List<OnlineUser> onlineUsers = buildOnlineList(engine.getOnlineUsers());
            String json = gson.toJson(onlineUsers);
            out.println(json);
            out.flush();
        }
    }

    private List<OnlineUser> buildOnlineList(Collection<UserDto> users) {
        List<OnlineUser> onlineUsers = new ArrayList<>();

        for (UserDto user: users) {
            String name = user.getName();
            onlineUsers.add(new OnlineUser(name, name.equalsIgnoreCase("admin") ? "Admin" : "Broker"));
        }

        return onlineUsers;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private static class OnlineUser {
        String name;
        String role;

        public OnlineUser(String name, String role) {
            this.name = name;
            this.role = role;
        }
    }
}

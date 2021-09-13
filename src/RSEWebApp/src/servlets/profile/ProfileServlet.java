package servlets.profile;

import com.google.gson.Gson;
import constants.Constants;
import datafiles.dto.UserDto;
import datafiles.user.User;
import exceptions.name.NameException;
import exceptions.stock.StockNotFoundException;
import exceptions.user.UserNotFoundException;
import logicinterface.RizpaInterface;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.USERNAME;

public class ProfileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        RizpaInterface engine = ServletUtils.getEngine(getServletContext());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        if (usernameFromSession == null) {
            response.setStatus(409);
            response.sendRedirect(request.getContextPath() + "index.html");
        } else {
            try {
                UserDto user = engine.getUser(usernameFromSession);
                String json = gson.toJson(user);
                out.println(json);
                out.close();
            } catch (UserNotFoundException | StockNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ServletUtils.ErrorMessage message = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
                String json = gson.toJson(message);
                out.println(json);
                out.close();
            }
        }
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

}

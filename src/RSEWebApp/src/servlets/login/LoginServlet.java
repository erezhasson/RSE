package servlets.login;

import datafiles.user.User;
import exceptions.name.NameException;
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

public class LoginServlet extends HttpServlet {

    private final String HOME_URL = "/pages/dashboard/dashboard.html";
    private final String ADMIN_HOME_URL = "/pages/dashboard/dashboard-admin.html";
    private final String LOGIN_URL = "/pages/login/login.html";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        RizpaInterface engine = ServletUtils.getEngine(getServletContext());
        String usernameFromParameter = request.getParameter(USERNAME);

        if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
            response.setStatus(409);
            response.sendRedirect(LOGIN_URL);
        } else {
            usernameFromParameter = usernameFromParameter.trim();

            synchronized (this) {
                if (engine.isUserExists(usernameFromParameter)) {
                    request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                    User newUser = new User(usernameFromParameter);

                    try {
                        engine.addOnlineUser(usernameFromParameter, newUser);
                    } catch (NameException ignored){
                    }
                    response.setStatus(200);

                    if (ServletUtils.isAdmin(newUser)) {
                        response.getOutputStream().println(ADMIN_HOME_URL);
                    } else {
                        response.getOutputStream().println(HOME_URL);
                    }
                }
                else {
                    response.setStatus(400);
                    String errorMessage = "Username " + usernameFromParameter + " doesn't exists.";
                    PrintWriter out = response.getWriter();

                    out.println(errorMessage);
                    out.flush();
                }
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

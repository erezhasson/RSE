package servlets.dashboard;

import com.google.gson.Gson;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
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
import java.util.List;

public class StockTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            String username = SessionUtils.getUsername(request);
            if (username == null) {
                response.sendRedirect(request.getContextPath() + "index.html");
            }

            Gson gson = new Gson();
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            List<StockDto> allStocks = (List<StockDto>) engine.getAllStocks();

            allStocks.sort((o1, o2) -> {
                double dif = o1.getPrice() - o2.getPrice();

                return dif > 0 ? (dif == 0 ? 0 : -1) : 1;
            });
            String json = gson.toJson(allStocks);
            out.println(json);
            out.flush();
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

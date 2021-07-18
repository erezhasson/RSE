package servlets.searchstock;

import com.google.gson.Gson;
import constants.Constants;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import datafiles.stock.Stocks;
import datafiles.user.User;
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

import static constants.Constants.STOCK_SYMBOL;

public class SearchStockServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String stockSymbolFromParameter = request.getParameter(STOCK_SYMBOL);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);

        try {
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            UserDto user = engine.getUser(username);
            StockDto stock = engine.getStock(stockSymbolFromParameter);
            StockInfo stockinfo = new StockInfo(stock,user.getStockAmount(stock.getSymbol()));
            String json = gson.toJson(stockinfo);

            out.println(json);
            out.flush();
        } catch (StockNotFoundException | UserNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ServletUtils.ErrorMessage message = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
            String json = gson.toJson(message);
            out.println(json);
            out.close();
        }
    }

    public static class StockInfo {
        StockDto stock;
        int amount;

        public StockInfo(StockDto stock, int amount) {
            this.stock = stock;
            this.amount = amount;
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

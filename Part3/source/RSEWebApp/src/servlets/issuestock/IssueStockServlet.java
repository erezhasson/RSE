package servlets.issuestock;

import com.google.gson.Gson;
import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;
import logicinterface.RizpaInterface;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.*;

public class IssueStockServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String stockSymbolParameter = request.getParameter(STOCK_SYMBOL);
        String companyNameParameter = request.getParameter(COMPANY_NAME);
        String companyWorthParameter = request.getParameter(COMPANY_ESTIMATED_WORTH);
        String stockAmountParameter = request.getParameter(STOCK_AMOUNT);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);

        try {
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            engine.issueNewStock(username, stockSymbolParameter, Integer.parseInt(stockAmountParameter),
                    companyNameParameter, Double.parseDouble(companyWorthParameter));
            IssueStockMessage message = new IssueStockMessage(stockSymbolParameter, stockAmountParameter);
            String json = gson.toJson(message);
            System.out.println(json);
            out.println(json);
            out.flush();
        } catch (StockException | SymbolException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ServletUtils.ErrorMessage message = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
            String json = gson.toJson(message);
            out.println(json);
            out.close();
        }
    }

    public static class IssueStockMessage {
        String message;

        public IssueStockMessage(String stockSymbol, String stockAmount) {
            this.message = stockAmount + " stock of " + stockSymbol + " have been issued successfuly.";
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

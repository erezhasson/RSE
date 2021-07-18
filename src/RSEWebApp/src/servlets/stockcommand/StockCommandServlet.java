package servlets.stockcommand;

import com.google.gson.Gson;
import datafiles.commands.CommandDetails;
import datafiles.dto.RizpaCommandDto;
import exceptions.command.CommandException;
import exceptions.stock.StockException;
import exceptions.symbol.SymbolException;
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
import java.util.Collection;

import static constants.Constants.*;

public class StockCommandServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        RizpaInterface engine = ServletUtils.getEngine(getServletContext());
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);
        String symbol = request.getParameter(STOCK_SYMBOL);
        String direction = request.getParameter(COMMAND_DIRECTION);
        String price = request.getParameter(COMMAND_PRICE);
        String amount = request.getParameter(STOCK_AMOUNT);
        String type = request.getParameter(COMMAND_TYPE);

        try {
            CommandDetails details = new CommandDetails(symbol, type, price, amount, direction);
            Collection<RizpaCommandDto> newDeals = engine.operateCommand(engine.createNewCommand(details, username));
            String message = createDealsMessage(newDeals, SessionUtils.getUsername(request), engine);
            String json = gson.toJson(message);
            out.println(json);
            out.flush();

        } catch (UserNotFoundException | SymbolException | CommandException | StockException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ServletUtils.ErrorMessage message = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
            String json = gson.toJson(message);
            out.println(json);
            out.close();
        }
    }

    private String createDealsMessage(Collection<RizpaCommandDto> newDeals, String username, RizpaInterface engine) {
        String message = "";

        if (newDeals.size() == 0) {
            message += "Command has been not executed and have been recorded in stock's pending commands.";
        }
        else {
            String prefix = "Command has been executed successfully.\n" +
                    "The following deals have been completed after executing command:\n";
            String alert = prefix;

            message = prefix;
            for (RizpaCommandDto deal : newDeals) {
                message += deal.toString() + "\n";
                alert += deal.toString();

                if (deal.getBuyer().equals(username)) {
                    engine.addAlertToUser(deal.getSeller(), alert);
                } else {
                    engine.addAlertToUser(deal.getBuyer(), alert);

                }
                alert = prefix;
            }
        }

        return message;
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

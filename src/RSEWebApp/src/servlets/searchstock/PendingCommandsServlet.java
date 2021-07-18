package servlets.searchstock;

import com.google.gson.Gson;
import datafiles.dto.RecordBookDto;
import datafiles.dto.RizpaCommandDto;
import datafiles.dto.StockDto;
import datafiles.dto.UserDto;
import exceptions.stock.StockNotFoundException;
import exceptions.user.UserNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logicinterface.RizpaInterface;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static constants.Constants.STOCK_SYMBOL;

public class PendingCommandsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String stockSymbolFromParameter = request.getParameter(STOCK_SYMBOL);
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);

        try {
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            StockDto stock = engine.getStock(stockSymbolFromParameter);
            RecordBookDto book = stock.getRecords();
            String json = gson.toJson(buildPendingCommandsList(book.getBuying(), book.getSelling()));
            out.println(json);
            out.flush();
        } catch (StockNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ServletUtils.ErrorMessage message = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
            String json = gson.toJson(message);
            out.println(json);
            out.close();
        }
    }

    private Collection<RizpaCommandDto> buildPendingCommandsList(ObservableList<RizpaCommandDto> buying, ObservableList<RizpaCommandDto> selling) {
        List<RizpaCommandDto> pendingCommands = new ArrayList<>();

        pendingCommands.addAll(buying);
        pendingCommands.addAll(selling);
        Collections.sort(pendingCommands);

        return pendingCommands;
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

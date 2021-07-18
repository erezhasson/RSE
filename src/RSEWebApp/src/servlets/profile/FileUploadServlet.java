package servlets.profile;

import com.google.gson.Gson;
import logicinterface.RizpaInterface;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    private final String HOME_URL = "/index.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        String usernameFromSession = SessionUtils.getUsername(request);
        String message  = "XML Uploaded Successfully.";
        String json = gson.toJson(message);

        if (usernameFromSession == null) {
            response.setStatus(409);
            response.sendRedirect(HOME_URL);
        } else {
            RizpaInterface engine = ServletUtils.getEngine(getServletContext());
            PrintWriter out = response.getWriter();
            File file = createXMLFile(request.getPart("file"));
            try {
                engine.buildUserStockFromFile(usernameFromSession, file);
            } catch ( Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ServletUtils.ErrorMessage errorMessage = new ServletUtils.ErrorMessage(response.getStatus(), e.getMessage());
                json = gson.toJson(errorMessage);
            }
            finally {
                boolean fileDeleted = file.delete();
                out.println(json);
                out.close();
            }
        }
    }

    private File createXMLFile(Part part) throws IOException {
        String location = getServletContext().getRealPath("common\\xmlfile\\xml-file.xml");
        File fileToCreate = new File(location);
        boolean createdFile = fileToCreate.createNewFile();

        if (createdFile) {
            PrintWriter pw = new PrintWriter(fileToCreate);
            String content = readFromInputStream(part.getInputStream());
            pw.write(content);
            part.delete();
            pw.close();
        }
        else {
            throw new IOException("Error while trying to upload file.");
        }
        return fileToCreate;
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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



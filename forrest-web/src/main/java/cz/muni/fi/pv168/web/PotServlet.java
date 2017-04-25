package cz.muni.fi.pv168.web;

import cz.muni.fi.pv168.forrest.Pot;
import cz.muni.fi.pv168.forrest.PotManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for managing pots.
 *
 * @author Jakub Bohos 422419
 */
@WebServlet(PotServlet.URL_MAPPING + "/*")
public class PotServlet extends HttpServlet {

    private static final String LIST_JSP = "/potWeb.jsp";
    public static final String URL_MAPPING = "/pots";

    private final static Logger log = LoggerFactory.getLogger(PotServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET ...");
        showPotsList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}",action);

        switch (action) {
            case "/add":
                //getting POST parameters from form
//                String id = request.getParameter("id");
                String row = request.getParameter("row");
                String column = request.getParameter("column");
                String capacity = request.getParameter("capacity");
                String note = request.getParameter("note");
                //form data validity check
                if (row == null || row.length() == 0 || column == null || column.length() == 0 ||
                        capacity == null || capacity.length() == 0 || note == null || note.length() == 0)  {
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty !");
                    log.debug("form data invalid");
                    showPotsList(request, response);
                    return;
                }
                //form data processing - storing to database
                try {
                    Pot pot = new Pot(null,Integer.valueOf(row),Integer.valueOf(column),Integer.valueOf(capacity),note);
                    getPotManager().createPot(pot);
                    //redirect-after-POST protects from multiple submission
                    log.debug("redirecting after POST");
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot add pot", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long potId = Long.valueOf(request.getParameter("id"));
                    getPotManager().deletePot(getPotManager().findPotById(potId));
                    log.debug("redirecting after POST");
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot delete pot", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            case "/update":
                //getting POST parameters from form
                Long idU = Long.valueOf(request.getParameter("id"));
                String rowU = request.getParameter("row");
                String columnU = request.getParameter("column");
                String capacityU = request.getParameter("capacity");
                String noteU = request.getParameter("note");
                //form data validity check
                if ( idU == null || rowU == null || rowU.length() == 0 || columnU == null || columnU.length() == 0 ||
                        capacityU == null || capacityU.length() == 0 || noteU == null || noteU.length() == 0) {
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty !");
                    log.debug("form data invalid");
                    showPotsList(request, response);
                    return;
                }
                //form data processing - storing to database
                try {
                    Pot pot = new Pot(idU,Integer.valueOf(rowU),Integer.valueOf(columnU),Integer.valueOf(capacityU),noteU);
                    getPotManager().updatePot(pot);
                    log.debug("redirecting after POST");
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (Exception e) {
                    log.error("Cannot update pot", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }


            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets PotManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return PotManager instance
     */
    private PotManager getPotManager() {
        return (PotManager) getServletContext().getAttribute("potManager");
    }

    /**
     * Stores the list of pots to request attribute "pots" and forwards to the JSP to display it.
     */
    private void showPotsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            log.debug("showing table of pots");
            request.setAttribute("pots", getPotManager().findAllPots());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.error("Cannot show pots", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

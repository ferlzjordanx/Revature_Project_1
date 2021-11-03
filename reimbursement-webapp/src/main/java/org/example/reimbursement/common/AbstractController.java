package org.example.reimbursement.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract controller with common functionality.
 *
 * @author Jorge Ferreira.
 * @since 10/02/2021.
 */
public class AbstractController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ObjectMapper objectMapper;

    /**
     * Servlet initialization method.
     *
     * @param config Configuration attributes.
     */
    @Override
    public void init(ServletConfig config) {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Open a SQL connection.
     *
     * @return The SQL database connection.
     * @throws SQLException    Error connecting to the database.
     * @throws NamingException Error finding Tomcat resource.
     */
    public Connection openConnection() throws SQLException, NamingException {
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/reimbursement-ds");
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    /**
     * Get JSON object mapper.
     *
     * @return The JSON mapper.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Write output JSON with status 200 (Ok).
     *
     * @param obj  The object to serialize.
     * @param resp The response object.
     * @throws IOException Error while serializing.
     */
    public void writeOk(Object obj, HttpServletResponse resp) throws IOException {
        String json = getObjectMapper().writeValueAsString(obj);
        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }

    /**
     * Write output JSON with status 500 (Internal Server Error).
     *
     * @param ex   The exception thrown.
     * @param resp The response object.
     * @throws IOException Error while serializing.
     */
    public void writeError(Exception ex, HttpServletResponse resp) throws IOException {
        String json = "{\"error\": \"" + ex.getMessage() + "\"}";
        resp.setStatus(500);
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }
}

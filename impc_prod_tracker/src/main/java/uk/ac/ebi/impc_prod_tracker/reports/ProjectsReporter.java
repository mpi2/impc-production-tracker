package uk.ac.ebi.impc_prod_tracker.reports;

import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Create a report that represents the project page on the web site with appropriate filter options
 */
@Component
public class ProjectsReporter {

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        System.out.println("running main method here");
//        ProjectsReporter reporter = new ProjectsReporter();
//        try {
//            reporter.printReport();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    public void printReport(PrintWriter printWriter) throws IOException, SQLException {
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
        String query = "select * from project";
        statement.execute(query);
        ResultSet rs = statement.getResultSet();
        CSVWriter csvWriter = new CSVWriter(printWriter, CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeAll(rs, true);//note looks like no newlines in browser but they are there.
        csvWriter.close();
        //System.out.println(writer.toString());
        statement.close();
        conn.close();
    }

}

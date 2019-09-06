package uk.ac.ebi.impc_prod_tracker.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

import javax.sql.DataSource;
import java.io.IOException;
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
        ProjectsReporter reporter = new ProjectsReporter();
        try {
            reporter.printReport();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void printReport() throws IOException, SQLException {
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
        String query = "select * from project";
        statement.execute(query);
        ResultSet rs = statement.getResultSet();
        while (rs.next()) {
            System.out.println(rs.getString("id"));
        }
        statement.close();
        conn.close();
        Project project = new Project();
        System.out.println("test running here " + project);


//EntityManager em=
//        Query q = em.createNativeQuery("SELECT a.firstname, a.lastname FROM Author a");
//        List<Object[]> authors = q.getResultList();
//
//        for (Object[] a : authors) {
//            System.out.println("Author "
//                    + a[0]
//                    + " "
//                    + a[1]);
//        }
//        CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"), '\t');
//        Boolean includeHeaders = true;
//
//        java.sql.ResultSet myResultSet = .... //your resultset logic here
//
//        writer.writeAll(myResultSet, includeHeaders);
//
//        writer.close();
    }

}

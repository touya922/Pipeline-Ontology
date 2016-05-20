package semCal;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import GML.QueryResult;

@Path("/SemQuery")
public class SemQuery {

	@GET
	@Path("/{queryText}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getResult(@PathParam("queryText") String queryText) {

		String strResult="";
		//test test1=new test();
		SemiCalculation semCal = new SemiCalculation();
		//strResult = semCal.semCal(queryText.split(",")[0], queryText.split(",")[1]);
		semCal.semCal();

		ArrayList<QueryResult> re = new ArrayList<QueryResult>();

		QueryResult qr = new QueryResult();
		qr.guid = "1";
		qr.geoType = "YL";
		qr.similarity = 1.0;
		re.add(qr);
		// return "1";
		return strResult;
	}

}

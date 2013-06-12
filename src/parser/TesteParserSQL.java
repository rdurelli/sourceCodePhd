package parser;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.TreeSet;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.Table;

public class TesteParserSQL {
	
	
	public static void main(String[] args) {
		
		
		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();
		
		String update = "UPDATE CLIENTE SET usuario = '   ', usuario2 = '   ' WHERE usuario3 = '   ' AND usuario500 = '   ' ;";
		
		
		// call the SQLParser to verify the SQL obtained
		parserSQL.initParser(new ByteArrayInputStream(update.getBytes()));
		
		try {

			// read the statement and transform it to ZStatement,
			// afterwards it is casted to ZUpdate
			ZStatement sqlStatement = parserSQL.readStatement();

			// cast the ZStatement to ZUpdate to get the information
			// in the Update clause
			if (sqlStatement instanceof ZUpdate) {

				// where the cast really occurs
				ZUpdate sqlUpdate = (ZUpdate) sqlStatement;

				// get the name of the Table used in the UPDATE
				// statement
				String tableName = sqlUpdate.getTable().toUpperCase();

				System.out.println("name of the Table " + tableName);

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

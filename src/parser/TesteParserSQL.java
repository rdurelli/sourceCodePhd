package parser;

import java.io.ByteArrayInputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZDelete;
import org.gibello.zql.ZInsert;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.Table;

public class TesteParserSQL {
	
	
	public static void main(String[] args) {
		
		
		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();
		
		String update = "SELECT ANTIQUEOWNERS.OWNERLASTNAME, ANTIQUEOWNERS.OWNERFIRSTNAME FROM ANTIQUEOWNERS, ANTIQUES, ALUNO, CLIENTE WHERE ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID AND ANTIQUES.ITEM = 'Chair';";
		
		String testeaaa = "SELECT DISTINCT coddiscip_insc, turma_insc, nome_discip, sigla_cur FROM alunograd, enfasegrad, disciplinagrad, cursograd, inscricaograd, turmagrad".concat(";"); 
		
		
//		Pattern pattern = Pattern.compile("where", Pattern.CASE_INSENSITIVE);
//		
//		Matcher matcher = pattern.matcher(testeaaa);
//		
//		if (matcher.find()) {
//			
//			System.out.println(pattern.toString());
//			
//			System.out.println(matcher.group());
//			
//		}
		
		
		String[] cortouOWhere = testeaaa.split("(?i)where");
		
		System.out.println(cortouOWhere.length);
		
		System.out.println(cortouOWhere[0]);
		
		// call the SQLParser to verify the SQL obtained
		parserSQL.initParser(new ByteArrayInputStream(cortouOWhere[0].getBytes()));
		
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

			} else if (sqlStatement instanceof ZQuery) {
				
				// where the cast really occurs
				ZQuery sqlSelect = (ZQuery) sqlStatement;
				
				Vector<?> vectorTEste = sqlSelect.getFrom();
				
			System.out.println(sqlSelect.getWhere());
				
				for (Object object : vectorTEste) {
					System.out.println("Aqui " + object);
				}
				
			} else if (sqlStatement instanceof ZDelete) {
				
				ZDelete sqlInsert = (ZDelete) sqlStatement;
				
				
				
				
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
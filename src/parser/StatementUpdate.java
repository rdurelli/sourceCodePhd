package parser;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;

public class StatementUpdate extends StatementCRUD {

	@Override
	public void createStatement(Set<String> updates, DataBase dataBase) {

		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// iterate over all update statements found in the source code
		for (String update : updates) {

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

					// use the name of the table obtained and
					// instantiate a Table class
					Table table = new Table(tableName);

					// add the Table instance to the Set<Table>.
					// It is worth highlighted that I have choose to
					// Upper case the table name and put it to an Set..
					// once Set does't permit identical objects
					if (dataBase.getDataBaseTables().add(table)) {//TODO

						Set<Column> columnsFound = new TreeSet<Column>();

						table.setColumnsTable(columnsFound);
						//TODO removi aqui pois eu n�o quero pegar o tipo por meio da STRING. O banco de dados que tem que se virar e me trazer os nomes das colunas e os tipos...
//						addColumnToClauseUpdate(tableName,
//								dataBase.getDataBaseTables(), sqlStatement);

					}
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Iterator<Table> iterator = dataBase.getDataBaseTables().iterator();

		// if(dataBase.getDataBaseTables().contains(new
		// Table("CLIENTE"))) {
		//
		// System.out.println("Sim sim Contains mesmo..");
		//
		// }

		while (iterator.hasNext()) {
			Table table = (Table) iterator.next();

			Set<Column> columnsOfTable = table.getColumnsTable();

			Iterator<Column> iteratorColumn = columnsOfTable.iterator();

			System.out.println("O nome da tabela � " + table.getTableName());
			while (iteratorColumn.hasNext()) {

				Column column = (Column) iteratorColumn.next();

				System.out.println("Nome da COluna " + column.getColumnName());
			}

		}

		// System.out.println("Table obtida � " +
		// dataBase.getDataBaseTables().iterator());
		// tem que colocar isso para obter o tipo da coluna.
		super.getColumnType(dataBase.getDataBaseTables());

	}

	private void addColumnToClauseUpdate(String tableName, Set<Table> tables,
			ZStatement zStatement) {

		if (tables.contains(new Table(tableName))) {

			Iterator<Table> tableToAddColumn = tables.iterator();

			while (tableToAddColumn.hasNext()) {
				Table table = (Table) tableToAddColumn.next();
				if (table.getTableName().equals(tableName)) {

					if (zStatement instanceof ZUpdate) {

						ZUpdate zUpdate = (ZUpdate) zStatement;

						Hashtable hashSET = zUpdate.getSet();

						Enumeration<?> columns = hashSET.keys();

						while (columns.hasMoreElements()) {
							Object column = (Object) columns.nextElement();

							table.getColumnsTable().add(
									new Column(column.toString()));

						}

						ZExpression zExpression = (ZExpression) zUpdate
								.getWhere();

						List operands = zExpression.getOperands();

						// System.out.println(" O tamanho da Lista " +
						// operands.size());

						for (Object object : operands) {

							String[] column = object.toString().split(
									"\\W*\\s*");

							System.out.println(" O tamanho da Vector �  "
									+ column.length);

							if (column.length > 0) {

								String createTheColumnName = "";

								for (String columnName : column) {
									System.err.println("Elementos do Vector "
											+ columnName);
									createTheColumnName += columnName;
								}

								table.getColumnsTable().add(
										new Column(createTheColumnName));
							}

						}

					}

				}
			}

		}

	}

	public String getTableName(String statmentUpdate) {

		String tableName = null;

		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// call the SQLParser to verify the SQL obtained
		parserSQL
				.initParser(new ByteArrayInputStream(statmentUpdate.getBytes()));

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
				tableName = sqlUpdate.getTable().toUpperCase();

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableName;
	}

}

package parser;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZDelete;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZqlParser;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;

public class StatementDelete extends StatementCRUD {

	@Override
	public void createStatement(Set<String> delets, DataBase dataBase) {

		StatementCRUD.delets = delets;

		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// iterate over all delets statements found in the source code
		for (String delet : delets) {

			// call the SQLParser to verify the SQL obtained
			parserSQL.initParser(new ByteArrayInputStream(delet.getBytes()));

			try {

				// read the statement and transform it to ZStatement,
				// afterwards it is casted to ZUpdate
				ZStatement sqlStatement = parserSQL.readStatement();

				// cast the ZStatement to ZUpdate to get the information
				// in the Update clause
				if (sqlStatement instanceof ZDelete) {

					// where the cast really occurs
					ZDelete sqlDelete = (ZDelete) sqlStatement;

					// get the name of the Table used in the UPDATE
					// statement
					String tableName = sqlDelete.getTable().toUpperCase();

					// use the name of the table obtained and
					// instantiate a Table class
					Table table = new Table(tableName);

					// add the Table instance to the Set<Table>.
					// It is worth highlighted that I have choose to
					// Upper case the table name and put it to an Set..
					// once Set does't permit identical objects
					dataBase.getDataBaseTables().add(table);

					Set<Column> columnsFound = new TreeSet<Column>();

					table.setColumnsTable(columnsFound);

					addColumnToClauseDelete(tableName,
							dataBase.getDataBaseTables(), sqlStatement);

				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// Iterator<Table> iterator = dataBase.getDataBaseTables()
		// .iterator();
		//
		//
		// while (iterator.hasNext()) {
		// Table table = (Table) iterator.next();
		//
		// Set<Column> columnsOfTable = table.getColumnsTable();
		//
		// Iterator<Column> iteratorColumn = columnsOfTable.iterator();
		//
		// System.out.println("O nome da tabela Ž "
		// + table.getTableName());
		// while (iteratorColumn.hasNext()) {
		//
		// Column column = (Column) iteratorColumn.next();
		//
		// System.out.println("Nome da COluna "
		// + column.getColumnName());
		// }
		//
		// }

		// System.out.println("Table obtida Ž " +
		// dataBase.getDataBaseTables().iterator());
		// tem que colocar isso para obter o tipo da coluna.
		getColumnType(dataBase.getDataBaseTables());

	}

	private void addColumnToClauseDelete(String tableName, Set<Table> tables,
			ZStatement zStatement) {

		if (tables.contains(new Table(tableName))) {

			Iterator<Table> tableToAddColumn = tables.iterator();

			while (tableToAddColumn.hasNext()) {
				Table table = (Table) tableToAddColumn.next();
				if (table.getTableName().equals(tableName)) {

					if (zStatement instanceof ZDelete) {

						ZDelete zDelete = (ZDelete) zStatement;

						ZExpression zExpression = (ZExpression) zDelete
								.getWhere();

						List operands = zExpression.getOperands();

						System.out.println("Operadores do DELETE " + operands);
						System.out.println("Operador tamanho "
								+ operands.size());

						if (!operands.contains("?")) {

							for (Object object : operands) {

								String[] column = object.toString().split(
										"\\W*");

								String createTheColumnName = "";

								for (String columnName : column) {
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
	
	@Override
	public String getTableName(String statmentDelete) {

		String tableName = null;
		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// call the SQLParser to verify the SQL obtained
		parserSQL
				.initParser(new ByteArrayInputStream(statmentDelete.getBytes()));

		try {

			// read the statement and transform it to ZStatement,
			// afterwards it is casted to ZUpdate
			ZStatement sqlStatement = parserSQL.readStatement();

			// cast the ZStatement to ZUpdate to get the information
			// in the Update clause
			if (sqlStatement instanceof ZDelete) {

				// where the cast really occurs
				ZDelete sqlDelete = (ZDelete) sqlStatement;

				// get the name of the Table used in the UPDATE
				// statement
				tableName = sqlDelete.getTable().toUpperCase();

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableName;
	}

}

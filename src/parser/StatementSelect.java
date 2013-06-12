package parser;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.gibello.zql.ParseException;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZqlParser;

import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;

public class StatementSelect extends StatementCRUD {

	@Override
	public void createStatement(Set<String> selects, DataBase dataBase) {

		StatementCRUD.selects = selects;

		// System.out.println("Entrou no Select ");

		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// iterate over all update statements found in the source code
		for (String select : selects) {

			// call the SQLParser to verify the SQL obtained
			parserSQL.initParser(new ByteArrayInputStream(select.getBytes()));

			try {

				// read the statement and transform it to ZStatement,
				// afterwards it is casted to ZUpdate
				ZStatement sqlStatement = parserSQL.readStatement();

				// cast the ZStatement to ZUpdate to get the information
				// in the Update clause
				if (sqlStatement instanceof ZQuery) {

					// where the cast really occurs
					ZQuery sqlSelect = (ZQuery) sqlStatement;

					// get the name of the Table used in the ZQuery
					// statement

					List<?> tableSelect = sqlSelect.getFrom();

					// coloquei isso aqui, pq eu s— consegui pensar como pegar
					// SELECT do tipo SELECT alunoID, alunoName from ALUNO where
					// etc..
					// n‹o consigo pegar com o FROM tem duas tabelas...
					if (!(tableSelect.size() > 1)) {

						String tableName = tableSelect.get(0).toString()
								.toUpperCase();

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

						addColumnToClauseSelect(tableName,
								dataBase.getDataBaseTables(), sqlStatement);

					}
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// tem que colocar isso para obter o tipo da coluna.
		getColumnType(dataBase.getDataBaseTables());

	}

	private void addColumnToClauseSelect(String tableName, Set<Table> tables,
			ZStatement zStatement) {

		if (tables.contains(new Table(tableName))) {

			Iterator<Table> tableToAddColumn = tables.iterator();

			while (tableToAddColumn.hasNext()) {
				Table table = (Table) tableToAddColumn.next();
				if (table.getTableName().equals(tableName)) {

					if (zStatement instanceof ZQuery) {

						ZQuery zInsert = (ZQuery) zStatement;

						List<?> columnOfClauseInsert = zInsert.getSelect();

						if (!((columnOfClauseInsert.size() == 1) && (columnOfClauseInsert
								.get(0).toString().equals("*")))) {

							for (Object object : columnOfClauseInsert) {
								table.getColumnsTable().add(
										new Column(object.toString()));
							}

							ZExpression zExpressionInsert = (ZExpression) zInsert
									.getWhere();

							if (zExpressionInsert != null) {

								List<?> columnOfClauseInsertWhere = zExpressionInsert
										.getOperands();

								for (Object object : columnOfClauseInsertWhere) {

									String[] column = object.toString().split(
											"\\W*\\s*");

									// System.out.println(" O tamanho da Vector Ž  "
									// + column.length);

									if (column.length > 0) {

										String createTheColumnName = "";

										for (String columnName : column) {
											// System.err.println("Elementos do Vector "+
											// columnName);
											createTheColumnName += columnName;
										}

										table.getColumnsTable()
												.add(new Column(
														createTheColumnName));
									}

								}

							}

						}
					}

				}
			}

		}

	}

	@Override
	public String getTableName(String statmentSelect) {

		
		String tableName = null;
		// instanciate the SQL parser to get the information
		ZqlParser parserSQL = new ZqlParser();

		// call the SQLParser to verify the SQL obtained
		parserSQL
				.initParser(new ByteArrayInputStream(statmentSelect.getBytes()));

		// read the statement and transform it to ZStatement,
		// afterwards it is casted to ZUpdate
		ZStatement sqlStatement;
		try {
			sqlStatement = parserSQL.readStatement();
			// cast the ZStatement to ZUpdate to get the information
			// in the Update clause
			if (sqlStatement instanceof ZQuery) {

				// where the cast really occurs
				ZQuery sqlSelect = (ZQuery) sqlStatement;

				// get the name of the Table used in the ZQuery
				// statement

				List<?> tableSelect = sqlSelect.getFrom();

				// coloquei isso aqui, pq eu s— consegui pensar como pegar
				// SELECT do tipo SELECT alunoID, alunoName from ALUNO where
				// etc..
				// n‹o consigo pegar com o FROM tem duas tabelas...
				if (!(tableSelect.size() > 1)) {

					tableName = tableSelect.get(0).toString()
							.toUpperCase();

				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableName;
	}

}

package parser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.antlr.runtime.CharStream;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.gibello.zql.ZDelete;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;

import com.br.connection.factory.ConnectionFactory;
import com.br.databaseDDL.Column;
import com.br.databaseDDL.DataBase;
import com.br.databaseDDL.Table;

public class Teste1 {

	private DataBase dataBase;

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public void callParser(String javaFile) {

		CharStream stream = null;
		try {
			stream = new org.antlr.runtime.ANTLRFileStream(javaFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {

			parser.blockStatement();

			parser.localVariableDeclaration();

			Set<String> inserts = parser.getInserts();

			System.err.println("Quantos Inserts " + inserts.size());// TODO

			Set<String> selects = parser.getSelects();

			// String pattern = System.getProperty("line.separator") + " ";

			System.err.println("Quantos Selects: " + selects.size());

			for (String select : selects) {

				System.err.println("Select " + select);

			}

			Set<String> delets = parser.getDeletes();

			System.out.println("Quantos Deletes " + delets.size());

			for (String del : delets) {
				System.out.println("Delets " + del);
			}

			Set<String> updates = parser.getUpdates();

			System.err.println("Quantos Updates: " + updates.size());

			for (String string : updates) {
				System.err.println("Updates " + string);
			}

			// mudar aq
			this.setDataBase(new DataBase("projectValter"));

			Set<Table> tablesFound = new TreeSet<Table>();

			this.getDataBase().setDataBaseTables(tablesFound);

			if (updates.size() > 0) {

				System.out.println("Entrou no Updates");

				StatementCRUD up = new StatementUpdate();
				up.createStatement(updates, dataBase);

			}
			if (delets.size() > 0) {

				System.out.println("Deletes entrou e rolou");

				StatementCRUD dele = new StatementDelete();
				dele.createStatement(delets, dataBase);

			}
			if (selects.size() > 0) {

				System.out.println("Entrou no Select ");

				StatementCRUD sele = new StatementSelect();
				sele.createStatement(selects, dataBase);

			}

			// HashMap<String, String> names = parser.getNames();
			// System.out.println("AQUI "+ names.get("class_name"));
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

							System.out.println(" O tamanho da Vector é  "
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

									// System.out.println(" O tamanho da Vector é  "
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

						for (Object object : operands) {

							String[] column = object.toString().split("\\W*");

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

	private void getColumnType(Set<Table> tables) {

		Connection connection = ConnectionFactory.getInstance();

		try {
			Statement stmt = connection.createStatement();
			DatabaseMetaData metaData = connection.getMetaData();

			ResultSet rSeltMeta = metaData.getTables(null, null, "%", null);

			while (rSeltMeta.next()) {

				if (tables.contains(new Table(rSeltMeta.getString(3)))) {

					Table tablesObtained = tables.iterator().next();

					Set<Column> columnObtained = tablesObtained
							.getColumnsTable();

					// System.out.println("O nome da tabela OBTIDA é " +
					// teste.getTableName());

					// System.err.println("Sim contem ");

					ResultSet resultSetMetaData = stmt
							.executeQuery("SELECT * FROM "
									+ tablesObtained.getTableName());

					ResultSetMetaData rsMeta = resultSetMetaData.getMetaData();

					Iterator<Column> iterator = columnObtained.iterator();

					// Column columnToAddType =
					// columnObtained.iterator().next();

					for (int j = 0; j < rsMeta.getColumnCount(); j++) {

						iterator.hasNext();
						if (columnObtained.contains(new Column(rsMeta
								.getColumnName(j + 1)))) {

							Column columnToAddType = iterator.next();

							System.err.println("O que tem no getColumnName "
									+ rsMeta.getColumnName(j + 1));

							columnToAddType.setColumnType(rsMeta
									.getColumnTypeName(j + 1));

							System.err.println("O nome da COLUNA é "
									+ columnToAddType.getColumnName());

							System.err.println("O type é COLUNA é "
									+ columnToAddType.getColumnType());

							if (rsMeta.isAutoIncrement(j + 1)) {

								columnToAddType.setIsPrimaryKey(true);

							} else {

								columnToAddType.setIsPrimaryKey(false);

							}

						} else {

							Column newColumn = new Column(
									rsMeta.getColumnName(j + 1));
							System.err.println("O nome da COLUNA no else é "
									+ newColumn.getColumnName());

							newColumn.setColumnType(rsMeta
									.getColumnTypeName(j + 1));

							System.err.println("O type da COLUNA no else é "
									+ newColumn.getColumnType());

							if (rsMeta.isAutoIncrement(j + 1)) {

								newColumn.setIsPrimaryKey(true);

							} else {

								newColumn.setIsPrimaryKey(false);

							}
							columnObtained.add(newColumn);
							columnObtained.iterator().hasNext();

						}

						// System.out.println(rsMeta.getColumnTypeName(j + 1));
						// System.out.println(rsMeta.getColumnType(j + 1));
						// System.out.println(rsMeta.getColumnDisplaySize(j +
						// 1));
						// System.out.println(rsMeta.getColumnName(j + 1));
						// System.out.println(rsMeta.isAutoIncrement(j + 1));

					}

				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

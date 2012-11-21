package parser;


import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.StyledEditorKit.ItalicAction;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.gibello.zql.ParseException;
import org.gibello.zql.ZDelete;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZQuery;
import org.gibello.zql.ZStatement;
import org.gibello.zql.ZUpdate;
import org.gibello.zql.ZqlParser;

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
	
	

	public void callParser() {
		CharStream stream = new ANTLRStringStream(
				"public class AlteracaoEmail extends HttpServlet\n" + 
				"{\n" + 
				"    public AlteracaoEmail() {\n" + 
				"        try {\n" + 
				"            jbInit();\n" + 
				"        } catch (Exception ex) {\n" + 
				"            ex.printStackTrace();\n" + 
				"        }\n" + 
				"    }\n" + 
				"\n" + 
				"    public void doPost (HttpServletRequest req, HttpServletResponse res)\n" + 
				"      throws ServletException, IOException\n" + 
				"  {\n" + 
				"        HttpSession session = req.getSession(true);\n" + 
				"    // autenticando sess�o\n" + 
				"    if (AliasLogin.AutenticarSessao(req))\n" + 
				"    {\n" + 
				"    //String sUsuario = req.getParameter(\"Usuario\");\n" + 
				"    //boolean obrigatorio = req.getParameter(\"Obrigatorio\").equals(\"S\");\n" + 
				"\n" + 
				"    String sUsuario     = (String)session.getValue(\"userSet\");\n" + 
				"\n" + 
				"    boolean obrigatorio = ((String)session.getValue(\"Obrigatorio\")).equals(\"S\");\n" + 
				"        if (req.getParameter(\"Obrigatorio\")!=null)\n" + 
				"            obrigatorio = req.getParameter(\"Obrigatorio\").equals(\"S\");\n" + 
				"    session.putValue(\"Obrigatorio\",(obrigatorio ? \"S\" : \"N\"));\n" + 
				"\n" + 
				"    res.setContentType(\"text/html\");\n" + 
				"    ServletOutputStream out = res.getOutputStream();\n" + 
				"\n" + 
				"\n" + 
				"    try {\n" + 
				"       // conex�o JDBC ( via string / via dbcp )\n" + 
				"       InitialContext context  = null;\n" + 
				"\n" + 
				"       Connection conn         = null;\n" + 
				"       Statement stmtEmail     = null;\n" + 
				"       ResultSet rsetEmail     = null;\n" + 
				"       Statement stmtCargo     = null;\n" + 
				"       ResultSet rsetCargo     = null;\n" + 
				"       ResultSet rsetAluno     = null;\n" + 
				"\n" + 
				"       try\n" + 
				"       {\n" + 
				"          conn      = AliasLogin.AbrirConexao(AliasLogin.forma,conn,context);\n" + 
				"          stmtEmail = conn.createStatement();\n" + 
				"          stmtCargo = conn.createStatement();\n" + 
				"       }\n" + 
				"       catch (Exception e)\n" + 
				"       {\n" + 
				"          System.out.println(\"Ocorreu um erro na tentativa de estabelecer uma conexão com o banco de dados!\");\n" + 
				"       }\n" + 
				"\n" + 
				"      out.println(\"<html>\");\n" + 
				"      out.println(\"<head>\");\n" + 
				"      out.println(\"<title>ProGrad Web - Atualiza&ccedil;&atilde;o de E-mail Institucional</title>\");\n" + 
				"      out.println(\"<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\");\n" + 
				"\n" + 
				"      out.println(\"<script language = 'JavaScript'>\");\n" + 
				"      out.println(\"  function validar() {\");\n" + 
				"      out.println(\"    if((window.document.fEmail.Email[0].value == '') && (window.document.fEmail.Email[1].value == ''))\");\n" + 
				"      out.println(\"      alert('é necessário que pelo menos um e-mail seja informado!');\");\n" + 
				"      out.println(\"    else\");\n" + 
				"      out.println(\"      if((window.document.fEmail.Email[0].value.indexOf('.com') >= 0) || (window.document.fEmail.Email[1].value.indexOf('.com') >= 0))\");\n" + 
				"      out.println(\"        alert('O e-mail informado deve ser Institucional.\\\\nObserve a terminação \\\".ufscar.br\\\" na frente de ambos os campo de e-mail.');\");\n" + 
				"      out.println(\"      else\");\n" + 
				"      out.println(\"        if((window.document.fEmail.Email[0].value.indexOf('.ufscar') >= 0) || (window.document.fEmail.Email[1].value.indexOf('.ufscar') >= 0))\");\n" + 
				"      out.println(\"          alert('O e-mail deve ser informado sem a terminação \\\".ufscar.br\\\", \\\\nque já se encontra na frente de ambos os campos de e-mail.');\");\n" + 
				"      out.println(\"        else\");\n" + 
				"      out.println(\"          window.document.fEmail.submit();\");\n" + 
				"      out.println(\"  }\");\n" + 
				"      out.println(\"</script>\");\n" + 
				"\n" + 
				"      out.println(\"</head>\");\n" + 
				"      out.println(\"<body bgcolor='#FDFFDD'>\");\n" + 
				"      out.println(\"<center>\");\n" + 
				"      out.println(\"  <table width='90%' border='0'>\");\n" + 
				"      out.println(\"    <tr>\");\n" + 
				"      out.println(\"      <td><font face='Arial' size='4'>Atualiza&ccedil;&atilde;o de e-mail institucional</font><br><br>\");\n" + 
				"      out.println(\"        <font face='Arial' size='2'>O e-mail institucional &eacute; a maneira mais r&aacute;pida e segura para sua intera&ccedil;&atilde;o com a Universidade. Por exemplo, caso voc&ecirc; esque&ccedil;a a sua senha de utiliza&ccedil;&atilde;o do ProGrad Web / Nexos, a mesma poder&aacute; ser enviada nesse seu e-mail institucional. Portanto, por gentileza preencha os campos abaixo e clique no bot&atilde;o &quot;Salvar&quot;.<br><br>\");\n" + 
				"      out.println(\"        <b>Obs: O e-mail preenchido deve pertencer &agrave; UFSCar, como indicado abaixo:</b><br><br>\");\n" + 
				"      out.println(\"        <font color='#006600'><strong>nome@depto</strong></font> ou <font color='#006600'><strong>nome@power</strong></font> e para ALUNOS <font color='#006600'><strong>gRA@polvo, ex:(g76201@polvo)</strong></font>, pois a extensão <font color='#006600'><strong> .ufscar.br </strong></font> é fixo.<br>Caso tais regras não sejam satisfeitas, você poderá não receber informações do ProgradWEB via e-mail.</strong></font>\");\n" + 
				"      out.println(\"      </td>\");\n" + 
				"      out.println(\"    </tr>\");\n" + 
				"      out.println(\"  </table>\");\n" + 
				"      out.println(\"  <form name='fEmail' method='post' action='/progradweb/servlet/AlteracaoEmailGravar'>\");\n" + 
				"      out.println(\"    <table width='90%' border='0'>\");\n" + 
				"      out.println(\"      <tr bgcolor='#FFCC33'>\");\n" + 
				"      out.println(\"        <td width='28%'><font face='Arial' size='2'>Usu&aacute;rio:</font></td>\");\n" + 
				"      out.println(\"        <td width='72%'><font face='Arial' size='2'>\" + sUsuario + \"</font></td>\");\n" + 
				"      out.println(\"      </tr>\");\n" + 
				"      out.println(\"      <tr bgcolor='#FFCC33'>\");\n" + 
				"      out.println(\"        <td width='28%' valign='top'><font face='Arial' size='2'>Cargo(s):</font></td>\");\n" + 
				"      out.println(\"        <td width='72%'><font face='Arial' size='2'>\");\n" + 
				"\n" + 
				"      rsetCargo = stmtCargo.executeQuery(\"SELECT 2 AS Ordem, IdCargo, Descricao, DeptoCurso, NomeCompleto, Vice \" +\n" + 
				"                  \"FROM Ocupantes, OcupanteCargo, TipoCargos \" +\n" + 
				"                  \"WHERE (Ocupantes.IdOcupante = OcupanteCargo.IdOcupante) AND \" +\n" + 
				"                  \"(IdCargo = IdTipoCargo) AND \" +\n" + 
				"                  \"(Usuario = '\" + sUsuario + \"') \" +\n" + 
				"                  \"UNION \" +\n" + 
				"                  \"SELECT 1, IdTipo + 50, Descricao, '', Nome, 0 \" +\n" + 
				"                  \"FROM Usuarios, TipoCargos \" +\n" + 
				"                  \"WHERE (IdTipo + 50 = IdTipoCargo) AND \" +\n" + 
				"                  \"(Usuario = '\" + sUsuario + \"') \" +\n" + 
				"                  \"ORDER BY Ordem, IdCargo\");  \n" + 
				"        SELECT 2 AS Ordem, IdCargo, Descricao, DeptoCurso, NomeCompleto, Vice \n" + 
				"        FROM Ocupantes, OcupanteCargo, TipoCargos \n" + 
				"        WHERE (Ocupantes.IdOcupante = OcupanteCargo.IdOcupante) AND \n" + 
				"        (IdCargo = IdTipoCargo) AND \n" + 
				"        (Usuario = '   ') \n" + 
				"        UNION \n" + 
				"        SELECT 1, IdTipo + 50, Descricao, '', Nome, 0 \n" + 
				"        FROM Usuarios, TipoCargos \n" + 
				"        WHERE (IdTipo + 50 = IdTipoCargo) AND \n" + 
				"        (Usuario = '   ') \n" + 
				"        ORDER BY Ordem, IdCargo ;\n" + 
				"      int numeroCargos = 0;\n" + 
				"      String sigla = \"\";\n" + 
				"      while(rsetCargo.next()) {\n" + 
				"        if(numeroCargos > 0)\n" + 
				"          out.println(\"<br>\");\n" + 
				"\n" + 
				"        sigla = rsetCargo.getString(\"DeptoCurso\");\n" + 
				"        if(sigla == null)\n" + 
				"          sigla = \"\";\n" + 
				"        out.print(\"          \" + (rsetCargo.getInt(\"Vice\") == 1 ? \"Vice-\" : \"\") + rsetCargo.getString(\"Descricao\") + (sigla.equals(\"\") ? \"\" : \" - \" + sigla));\n" + 
				"\n" + 
				"        numeroCargos++;\n" + 
				"      }\n" + 
				"\n" + 
				"      out.println(\"</font></td>\");\n" + 
				"      out.println(\"      </tr>\");\n" + 
				"\n" + 
				"      String updates = \"UPDATE CLIENTE SET usuario = '\" + sUsuario + \"', usuario2 = '\" + sUsuario + \"' WHERE usuario3 = '\" + sUsuario + \"' AND usuario500 = '\" + sUsuario + \"'\";\n" + 
				"      String updates2 = \"UPDATE PERSONS SET usuario = '\" + sUsuario + \"', usuario2 = '\" + sUsuario + \"' WHERE usuario3 = '\" + sUsuario + \"'\";\n" + 
				"      String deletes = \"DELETE FROM PERSONS WHERE usuario = '\" + sUsuario + \"' AND usuario2 = '\" + sUsuario + \"'\";\n" + 
				"\n" + 
				"      rsetEmail = stmtEmail.executeQuery(\"SELECT email FROM Usuarios \" +\n" + 
				"                  \"WHERE usuario = '\" + sUsuario + \"' \" +\n" + 
				"                  \"UNION \" +\n" + 
				"                  \"SELECT email FROM Ocupantes \" +\n" + 
				"                  \"WHERE usuario = '\" + sUsuario + \"'\"); \n" + 
				"\n" + 
				"rsetEmail = stmtEmail.executeQuery(\"SELECT id, name, lastName FROM ALUNO \");\n" + 
				"\n" + 
				"      rsetEmail.next();\n" + 
				"\n" + 
				"      String emailPrincipal, emailSecundario = \"\";\n" + 
				"      emailPrincipal = rsetEmail.getString(\"email\");\n" + 
				"\n" + 
				"      if(emailPrincipal == null)\n" + 
				"        emailPrincipal = \"\";\n" + 
				"\n" + 
				"      if(!emailPrincipal.equals(\"\"))\n" + 
				"        if(emailPrincipal.indexOf(\",\") > -1) {\n" + 
				"          emailSecundario = emailPrincipal.substring(emailPrincipal.indexOf(\",\") + 1);\n" + 
				"          emailPrincipal = emailPrincipal.substring(0, emailPrincipal.indexOf(\",\"));\n" + 
				"        }\n" + 
				"      emailPrincipal = Funcoes.replaceText(emailPrincipal, \".ufscar.br\", \"\");\n" + 
				"      emailSecundario = Funcoes.replaceText(emailSecundario, \".ufscar.br\", \"\");\n" + 
				"\n" + 
				"      out.println(\"      <tr bgcolor='#FFCC33'>\");\n" + 
				"      out.println(\"        <td width='28%'><font face='Arial' size='2'>E-mail principal: </font></td>\");\n" + 
				"      out.println(\"        <td width='72%'><font face='Arial' size='2'><input type='text' name='Email' maxlength='39' size='40' value='\" + emailPrincipal + \"'> .ufscar.br</font></td>\");\n" + 
				"      out.println(\"      </tr>\");\n" + 
				"      out.println(\"      <tr bgcolor='#FFCC33'>\");\n" + 
				"      out.println(\"        <td width='28%'><font face='Arial' size='2'>E-mail secundario: </font></td>\");\n" + 
				"      out.println(\"        <td width='72%'><font face='Arial' size='2'><input type='text' name='Email' maxlength='39' size='40' value='\" + emailSecundario + \"'> .ufscar.br</font></td>\");\n" + 
				"      out.println(\"      </tr>\");\n" + 
				"      out.println(\"    </table>\");\n" + 
				"      out.println(\"    <input type='hidden' name='Usuario' value='\" + sUsuario + \"'>\");\n" + 
				"      out.println(\"    <input type='hidden' name='Obrigatorio' value='\" + (obrigatorio ? \"S\" : \"N\") + \"'>\");\n" + 
				"      out.println(\"  </form>\");\n" + 
				"\n" + 
				"      out.println(\"  <table width='90%' border='0'>\");\n" + 
				"      out.println(\"    <tr>\");\n" + 
				"      out.println(\"      <td><font face='Arial' size='2' color='#FF0000'>ATEN&Ccedil;&Atilde;O: Caso voc&ecirc; n&atilde;o possua um e-mail institucional, contate o seu departamento ou a SIn (Secretaria de Inform&aacute;tica, r.8150), para a cria&ccedil;&atilde;o do mesmo. \" + (obrigatorio ? \"Neste caso, por favor utilize o bot&atilde;o &quot;Cancelar&quot;.\" : \"\") + \"<td>\");\n" + 
				"      out.println(\"    </tr>\");\n" + 
				"      out.println(\"  </table>\");\n" + 
				"      out.println(\"  <br>\");\n" + 
				"\n" + 
				"      out.println(\"  <a href='javascript:validar();'><img border='0' src='/progradweb/salvar.gif' width='213' height='32'></a>\");\n" + 
				"      if(!obrigatorio)\n" + 
				"        out.println(\"  <a href='javascript:window.parent.frames[0].document.fPrincipal.submit();'><img border='0' src='/progradweb/voltarMenu.gif' width='213' height='32'></a>\");\n" + 
				"      else\n" + 
				"        out.println(\"  <a href='javascript:window.parent.location.reload();'><img border='0' src='/progradweb/cancelar.gif' width='213' height='32'></a>\");\n" + 
				"\n" + 
				"\n" + 
				"      // fecha a conex�o\n" + 
				"      AliasLogin.FecharContexto(context);\n" + 
				"      AliasLogin.FecharConexao(rsetEmail,stmtEmail,conn);\n" + 
				"      AliasLogin.FecharConexao(rsetCargo,stmtCargo);\n" + 
				"    }\n" + 
				"    catch (Exception e) {\n" + 
				"      out.println(\"Erro encontrado, favor reportar a seguinte mensagem: \" + e);\n" + 
				"    }\n" + 
				"\n" + 
				"    out.println(\"</center>\");\n" + 
				"    out.println(\"</body>\");\n" + 
				"    out.println(\"</html>\");\n" + 
				"    }\n" + 
				"    else\n" + 
				"    {\n" + 
				"     AliasLogin.FecharSessao(req);\n" + 
				"     res.sendRedirect(\"/progradweb/logoff.jsp\");\n" + 
				"    }\n" + 
				"   }\n" + 
				"   private void jbInit() throws Exception {\n" + 
				"   }\n" + 
				"}");

		JavaLexer lexer = new JavaLexer(stream);

		TokenStream tokenStream = new CommonTokenStream(lexer);

		JavaParser parser = new JavaParser(tokenStream);

		try {

			parser.blockStatement();

			parser.localVariableDeclaration();

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

				// instanciate the SQL parser to get the information
				ZqlParser parserSQL = new ZqlParser();

				// iterate over all update statements found in the source code
				for (String update : updates) {

					// call the SQLParser to verify the SQL obtained
					parserSQL.initParser(new ByteArrayInputStream(update
							.getBytes()));

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
							String tableName = sqlUpdate.getTable()
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

							addColumnToClauseUpdate(tableName,
									dataBase.getDataBaseTables(), sqlStatement);

						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Iterator<Table> iterator = dataBase.getDataBaseTables()
						.iterator();

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

					System.out.println("O nome da tabela é "
							+ table.getTableName());
					while (iteratorColumn.hasNext()) {

						Column column = (Column) iteratorColumn.next();

						System.out.println("Nome da COluna "
								+ column.getColumnName());
					}

				}

				// System.out.println("Table obtida é " +
				// dataBase.getDataBaseTables().iterator());

			}
			if (delets.size() > 0) {

				System.out.println("Deletes entrou e rolou");

				// instanciate the SQL parser to get the information
				ZqlParser parserSQL = new ZqlParser();

				// iterate over all delets statements found in the source code
				for (String delet : delets) {

					// call the SQLParser to verify the SQL obtained
					parserSQL.initParser(new ByteArrayInputStream(delet
							.getBytes()));

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
							String tableName = sqlDelete.getTable()
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

							addColumnToClauseDelete(tableName,
									dataBase.getDataBaseTables(), sqlStatement);

						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Iterator<Table> iterator = dataBase.getDataBaseTables()
						.iterator();

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

					System.out.println("O nome da tabela é "
							+ table.getTableName());
					while (iteratorColumn.hasNext()) {

						Column column = (Column) iteratorColumn.next();

						System.out.println("Nome da COluna "
								+ column.getColumnName());
					}

				}

				// System.out.println("Table obtida é " +
				// dataBase.getDataBaseTables().iterator());

			} if (selects.size() > 0) {
				
				
				System.out.println("Entrou no Select ");
				
				// instanciate the SQL parser to get the information
				ZqlParser parserSQL = new ZqlParser();

				// iterate over all update statements found in the source code
				for (String select : selects) {

					// call the SQLParser to verify the SQL obtained
					parserSQL.initParser(new ByteArrayInputStream(select
							.getBytes()));

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
							
							
							//coloquei isso aqui, pq eu só consegui pensar como pegar SELECT do tipo SELECT alunoID, alunoName from ALUNO where etc.. 
							//não consigo pegar com o FROM tem duas tabelas...
							if (! (tableSelect.size() > 1)) {
							
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

				Iterator<Table> iterator = dataBase.getDataBaseTables()
						.iterator();

				// if(dataBase.getDataBaseTables().contains(new
				// Table("CLIENTE"))) {
				//
				// System.out.println("Sim sim Contains mesmo..");
				//
				// }

				getColumnType(dataBase.getDataBaseTables());
				
				while (iterator.hasNext()) {
					Table table = (Table) iterator.next();

					Set<Column> columnsOfTable = table.getColumnsTable();

					Iterator<Column> iteratorColumn = columnsOfTable.iterator();

					System.out.println("O nome da tabela é "
							+ table.getTableName());
					while (iteratorColumn.hasNext()) {

						Column column = (Column) iteratorColumn.next();

						System.out.println("Nome da COluna "
								+ column.getColumnName());
						
						System.out.println("O type das colunas são "
								+ column.getColumnType());
						
						System.out.println("Is primary KEY "
								+ column.getIsPrimaryKey());
					}

				}
				
				//obtain the type of the columns by using the MetaData of JDBC...
				
				
				
				

				// System.out.println("Table obtida é " +
				// dataBase.getDataBaseTables().iterator());
				
				
				
			}

			// HashMap<String, String> names = parser.getNames();
			// System.out.println("AQUI "+ names.get("class_name"));
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addColumnToClauseUpdate(String tableName,
			Set<Table> tables, ZStatement zStatement) {

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

	private void addColumnToClauseSelect(String tableName,
			Set<Table> tables, ZStatement zStatement) {

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

	private  void addColumnToClauseDelete(String tableName,
			Set<Table> tables, ZStatement zStatement) {

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
	
	private  void getColumnType (Set<Table> tables){
		
		Connection connection = ConnectionFactory.getInstance();
		
		try {
			Statement stmt = connection.createStatement();
			DatabaseMetaData metaData = connection.getMetaData();
			
		    ResultSet rSeltMeta = metaData.getTables(null, null, "%", null);
		      
		      while (rSeltMeta.next()) {
				
		    	  if (tables.contains(new Table(rSeltMeta.getString(3)))){
		    		  
		    		  Table tablesObtained = tables.iterator().next();
		    		  
		    		  Set<Column> columnObtained = tablesObtained.getColumnsTable();
		    		  
//		    		  System.out.println("O nome da tabela OBTIDA é " + teste.getTableName());
		    		  
//		    		  System.err.println("Sim contem ");
		    		  
		    		  ResultSet resultSetMetaData = stmt.executeQuery("SELECT * FROM "+ tablesObtained.getTableName());
		    		  
		    		  ResultSetMetaData rsMeta = resultSetMetaData.getMetaData();
		    		  
		    		  Iterator<Column> iterator = columnObtained.iterator();
		    		  
//		    		  Column columnToAddType = columnObtained.iterator().next();
		    		  
		    		  for (int j = 0; j < rsMeta.getColumnCount(); j++) {
						
		    			  
		    			  iterator.hasNext();
		    			  if(columnObtained.contains(new Column(rsMeta.getColumnName(j + 1)))) {
		    				  
		    				  Column columnToAddType = iterator.next();
		    				  
		    				  System.err.println("O que tem no getColumnName " +rsMeta.getColumnName(j + 1));
		    				  
		    				  columnToAddType.setColumnType(rsMeta.getColumnTypeName(j + 1));
		    				  
		    				  System.err.println("O nome da COLUNA é " +columnToAddType.getColumnName());
		    				  
		    				  System.err.println("O type é COLUNA é " +columnToAddType.getColumnType());
		    				  
		    				  if(rsMeta.isAutoIncrement(j + 1)) {
		    					  
		    					  columnToAddType.setIsPrimaryKey(true);
		    					  
		    				  }else {
		    					  
		    					  columnToAddType.setIsPrimaryKey(false);
		    					  
		    				  }
		    				  
		    				  
		    			  } else {
		    				 
		    				  Column newColumn = new Column(rsMeta.getColumnName(j + 1));
		    				  System.err.println("O nome da COLUNA no else é " +newColumn.getColumnName());
		    				  
		    				  newColumn.setColumnType(rsMeta.getColumnTypeName(j + 1));
		    				  
		    				  System.err.println("O type da COLUNA no else é " +newColumn.getColumnType());
		    				  
		    				  if(rsMeta.isAutoIncrement(j + 1)) {
		    					  
		    					  newColumn.setIsPrimaryKey(true);
		    					  
		    				  } else {
		    					  
		    					  
		    					  newColumn.setIsPrimaryKey(false);
		    					  
		    				  }
		    				  columnObtained.add(newColumn);
		    				  columnObtained.iterator().hasNext();

		    			  }
		    			  
		    			  
//		    			  	System.out.println(rsMeta.getColumnTypeName(j + 1));
//		    	            System.out.println(rsMeta.getColumnType(j + 1));
//		    	            System.out.println(rsMeta.getColumnDisplaySize(j + 1));
//		    	            System.out.println(rsMeta.getColumnName(j + 1));
//		    	            System.out.println(rsMeta.isAutoIncrement(j + 1));
		    			  
					}
		    		  
		    	  }
		    	  
		    	  
		    	  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

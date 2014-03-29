package com.br.trace.refactoring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.br.connection.factory.ConnectionFactory;

public class PersisteTraceLogRefactoring {

	
	public static void saveTrace(String project, String refactoringRealized, String author) {
		
		
		String insertIntoTRACK_LOG = "INSERT INTO TRACK_LOG"
				+ "(project, change_refactoring, author) VALUES"
				+ "(?, ?, ?)";
		
		try {
			PreparedStatement preparedStatement = ConnectionFactory.getInstance().prepareStatement(insertIntoTRACK_LOG);
			
			preparedStatement.setString(1, project);
			preparedStatement.setString(2, refactoringRealized);
			preparedStatement.setString(3, author);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<TrackLog> getAllTraces() {
		
		
		String statement = "SELECT * FROM TRACK_LOG";
		
		ArrayList<TrackLog> trackLogs = new ArrayList<TrackLog>();
		
		try {
			PreparedStatement preparedStatement = ConnectionFactory.getInstance().prepareStatement(statement);
			
			ResultSet resultSET = preparedStatement.executeQuery();
			
			while (resultSET.next()) {
				
				int trackId = resultSET.getInt("id");
				String project = resultSET.getString("project");
				String change_refactoring = resultSET.getString("change_refactoring");
				String author = resultSET.getString("author");
				
				TrackLog trackLog = new TrackLog(trackId, project, change_refactoring, author);
				
				trackLogs.add(trackLog);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trackLogs;
	}
	
}

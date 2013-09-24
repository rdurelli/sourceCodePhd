package com.br.utils.modernization.kdm.DAO;

import org.eclipse.gmt.modisco.omg.kdm.kdm.Segment;

import com.br.databaseDDL.DataBase;
import com.br.util.models.UtilKDMModel;

public class ModernizationKDMToDAO {

	
	

	/**
	 * This method must be called to start the modernization of the KDM MODEL.  
	 *
	 * @param  kdmPath is a String that contains the path to the KDM to be put the news DAO
	 * @param  dataBase contains the elements to put in the segment
	 * @return      return a new Segment to be persist
	 * 
	 */
	public Segment start(String kdmPath, DataBase dataBase) {
		
		Segment segment = null;
		
		UtilKDMModel utilKDMModel = new UtilKDMModel();
		
		segment = utilKDMModel.load(kdmPath);
		
		
		
		
		return null;		
		
	} 
	
	
	
	
}

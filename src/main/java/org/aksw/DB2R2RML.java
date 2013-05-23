package org.aksw;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.print.URIException;
/*import org.aksw.commons.util.jdbc.ForeignKey;
import org.aksw.commons.util.jdbc.Schema;
import com.google.common.collect.Multimap;*/
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class DB2R2RML 
{
	/*private Model r2rmlGraph = ModelFactory.createDefaultModel();
	private Resource tripleMap ;
	private String dbUri, dataUri, vocUri, compPkSep;
	//private Properties dbProps;
	private Connection dbConnction;
	private DatabaseMetaData md;
	//////////////private Schema dbSchema;
	static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DB2R2RML.class);

	*//**
	 * Contractor that opens the database and initialize some basic variables 
	 * @param dbUrl
	 * @param dbUserName
	 * @param dbPassWord
	 * @param mappedDbUri
	 * @param userDataUri
	 * @param userVocUri
	 * @param userCompPkSep
	 * @author Sherif
	 *//*
	public DB2R2RML(String dbUrl, String dbUserName, String dbPassWord, String mappedDbUri, String userDataUri, String userVocUri, String userCompPkSep){
		String myDbUrl = dbUrl;
		String myDbUserName=dbUserName;
		String myDbPassWord=dbPassWord;
		dbUri  = mappedDbUri != null ? mappedDbUri : "http://example.com/mapping/" ;
		dataUri= userDataUri != null ? userDataUri : "http://example.com/data/";
		vocUri= userVocUri != null ? userVocUri : "http://example.com/vocabulary/";
		compPkSep= userCompPkSep != null ? userCompPkSep : ";";
		Properties dbProps = new Properties();
		dbProps.setProperty("user",myDbUserName);
		dbProps.setProperty("password", myDbPassWord);
		dbConnction = null;
		try {
			dbConnction = DriverManager.getConnection(myDbUrl, dbProps);
			md = dbConnction.getMetaData();
	////////////		dbSchema=Schema.create(dbConnction);
		} catch (SQLException e) {log.error("Error:",e);}

		
	}

	public DB2R2RML(Connection conn,  String mappedDbUri, String userDataUri, String userVocUri, String userCompPkSep) throws SQLException {
		this.dbConnction = conn;
//		this.md =  dbConnction.getMetaData();
		this.dbUri = mappedDbUri != null ? mappedDbUri : "http://example.com/mapping/" ;
		this.dataUri= userDataUri != null ? userDataUri : "http://example.com/data/";
		this.vocUri= userVocUri != null ? userVocUri : "http://example.com/vocabulary/";
		this.compPkSep  = userCompPkSep !=null? userCompPkSep:";";
		

	}
	public DB2R2RML(){}
	*//**
	 * @return the r2rmlGraph
	 *//*
	public Model getR2rmlGraph() {
		return r2rmlGraph;
	}

	public static void main( String[] args )
	{   
		DB2R2RML app = new DB2R2RML(args[0], args[1], args[2],args[3], args[4], args[5], args[6]);

		try {
			app.getMydbData().write(System.out,"TURTLE");
		} catch (SQLException e ) {
			log.error("Error:",e);
		}


	}

	public Model getMydbData() throws SQLException
	{

		try {

			java.sql.ResultSet catalogs = null;
			catalogs = md.getCatalogs();
			java.sql.ResultSet  primaryKeyRecordSet=null;
			String[] TABLE_TYPES = {"TABLE"};
			catalogs = md.getTables("testDB", "", "%", TABLE_TYPES);

			catalogs.beforeFirst();
			while (catalogs.next()) {
				
				String tableName = catalogs.getString(3);  //"TABLE_CATALOG" 	
				mapTable(tableName);
				// PK fields         
				primaryKeyRecordSet = md.getPrimaryKeys(null, null, tableName);

				//Map PKs
				 mapPrimaryKey( primaryKeyRecordSet, tableName);
				
				// FK Fields
				mapForeignKeys(tableName);

				// Normal fields
				mapAllKeys(tableName);

			}

			r2rmlGraph.setNsPrefix("rr",RR.getURI());
			r2rmlGraph.setNsPrefix("vocab",vocUri);
			r2rmlGraph.setNsPrefix("mapping",dbUri);
			r2rmlGraph.setNsPrefix("data",dataUri);
			// r2rmlGraph.write(System.out,"N-TRIPLE");
			//return r2rmlGraph; 
		} catch (UnsupportedEncodingException e) {
			log.error("Error:",e);
		}
		return r2rmlGraph;          
	}

	private String urlEncode(String tableName)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(tableName, "UTF-8").replaceAll("\\+", "%20");
	}
	
	String getParentTable(ArrayList<String> tableNamesList, String fkName){
		String tableName=null;
		for(int i=0; i<tableNamesList.size();i++) {
			tableName = tableNamesList.get(i);
			if(fkName.contains(tableName)){
				return tableName;
			}
		}
		return fkName;
	}
	
	*//**
	 * mapTable maps the rr:logicalTabel property and table name.
	 * @param tableName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapTable(String tableName) throws UnsupportedEncodingException{
		String tableNameEncoded="";
		tableNameEncoded = urlEncode(tableName);
		
		tripleMap =r2rmlGraph.createResource(dbUri + tableNameEncoded);
		
		Resource tabelNameResource=r2rmlGraph.createResource()
				.addProperty(RR.tableName , '\"' + tableName + '\"');
		
		tripleMap.addProperty(RR.logicalTable , tabelNameResource);	
	}
	
	*//**
	 * getPrimaryKeyCount tack DatabaseMetaData and a pacific table name as parameters and returns the number of primary keys in this table
	 * @param DatabaseMetaData
	 * @param tableName
	 * @return Primary Keys Count
	 * @author Sherif
	 * @throws SQLException 
	 *//*
	int getPrimaryKeyCount(String tableName) throws SQLException{
		java.sql.ResultSet  primaryKeyRecordSet=null;
		int rowcount = 0;
		primaryKeyRecordSet = md.getPrimaryKeys(null, null, tableName);
		if (primaryKeyRecordSet.last()) {
			rowcount = primaryKeyRecordSet.getRow();
			primaryKeyRecordSet.beforeFirst(); // not primaryKeyRecordSet.first() because the primaryKeyRecordSet.next() below will move on, missing the first element
		}
		return rowcount;
	}
	
	*//**
	 * mapPrimaryKey maps rr:subjectMap, rr:class and rr:template properties for Primary Key Table
	 * @param primaryKeyRecordSet
	 * @param pKcolumnName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapPrimaryKey(java.sql.ResultSet  primaryKeyRecordSet, String tableName ) throws UnsupportedEncodingException, SQLException{
		// Count PKs
		int primaryKeysCount = getPrimaryKeyCount(tableName);
		String tableNameEncoded = urlEncode(tableName);

		if (primaryKeysCount > 1) {			// case composite PKs
			mapCompositePrimaryKey(primaryKeyRecordSet, tableNameEncoded);
		}
		else if (primaryKeysCount == 1) {	// case 1 PK
			mapSinglePrimaryKey(primaryKeyRecordSet, tableNameEncoded);
		} 
		else{								// case No PK
			mapNoPrimaryKey(tableName);
		}
	}
	
	*//**
	 * mapCompositePrimaryKey maps rr:subjectMap, rr:class and rr:template properties for Composite Primary Key Table
	 * @param primaryKeyRecordSet
	 * @param pKcolumnName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapCompositePrimaryKey(java.sql.ResultSet  primaryKeyRecordSet, String tableNameEncoded ) throws UnsupportedEncodingException, SQLException{
		String compositePrimaryKeyString="";
		String parentTableNameEncoded="";
		String pKcolumnName ;

		while (primaryKeyRecordSet.next()) {
			String parentTableName=primaryKeyRecordSet.getString(4);
			parentTableNameEncoded = urlEncode(parentTableName);
			pKcolumnName  = primaryKeyRecordSet.getString("COLUMN_NAME");
//			String pKcolumnNameEncoded = urlEncode(pKcolumnName);
			compositePrimaryKeyString += parentTableNameEncoded + "={\"" + pKcolumnName + "\"}"; 
			compositePrimaryKeyString += primaryKeyRecordSet.isLast()?"":compPkSep;       
		}

		Property VocUriProperty 	= ResourceFactory.createProperty(vocUri + tableNameEncoded);

		Resource dataVocUriResource	= r2rmlGraph.createResource()
				.addProperty(RR.template , dataUri+ compositePrimaryKeyString)
				.addProperty(RR.Class,VocUriProperty);

		tripleMap.addProperty(RR.subjectMap , dataVocUriResource);
	}
	
	*//**
	 * mapSinglePrimaryKey maps rr:subjectMap, rr:class and rr:template properties for Single PrimaryKey Table
	 * @param primaryKeyRecordSet
	 * @param pKcolumnName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapSinglePrimaryKey(java.sql.ResultSet  primaryKeyRecordSet, String tableNameEncoded ) throws SQLException, UnsupportedEncodingException{	
		String pKcolumnName="" ;
		String pKcolumnNameEncoded="";

		primaryKeyRecordSet.first();
		pKcolumnName  = primaryKeyRecordSet.getString("COLUMN_NAME");
		pKcolumnNameEncoded = urlEncode(pKcolumnName);

		Property VocUriProperty 	= ResourceFactory.createProperty(vocUri + tableNameEncoded);

		Resource dataVocUriResource	= r2rmlGraph.createResource()
			.addProperty(RR.template , dataUri+ tableNameEncoded +'/'+ pKcolumnNameEncoded +"={\"" + pKcolumnName + "\"}")
			.addProperty(RR.Class ,VocUriProperty);

		tripleMap.addProperty(RR.subjectMap , dataVocUriResource);
	}
	
	*//**
	 * mapNoPrimaryKey maps rr:subjectMap, rr:class rr:BlankNode and rr:template properties for no PrimaryKey Table
	 * @param primaryKeyRecordSet
	 * @param pKcolumnName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapNoPrimaryKey(String tableName) throws SQLException, UnsupportedEncodingException{
		*//**
		 * If no PK then make a PK as a composition of all keys
		 *//*
		String compositePrimaryKeyString="";
		java.sql.ResultSet r;
		String tableNameEncoded="";
		r = md.getColumns("", "", tableName, "%");
		tableNameEncoded = urlEncode(tableName);
		
		while(r.next()){
			String columnName=r.getString(4);
//			String columnNameEncoded = urlEncode(columnName);
			compositePrimaryKeyString += tableNameEncoded + "={\"" + columnName + "\"}"; 
			compositePrimaryKeyString += r.isLast()?"":compPkSep;
		}

		Property VocUriProperty 	= ResourceFactory.createProperty(vocUri + tableNameEncoded);

		Resource dataVocUriBnodeResource=r2rmlGraph.createResource();
		dataVocUriBnodeResource
			.addProperty(RR.template, dataUri+ compositePrimaryKeyString )
			.addProperty(RR.Class,VocUriProperty)
			.addProperty(RR.termType, RR.BlankNode);

		tripleMap.addProperty(RR.subjectMap,dataVocUriBnodeResource);
	}
	
	*//**
	 * mapAllKeys maps rr:predicateObjectMap, rr:predicate rr:objectMap and rr:column properties for all keys of each Table
	 * @param primaryKeyRecordSet
	 * @param pKcolumnName
	 * @param tableNameEncoded
	 * @author Sherif
	 * @throws SQLException 
	 * @throws UnsupportedEncodingException 
	 *//*
	void mapAllKeys(String tableName) throws SQLException, UnsupportedEncodingException{

		java.sql.ResultSet allKeysRecordSet = md.getColumns("", "", tableName, "%");

		while (allKeysRecordSet.next()){
			String colName=allKeysRecordSet.getString(4);
			String colNameEncoded = urlEncode(colName);
			String tableNameEncoded = urlEncode(tableName);

			Property VocUriProperty				= ResourceFactory.createProperty(vocUri + tableNameEncoded + '#' + colNameEncoded);

			Resource columnNameResource=r2rmlGraph.createResource();
			columnNameResource.addProperty(RR.column,'\"' + colName + '\"');

			Resource VocUriColResource=r2rmlGraph.createResource();
			VocUriColResource
				.addProperty(RR.predicateMap , VocUriProperty)
				.addProperty(RR.objectMap , columnNameResource);

			tripleMap.addProperty(RR.predicateObjectMap, VocUriColResource);
		}
	}


	
	*//**
	 * mapForeignKeys maps rr:predicateObjectMap, rr:predicate rr:objectMap, rr:parentTriplesMap, rr:joinCondition, rr:child and rr:parent properties foreign keys of each Table
	 * @param tableName
	 * @author sherif
	 * @throws UnsupportedEncodingException 
	 * @throws SQLException 
	 *//*
	void mapForeignKeys(String tableName) throws UnsupportedEncodingException, SQLException{
		String tableNameEncoded = urlEncode(tableName);
		String compositFkString="";
	//	Schema dbSchema=Schema.create(dbConnction);

		Multimap<String, ForeignKey> foreinKeysMultimap = dbSchema.getForeignKeys();	
		Collection<ForeignKey> foreinKeysCollection= foreinKeysMultimap.get(tableName);

		// Parent = Source
		// Child  = Target

		for (ForeignKey fk : foreinKeysCollection){// if the mapping related to the current table do	

			Property dbUriProperty				= ResourceFactory.createProperty(dbUri + urlEncode(fk.getTarget().getTableName()));

			// get the composite Keys String
			compositFkString="";
			for(int compFkIndex=0;compFkIndex<fk.getSource().getColumnNames().size();compFkIndex++){
				String fkSubStringEncoded=urlEncode(fk.getSource().getColumnNames().get(compFkIndex));
				compositFkString += fkSubStringEncoded + ";";
			}
			compositFkString = compositFkString.substring(0, compositFkString.length()-1); // remove the last ";"
			Property vocUriProperty				= ResourceFactory.createProperty(vocUri + tableNameEncoded + '#' + compositFkString);
					
			Resource [] childParentResource=new Resource[fk.getSource().getColumnNames().size()] ;
			Resource parentTriplesMapjoinConditionResource=null;
			Resource predicateobjectMapResource=null;

			for(int childParentResourceIndex=0;childParentResourceIndex<fk.getSource().getColumnNames().size();childParentResourceIndex++){
				childParentResource[childParentResourceIndex] = r2rmlGraph.createResource()
						.addProperty(RR.child, '\"' + fk.getTarget().getColumnNames().get(childParentResourceIndex) + '\"')
						.addProperty(RR.parent, '\"' + fk.getSource().getColumnNames().get(childParentResourceIndex) + '\"');
			}

			parentTriplesMapjoinConditionResource =r2rmlGraph.createResource();
			parentTriplesMapjoinConditionResource.addProperty(RR.parentTriplesMap, dbUriProperty);
			for(int childParentResourceIndex=0;childParentResourceIndex<fk.getSource().getColumnNames().size();childParentResourceIndex++){
				parentTriplesMapjoinConditionResource.addProperty(RR.joinCondition, childParentResource[childParentResourceIndex]);
			}

			predicateobjectMapResource =r2rmlGraph.createResource()
					.addProperty(RR.predicate, vocUriProperty)
					.addProperty(RR.objectMap, parentTriplesMapjoinConditionResource);

			tripleMap.addProperty(RR.predicateObjectMap, predicateobjectMapResource); 

		}
	}
*/

}

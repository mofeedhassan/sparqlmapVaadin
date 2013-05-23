package org.aksw;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.sql.*;

import javax.swing.plaf.SplitPaneUI;

import org.aksw.TripleMap;

import java.io.File;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.pfunction.library.container;
import com.hp.hpl.jena.sparql.util.QueryUtils;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.util.FileManager;
//import com.ibm.icu.impl.USerializedSet;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
//import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
//import com.vaadin.data.util.sqlcontainer.RowItem;
//import com.vaadin.data.util.sqlcontainer.SQLContainer;
//import com.mysql.jdbc.Connection;
//import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
//import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
//import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
//import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
//import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.CheckBox;
//import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
//import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Button.ClickListener;
//import com.vaadin.ui.LoginForm;---
//import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.CheckBox;
//import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.data.util.filter.SimpleStringFilter;
/*import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
*/
/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{
    Model model = ModelFactory.createDefaultModel();
    //HashMap<String,Integer> tablesizes= new HashMap<String,Integer>();
    int currentTableSize;
	String strTableName;
	//parameters
	Table tblTriplesMapPredicatesDetailsParam,tblTriplesMapBasicDetailsParam;
    @Override
    protected void init(VaadinRequest request) 
    {
    	final HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		final TextField txtfilePath= new TextField("File Path: ");
		Button btnLoad = new Button("Load the file");
		
		layout.addComponent(txtfilePath);
		layout.addComponent(btnLoad);
		setContent(layout);
		
		btnLoad.addClickListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				String filePath=txtfilePath.getValue();
				if(!filePath.equals(""))
				{
					File f = new File(filePath);
					
					if(f.exists()) 
					{ 
						java.io.InputStream in = FileManager.get().open(filePath);
						if(model.size() > 0 )//exist old loaded values
							model.removeAll();
						model.read(in,"http://www.w3.org/ns/r2rml#", "TURTLE"); 
						Notification.show("Model successfully created with Triples = "+model.size());
						/*try
						{
						java.io.FileOutputStream out =new FileOutputStream("/home/mofeed/Programs/mortada.ttl");
						model.write(out,"TURTLE");

						}
						catch(Exception e) {Notification.show(e.getMessage());}*/
						createDataWindow();
					}
					else
						Notification.show("File not Exist");
					
				}
			}
		});
    }
    boolean editable=false;
   private void createDataWindow()
	{
	   //create the subwindow
		Window dataWindow = new Window("Modal window");	
		dataWindow.setModal(true);
		dataWindow.setSizeFull();
		//create a splitter to make the window in two areas
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		
		//create the tow layouts for both areas
		VerticalLayout lytTripleMapsPage= new VerticalLayout();
		VerticalLayout lytTripleMapsDetailsPage= new VerticalLayout();
		HorizontalLayout lytButtons= new HorizontalLayout();
		//set each area to its layout
		hsplit.setFirstComponent(lytTripleMapsPage);
		hsplit.setSecondComponent(lytTripleMapsDetailsPage);
		//set the splitter to the window
		dataWindow.setContent(hsplit);
		//Create a table in the first area
		final Table tblTriplesMaps = new Table("Source Properties");
		final Table tblTriplesMapBasicDetails = new Table("Basic Details");
		final Table tblTriplesMapPredicatesDetails = new Table("Predicates Details");
		tblTriplesMapPredicatesDetails.setImmediate(true);
		tblTriplesMapPredicatesDetailsParam=tblTriplesMapPredicatesDetails;
		tblTriplesMapBasicDetailsParam=tblTriplesMapBasicDetails;
		
		tblTriplesMaps.setSizeFull();
     	tblTriplesMapBasicDetails.setSizeFull();
     	tblTriplesMapPredicatesDetails.setSizeFull();

		tblTriplesMaps.setSelectable(true);
		tblTriplesMapBasicDetails.setSelectable(true);
		tblTriplesMapBasicDetails.setPageLength(1);
		tblTriplesMapPredicatesDetails.setSelectable(true);

		tblTriplesMaps.addContainerProperty("Table", String.class,  null);
		tblTriplesMapBasicDetails.addContainerProperty("Triple Map", String.class,  null);
		tblTriplesMapBasicDetails.addContainerProperty("Class", String.class,  null);
		tblTriplesMapBasicDetails.addContainerProperty("Template", String.class,  null);
		tblTriplesMapPredicatesDetails.addContainerProperty("Predicate", String.class,  null);
		tblTriplesMapPredicatesDetails.addContainerProperty("Column", String.class,  null);

		final Button btnEdit= new Button("Edit");
		Button btnAdd= new Button("Add");//mostly for predicate objects map
		Button btnDelete= new Button("Delete");//mostly for predicate objects map
		Button btnSave= new Button("Save");

		//Retrieve list of TriplesMap with all their information(subjectMap,Class, Template, Predicates, and Columns)
		final HashMap<String, TripleMap> triplesMapsList=getTriplesMapsDetailsList();
        
        //Fill table of 'Tables' on leftside
	    int id=1;
	    for (String tableName  : triplesMapsList.keySet()) 
        {
         	tblTriplesMaps.addItem(new Object[] {tableName},new Integer(id));
        	id++;
        }

        //filling predicates
        //when click on a table fill in tow tables of its data (basic data, predicates_columns)
        tblTriplesMaps.addItemClickListener(new ItemClickEvent.ItemClickListener() 
        {
            public void itemClick(ItemClickEvent event) 
            {
            	strTableName=tblTriplesMaps.getContainerProperty(event.getItemId(), event.getPropertyId()).toString();
            	
            	//Filling Basic data table
            	for (TripleMap triple : triplesMapsList.values()) 
            	{
					if(triple.getTblname().equals("\""+strTableName+"\""))
					{
						tblTriplesMapBasicDetails.removeAllItems();
		               	tblTriplesMapBasicDetails.addItem(new Object[] {triple.getSubject(),triple.getCls(),triple.getTmpl()},new Integer(1));
		               	
						break;
					}
				}
            	
            	//Filling Predicates  table
            	tblTriplesMapPredicatesDetails.removeAllItems();
            	int id=1;
            	HashMap<String,String> predicatesColumns=triplesMapsList.get(strTableName).predicatesColumns;//get the table's preddicates_columns
        	    
                for ( Map.Entry<String, String> predicateColumn : predicatesColumns.entrySet()) 
                {
                	tblTriplesMapPredicatesDetails.addItem(new Object[] {predicateColumn.getKey(),predicateColumn.getValue()},new Integer(id));
                	id++;
                }
                
            	/*HashMap<String,HashMap<String,String>> predicatesColumns=triplesMapsList.get(strTableName).predicatesColumns;//get the table's preddicates_columns
        	    for (Map.Entry<String, HashMap<String, String>> predicateColumn: predicatesColumns.entrySet()) 
        	    {
        	    	String predicate =predicateColumn.getKey(); //this is predicate
        	    	HashMap<String,String> t =predicateColumn.getValue();//get predicates items
        	    	String column= t.get("column");
        	    	tblTriplesMapPredicatesDetails.addItem(new Object[] {predicate,column},new Integer(id));
                	id++;
                }*/
                
               // tablesizes.put(strTableName, tblTriplesMapPredicatesDetails.size());
                currentTableSize=tblTriplesMapPredicatesDetails.size();
            }
        });
        
        btnEdit.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				editable=!editable;
				tblTriplesMaps.setEditable(editable);
				tblTriplesMapBasicDetails.setEditable(editable);
				tblTriplesMapPredicatesDetails.setEditable(editable);
				if(editable)
					btnEdit.setCaption("Unedit");
				else
					btnEdit.setCaption("Edit");

			}
		});
        btnAdd.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window addDataWindow = new Window("Add Window");	
				addDataWindow.setModal(true);
				addWindow(addDataWindow);
				addToTripleMap(addDataWindow);
				//currentTableSize++;
			}
		});
        btnDelete.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
			Object id=null;
			id =tblTriplesMapPredicatesDetailsParam.getValue();
			if(id!=null)
				//tblTriplesMapPredicatesDetailsParam.removeItem(id);
			tblTriplesMapPredicatesDetailsParam.getContainerDataSource().removeItem(id);
			//currentTableSize--;
			}
		});
        btnSave.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Object id= tblTriplesMapBasicDetailsParam.size();//as it is one item row only
				Item row1 = tblTriplesMapBasicDetailsParam.getItem(id);
		        String subjct= row1.getItemProperty("Triple Map").getValue().toString();
		        
		        String query = "DELETE WHERE { <"+subjct+"> ?p ?o }";
		        UpdateAction.parseExecute(query, model);
		        
		        String addStatmentQuery="";
		       /* java.io.OutputStream in =  new ("/home/mofeed/Desktop/temp.nt");
				model.read(in,"http://www.w3.org/ns/r2rml#", "TURTLE"); */
		        
				/*Property property = tblTriplesMapBasicDetailsParam.getContainerProperty(id, "1");
				Object value = (null != property) ? property.getValue() : null;
				Notification.show(value.toString());*/
				//Property property1 = tblTriplesMapBasicDetailsParam.get("Triple Map");
				
				//String d= property1.getValue().toString();
				/*Collection<Object> lstProperties= (Collection<Object>) tblTriplesMapBasicDetailsParam.getContainerPropertyIds();
            	String property=tblTriplesMapBasicDetailsParam.getContainerProperty(id, lstProperties.iterator().next().toString());
				String deleteQuery= "DELETE { <"+tblTriplesMapBasicDetailsParam.getP+"> ?p ?o.";*/
				
			}
		});
/*        ok.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
        */
        lytButtons.addComponent(btnEdit);
        lytButtons.addComponent(btnAdd);
        lytButtons.addComponent(btnDelete);
        lytButtons.addComponent(btnSave);
		
		lytTripleMapsPage.addComponent(tblTriplesMaps);
		lytTripleMapsDetailsPage.addComponent(tblTriplesMapBasicDetails);
		lytTripleMapsDetailsPage.addComponent(tblTriplesMapPredicatesDetails);
		lytTripleMapsDetailsPage.addComponent(lytButtons);
		

		
		addWindow(dataWindow);
	}
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private void collectTableItems(Table table)
   {
	   
   }
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private void writToFile()
   {
	   OutputStream out=null;
		try {
			out = new FileOutputStream("/home/mofeed/Desktop/temp.nt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(out != null)
			model.write(out, "TURTLE") ;	
		else
			Notification.show("Writing error");
   }
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private void removeTriple(String subject)
   {
	   String deletQuery="DELETE { <"+subject+"> ?p ?o. }";
	 /*  Query query = QueryFactory.create(deletQuery);
	   QueryExecution exec = QueryExecutionFactory.create(query, model); exec.
	   com.hp.hpl.jena.query.ResultSet resultSet = exec.execSelect();*/
	// remove statements where resource is subject
	   Resource r= model.createResource(subject);
	   UpdateAction.parseExecute(deletQuery, model);
	   
   }
   HashMap<String, String> addData=new HashMap<String, String>();
   private void addToTripleMap(Window add)
   {
	   final Window addwin=add;
	   final TextField txtPredicate= new TextField("Enter new Predicate: ");
	   final TextField txtColumn= new TextField("Enter new Column: ");
	   Button btnOK=new Button("OK");
	   VerticalLayout lytAddData= new VerticalLayout();
	   lytAddData.addComponent(txtPredicate);
	   lytAddData.addComponent(txtColumn);
	   lytAddData.addComponent(btnOK);
	   add.setContent(lytAddData);
	   btnOK.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String addedPredicate= txtPredicate.getValue();
				String addedColumn=txtColumn.getValue();
				addData.put("predicate",addedPredicate );
				addData.put("column",addedColumn );
				int newId = Integer.parseInt(tblTriplesMapPredicatesDetailsParam.lastItemId().toString());
				//Note using the size of table does not reflect the new id value in case you deleted from the middle rows
				newId++;
				tblTriplesMapPredicatesDetailsParam.addItem(new Object[] {addedPredicate,addedColumn},new Integer(newId));
				
				removeWindow(addwin);
			}
		});
	   
	   
   }
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private HashMap<String, TripleMap> getTriplesMapsDetailsList()
   {
	   //List<TripleMap> triplesMaps=null;
	/*   String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			   " SELECT ?s ?tblname ?tmpl ?cls WHERE  { {" +
			   "?s <http://www.w3.org/ns/r2rml#logicalTable> ?empty1 ." +
			   "?empty1  <http://www.w3.org/ns/r2rml#tableName> ?tblname ." +
			   "?s <http://www.w3.org/ns/r2rml#subjectMap> ?empty2 ." +
			   "?empty2 <http://www.w3.org/ns/r2rml#template> ?tmpl .}" +
			   "UNION {?empty2 <http://www.w3.org/ns/r2rml#class> ?cls .}}";*/
	   String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			   " SELECT ?s ?tblname ?tmpl ?cls WHERE  { " +
			   " ?s <http://www.w3.org/ns/r2rml#logicalTable> ?empty1 ." +
			   " ?empty1  <http://www.w3.org/ns/r2rml#tableName> ?tblname ." +
			   " ?s <http://www.w3.org/ns/r2rml#subjectMap> ?empty2 ." +
			   " ?empty2 <http://www.w3.org/ns/r2rml#template> ?tmpl ." +
			   " ?empty2 <http://www.w3.org/ns/r2rml#class> ?cls .}";
	   Query query = QueryFactory.create(queryString);
	   QueryExecution exec = QueryExecutionFactory.create(query, model);
	   com.hp.hpl.jena.query.ResultSet resultSet = exec.execSelect();
	   HashMap<String, TripleMap> triples=new HashMap<String, TripleMap>();
	   String  tabelname,templat,clas;
	   Resource subject; 
	   TripleMap triple;
	   while(resultSet.hasNext())
	   {
		   QuerySolution solution = resultSet.next();
		   //collecting information
		   subject=solution.getResource("?s"); 
		   tabelname=solution.getLiteral("?tblname").toString(); 
		   templat=solution.get("?tmpl").toString();//=solution.getLiteral("?tmpl").toString();
		   if(solution.contains("?cls"))//as some test cases don't mention class predicate for the table
		   {
			   clas=solution.get("?cls").toString();
			   triple=new TripleMap(subject.toString(),tabelname, templat, clas);
		   }
		   else
			   triple=new TripleMap(subject.toString(),tabelname, templat, "");
		   String tableNameKey=tabelname.substring(1, tabelname.length()-1);
		   triple.predicatesColumns=PredicateExtractor(tableNameKey);
		   Notification.show(String.valueOf(triple.predicatesColumns.size()));
		   triples.put(tableNameKey,triple);
	   }

	   return triples;
   }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private String getTripleClass()
   {
	   //List<TripleMap> triplesMaps=null;
	   String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
			   " SELECT ?cls WHERE  { " +
			   "?s <http://www.w3.org/ns/r2rml#subjectMap> ?empty2 ." +
			   "?empty2 <http://www.w3.org/ns/r2rml#class> ?cls .}";
	   Query query = QueryFactory.create(queryString);
	   QueryExecution exec = QueryExecutionFactory.create(query, model);
	   com.hp.hpl.jena.query.ResultSet resultSet = exec.execSelect();
	    String  clas="";
	   while(resultSet.hasNext())
	   {
		   QuerySolution solution = resultSet.next();

		   clas=solution.get("?cls").toString();
	   }
	   return clas;
   }   
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private  HashMap<String,String> PredicateExtractor(String tablename)
    {
  		String queryString = 
  				" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
    			" SELECT ?predicate ?column WHERE  { " +
    			" ?s <http://www.w3.org/ns/r2rml#logicalTable> ?empty1 ." +
    			" ?empty1  <http://www.w3.org/ns/r2rml#tableName> \"\\\""+tablename+"\\\"\" ." +
    			" ?s <http://www.w3.org/ns/r2rml#predicateObjectMap> ?empty2 ." +
    			" ?empty2 <http://www.w3.org/ns/r2rml#objectMap> ?empty3 ." +
    			" ?empty2 <http://www.w3.org/ns/r2rml#predicate> ?predicate ." +
    			" ?empty3 <http://www.w3.org/ns/r2rml#column> ?column ." +
    			"}";
  		
		Query query = QueryFactory.create(queryString);
        QueryExecution exec = QueryExecutionFactory.create(query, model);
        com.hp.hpl.jena.query.ResultSet resultSet = exec.execSelect();
        String  predicate="",predicateItem="";
        HashMap<String,String> predicateItems=new HashMap<String,String>();
        while(resultSet.hasNext())
        {
            QuerySolution solution = resultSet.next();
            predicate=solution.get("?predicate").toString();
            predicateItem= solution.get("?column").toString();

            predicateItems.put(predicate, predicateItem);
        }

    	return predicateItems;	
    }

}
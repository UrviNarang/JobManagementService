package jobManagementService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jobs.Job;
import jobs.State;

public class JobScheduler {

	static ArrayList<Object> backedUpParams;
	static ArrayList<Object> params;
	static String CONFIG_XML_FILE_NAME="JobSchedulerConfig.xml";
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ParseException {
		// TODO Auto-generated method stub.
		
		JobScheduler jobScheduler=new JobScheduler();
		createSystemState();
		ArrayList<Job> jobs=readAndScheduleJobsFromXML();
		if(jobs==null ||jobs.size()==0)
		{
			System.out.println("Exiting since no job schedule found in Config");
			return;
		}
		jobScheduler.createJobSchedule(jobs);
		System.out.println("\n**************Job states before Execution ************** ");
		for(Job job:jobs)
		{
			job.printJobState();
	
		}
		System.out.println("******************************************************** ");
		for(Job job:jobs)
		{
			System.out.println("Started executing job:"+ job.getJobName() );
			System.out.println("Initial state:"+job.getState());
			
			jobScheduler.executeJob(job);
			
			System.out.println("Ended executing job:"+ job.getJobName() );
			System.out.println("Final state:"+job.getState());
		
			printSystemState();
		}
		System.out.println("\n**************Job states after Execution ************** ");
		for(Job job:jobs)
		{
			job.printJobState();
	
		}
		System.out.println("******************************************************** ");
		
	}
	
	
	/**
	 * This method is used to specify the state of the system.
	 * Any more params if needed can be specified here.
	 * Every job will have access to this system state and can update it.
	 */
	public static void createSystemState()
	{
		params=new ArrayList<>();
		params.add(new Date());
		params.add("State:000");
	}
	
	
	/**
	 * This method reads the config xml and instantiates the jobs and returns back the jobs to the caller.
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	public static ArrayList<Job> readAndScheduleJobsFromXML() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ParseException
	{
		ArrayList<Job> jobs=new ArrayList<Job>();
		
		//setting up parser to read config
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File( CONFIG_XML_FILE_NAME ));
		//Get all Jobs
		try{
			NodeList jobList = document.getElementsByTagName("job");
			if(jobList.getLength()<1)
			{
				System.out.println("Tag job not found in xml");
				return null;
			}
			for (int i = 0; i < jobList.getLength(); i++)
			{
			 Node jobNode = jobList.item(i);
			 System.out.println("");    //Just a separator
			 if (jobNode.getNodeType() == Node.ELEMENT_NODE)
			 {
				 //reading from xml
			    Element jobElement = (Element) jobNode;
			    String jobname=jobElement.getElementsByTagName("jobname").item(0).getTextContent();
			   
			    String priority=jobElement.getElementsByTagName("priority").item(0)==null?"":jobElement.getElementsByTagName("priority").item(0).getTextContent();
			    String schedule=jobElement.getElementsByTagName("schedule").item(0).getTextContent();
			    String execImmediate=jobElement.getElementsByTagName("execImmediate").item(0).getTextContent();
			   
			    
			    // instantiating the class for job with params
			    Class classdef=Class.forName("jobs."+jobname);
			    Class[] type = { String.class,String.class };
			    Constructor cons=classdef.getConstructor(type);
			    Object objs[]={jobname,priority};
			    Job job=(Job)cons.newInstance(objs);
			    
			    
			    scheduleTheJob(job,schedule);
			    
			    if(execImmediate.equalsIgnoreCase("YES"))
			    {
			    	job.setExecImmediate(true);
			    }
			    else
			    {
			    	job.setExecImmediate(false);
			    }
			    
			    jobs.add(job);

			  
			 }
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in Configuration: "+e);
			e.printStackTrace();
		}
		
		
		return jobs;
	}
	
	/**
	 * This method can be customized to schedule the job based on the input expected in the xml.
	 * cron string can be used to schedule jobs.
	 * Currently this method handles a schedule in 24 hr format for the current date
	 * eg. 14:25
	 * @param job
	 * @param schedule
	 * @throws ParseException 
	 */
	private static void scheduleTheJob(Job job, String schedule) throws ParseException {
		// TODO Auto-generated method stub
		if(schedule.contains(":") && schedule.length()==5)
		{
			String scheduleArr[]=schedule.split(":");
			int hours=Integer.parseInt(scheduleArr[0]);
			int mins=Integer.parseInt(scheduleArr[1]);
			if(hours<24 && mins<60)
			{
				GregorianCalendar cal=(GregorianCalendar) GregorianCalendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, hours);
				cal.set(Calendar.MINUTE, mins);
				cal.set(Calendar.MILLISECOND, 0);
				Date date= cal.getTime();
				
				//System.out.println("schedule set to "+date);
				job.setSchedule(date);
			}
		}
		
	}
	private void createJobSchedule(ArrayList<Job> jobs) {
		// TODO Auto-generated method stub
		
		/*for(int i=0; i< jobs.size();i++)
		{
			jobs.get(i).printJobState();
			
		}*/
		Collections.sort(jobs);
		for(int i=0; i< jobs.size();i++)
		{
			/*jobs.get(i).printJobState();*/
			jobs.get(i).setState(State.QUEUED);
		}
		
	}
	public static void printSystemState()
	{
		int i=0;
		System.out.println("********************************");
		
		System.out.println("System State:");
		while(i<params.size())
		{
			System.out.println(params.get(i++));
		}
		System.out.println("********************************");
		
	}
	public void executeJob(Job job)
	{
		
		try
		{
			job.setState(State.RUNNING);
			job.printJobState();
		
			backupParams(params);
			
			job.executeJob(params);
			job.setState(State.SUCCESS);
		}
		catch(Exception e)
		{
			params=getBackedUpParams();
			job.setState(State.FAILED);
		}
		
	}
	private ArrayList getBackedUpParams() {
		
		// TODO Auto-generated method stub
		return backedUpParams;
	}
	private void backupParams(ArrayList params) {
		// TODO Auto-generated method stub
		backedUpParams=new ArrayList<>();
		int i=0;
		while(i<params.size())
		{
			backedUpParams.add(params.get(i++));
		}
	}
}
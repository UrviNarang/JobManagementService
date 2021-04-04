package jobs;
import java.util.ArrayList;
import java.util.Date;

enum Priority
{
	LOW,MEDIUM,HIGH
}

public class Job implements Comparable<Job>{
	
	private String jobName;
	private Priority priority;
	private State state;
	private Date schedule;
	private boolean execImmediate;
	
	public Job(String jobName)
	{
		this.jobName=jobName;
		priority=Priority.LOW;
		state=State.BLANK;
	}
	
	public Job(String jobName,String priority)
	{
		this.jobName=jobName;
		//this.priority=	Priority.valueOf(priority);
		
		if(priority.equalsIgnoreCase("LOW"))
			this.priority=Priority.LOW;
		else if(priority.equalsIgnoreCase("MEDIUM"))
			this.priority=Priority.MEDIUM;
		else if(priority.equalsIgnoreCase("HIGH"))
			this.priority=Priority.HIGH;
		else
			this.priority=Priority.LOW;
		state=State.BLANK;
	}
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public Date getSchedule() {
		return schedule;
	}

	public void setSchedule(Date schedule) {
		this.schedule = schedule;
	}

	public boolean isExecImmediate() {
		return execImmediate;
	}

	public void setExecImmediate(boolean execImmediate) {
		this.execImmediate = execImmediate;
	}
	
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	
	public void executeJob(ArrayList<Object> params) throws Exception
	{
		System.out.println("Objects:");
		int i=0;
		while(i<params.size())
		{
			Object obj=params.get(i);
			System.out.println("Obj "+(i+1)+":"+obj);
			if(obj instanceof Date)
			{
				obj =new Date();
				params.set(i, obj);
			}
			else if(obj instanceof String)
			{
				String str=(String )obj;
				str=str+"\n"+this.jobName+" executed";
				params.set(i, str);
			}
				
			i++;
		}
		throw new Exception();
//		System.out.println(jobName + " is executed");
	}
	public void printJobState()
	{
		System.out.println("State: "+state+" for Job:"+this.jobName);
	}

	@Override
	public int compareTo(Job otherJob) {
		// TODO Auto-generated method stub
		if(this.isExecImmediate())
		{
			if(otherJob.isExecImmediate())
			{
				//check priorities
				if(this.getPriority().compareTo(otherJob.getPriority())<=0)
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		else
		{
			if(otherJob.isExecImmediate())
			{
				return 1;
			}
			else
			{
				// check schedules
				int comparisonRes=this.getSchedule().compareTo(otherJob.getSchedule());
				
				if(comparisonRes<0) // date is earlier to date of other job
				{
					return -1;
				}
				else if(comparisonRes==0)
				{
					//check priorities
					if(this.getPriority().compareTo(otherJob.getPriority())<=0)
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					return 1;
				}
				
			}
		}
		
	}
	
	
}

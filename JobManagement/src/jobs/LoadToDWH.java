package jobs;
import java.util.ArrayList;

public class LoadToDWH extends Job{

	public LoadToDWH(String jobName) {
		super(jobName);
		// TODO Auto-generated constructor stub
	}
	
	public LoadToDWH(String jobName,String priority) {
		super(jobName,priority);
		// TODO Auto-generated constructor stub
	}
	
	public void executeJob(ArrayList params) throws Exception
	{
		System.out.println("In executeJob of LoadtoDWH");
		String str=(String)params.get(1);
		str=str+" State changed by "+getJobName();
		params.set(1, str);
		//throw new Exception();
	}

}

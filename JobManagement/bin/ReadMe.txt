Job Management Service


Working:
	Job Management service reads the JobSchedulerConfig xml and instantiates the jobs.
	It then creates a job schedule as per below preferences:
		-> Immediate Execution ( highest preference)
		-> Scheduled time
		-> Priority
		
		-> If multiple jobs are to be executed immediately then preference is given to one with higher priority.
		-> If multiple jobs are scheduled at same time then again the one with higher priority is preferred.
	Each job makes changes in the System state which is rolled back if the job fails.

Code Description
	Methods are accompanied with comments and have tried to use method names that are self explanatory.
Configurations
	1. Initial System State is specified by just a Simple Date and a String. Anything needed can be specified within the method
	"createSystemState"
	2. The Job Schedules are specified in JobSchedulerConfig.xml. 
	3. The jobname specified in the xml is assumed to be the class name for the job.
	4. Specifying priority in xml for a job is optional. By default, low priority is assigned.
	
Assumptions
	1. Each job is scheduled by specifying a time in 24-hr format and the current date is assumed for execution of all the jobs.
	2. Each job just appends a string to the current state of system which will be rolled back if job fails ( Exception is thrown purposely to show the failure)
	3. Schedule is created just once for each execution of scheduler unlike a continuous one when jobs are scheduled to run repeatedly and specified using Cron strings.
	 

Enhancements
	1. Cron Strings can be used for scheduling jobs.
	2. The rollback of system state can be shown using a database and rollbacks in case of job failure.

Execution:
	1. Download the project and import in IDE (Eclipse)
	2. Update the JobSchedulerConfig.xml
	3. Execute JobScheduler.java
	4. Output will be written to console
	
Time Taken
	Took a day to accomplish the task.



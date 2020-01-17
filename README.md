
## Technical decisions:
- Dependencies
  * json-simple-1.1: Parse String to JSONObjects easily
  * GZIPInputStream: Read .gz file
  
- Java
  * OpenJDK 11
  
- IDE
  * Eclipse EE Version: 2019-12 (4.14.0) - Build id: 20191212-1212
 
- Maven
  * Apache Maven 3.6.3
 
 ## How to run:
   ``` java -jar quodai/app.jar [start Time] [end Time]``` 
   
  example if you start in quod-ai-challenge-thaile.cl-gmail.com directory
   
   ``` java -jar quodai/app.jar 2019-08-01T00:00:00Z 2019-09-01T00:00:00Z```
   
   or in quod-ai folder
   
   ``` java -jar app.jar 2019-08-01T00:00:00Z 2019-09-01T00:00:00Z```
   
 ## Improve in future:
  + Make it simpler
  + More feature metrics
  + Optimize for faster output
  
 ## Concept of process:
  Run app to each line in .gz file which contain information about the event action on github, then parse string to JSONObject. Following each event to next processed :
  - PushEvent: has actor's number commits
  - PullRequestEvent: has actor's info and gets the type of action of this event, and will consider 2 whether "opened" or "merged"
  - IssueEvent: has actor's info and gets the type of this event's action, and will consider 2 whether "opened" or "closed"
  - Multi thread for procesed JSON and update sequence
  - Display report absolute path  
  ```
                   Number of commits        Number of contributors       Number Commit per Day
  Health score = ---------------------- + --------------------------  + -----------------------
                 Max number of commits    Max number of contributors      Max commits per day
  ```                 
## References:
   - http://www.gharchive.org/
   - https://developer.github.com/v3/activity/events/types/
   - https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-source-and-target.html

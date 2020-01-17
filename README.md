
## Technical decisions:
- Dependencies
  * json-simple-1.1: Parse String to JSONObjects easily
  * GZIPInputStream: Read .gz file
 
 ## How to run:
   ``` java -jar app.jar [start Time] [end Time]``` 
   
  example
   
   ``` java -jar app.jar 2019-08-01T00:00:00Z 2019-09-01T00:00:00Z```
   
 ## Improve in future:
  + Make it simpler
  + Add more feature metric
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
## Reference:
   - http://www.gharchive.org/
   - https://developer.github.com/v3/activity/events/types/

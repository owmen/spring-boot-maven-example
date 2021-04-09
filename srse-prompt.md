Messages4You Inc. is a company that was founded on making the best instant-messenger platform for internet users. They successfully
created a messaging platform that is faster and delivers higher quality than their competitors. They've continued to grow their user base,
and now they need your help to resolve some user complaints they've received.  Messages4You is used widely across North America, but they have aspirations to be the best world-wide.
They can't do that with a flaky application and home-grown servers.

The company has several web services, but they need your help on improving one of the core services.

## This Application's Purpose:
* Get the most recent message for a user
* Get all messages for a user
* Search a user's messages for given text
* Create a new message for a user
* Edit a message

## Call To Action:
This Message web service is a core application of the Message4You product. It needs to be available 24/7. 
Messages4You Inc. needs your help to solve the immediate problems below, as well as recommendations to improve the infrastructure and maintenance of the application.

### Immediate Problems
They need your help to write cleaner code and resolve specific user issues. Please, do not break the existing api contract. All changes must be backwards compatible to not affect consumers. 
The solution must also include automated tests.

### Specific Issues:
* Current User Complaints:
    * Searches are slow and inconsistent
    * Timestamps are incorrect
    * Editing a message does not work
* Other observations:
    * IT Support has trouble debugging production problems
    * Unreliable network causes frequent failed requests to User Web Service

### Infrastructure
Messages4You is headquartered in Chicago, IL.
This application runs on an Apache Tomcat Servlet container hosted on a Windows server located in the basement. The source code is compiled and packaged as a .war file.
Changes are deployed manually, one instance at a time. There are currently two instances of the application running.
Sometimes, a third instance is set up because the other two stop accepting additional HTTP connections. 

The company needs your recommendations on how they can deliver new features and bug fixes faster to their users as well as reduce performance degradation.
Their user base continues to grow, so the company needs a solution that will support them over time.
Cost is not an issue, but the solution should meet the requirements without unneeded expense.
The solution should include changes outside this code base.

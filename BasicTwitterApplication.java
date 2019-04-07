package basictwitterapplication;

import java.util.List;
import java.util.Scanner;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.IDs;
import twitter4j.User;


public class BasicTwitterApplication 
{
  public static void main(String[] args) throws TwitterException
	{      
	     /* ConfigurationBuilder class is used to configure Twitter4J programatically*/
		ConfigurationBuilder cb = new ConfigurationBuilder();
		
		   cb.setDebugEnabled(true)
				.setOAuthConsumerKey("") //insert the consumer key
				.setOAuthConsumerSecret("") //insert the consumer secret
				.setOAuthAccessToken("") //insert the access token
				.setOAuthAccessTokenSecret(""); //insert the access token secret here.
			
		   TwitterFactory tf = new TwitterFactory(cb.build());
		   Twitter twitter = tf.getInstance();
		
			
	        Scanner scan = new Scanner(System.in);
	        
	        /*No.1  Display the Twitter Timeline of the user*/
	        
	        System.out.println("Type 1 for current user's home timeline and 2 for other user's Timeline :");
	        /*The other user can be any valid twitter account */
	        int tell = scan.nextInt();
	                
			if (tell==1)
	                {
	                    
	                    List<Status> status = twitter.getHomeTimeline(); 
	                    for (Status st : status)
	                    {
	                    		System.out.println(st.getUser().getName() + "--------" +st.getId() + "--------" + st.getText());
				
	                    }
	                    System.out.println("\n\n\n");
	                }
			/*Twitter.get****Timeline() returns a List of latest tweets from user's home timeline.*/
			if (tell==2)
	                {
	                    System.out.println("Enter the twitter id of the user (without @) : ");
	                    String user_id = scan.next();
	                    List<Status> status = twitter.getUserTimeline(user_id); 
	                    for (Status st : status)
	                    {
	                    		System.out.println(st.getUser().getName() + "--------" +st.getId() + "--------" + st.getText());
	                    }
	                    System.out.println("\n\n");
	                }
			
		/*No.2 Get the Trends on Twitter based on user input WOEID (Where On Earth Identifier)*/

		System.out.println("Enter a valid WOEID to get the trending hashtags in that area : ");
		int woeid = scan.nextInt();
		Trends trends = twitter.getPlaceTrends(woeid);
	        int count = 0;
	        for (Trend trend : trends.getTrends()) 
	        {
	            if (count < 10) 
	            {
	                System.out.println(trend.getName() + "----" + trend.getURL());
	                count++;
	            }
	        }
	       
			
		/*No.3 Post and Delete Status */
	        
		System.out.println("\n\n\nEnter the tweet you like to post as status : ");
		String tweetstatus = scan.next();
	        /*twitter.updateStatus() method is used to post a tweet*/
		twitter.updateStatus(tweetstatus);	
		System.out.println("---Status posted successfully!---");

	        int status_count=0,del_count;
	        System.out.println("Enter 1 to delete the recently  posted status) : ");
	        del_count = scan.nextInt();
	                
	        List<Status> status = twitter.getUserTimeline();
		for (Status st : status)
		{	
			status_count++;
			if(status_count==del_count)
	                {
	                    twitter.destroyStatus(st.getId());
	                }
		}
		System.out.println("---Status deleted successfully!---");
			
		/*No.4 Search tweets using Keywords*/
			
		System.out.println("\n\n\nEnter keywords to search :\n(Include # to search for hashtag keywords)\n");
		String querysearch = scan.next();
	  	/*Search for Tweets using Query class and Twitter.search(twitter4j.Query) method*/
	        Query query = new Query(querysearch);
	        QueryResult result;
	        do 
	        {
	        	result = twitter.search(query);
	         	List<Status> tweets = result.getTweets();
	            	for (Status tweet : tweets) 
	            	{
	                	System.out.println(tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n\n"); 
	            	}
	         } while ((query = result.nextQuery()) != null);
	         System.out.println("---Search complete!---");
	       
			/*No.5 Send personal messages to the follower*/
			
		System.out.println("\nEnter any text message that you wish to send : ");
		String directMessage= scan.next();
		System.out.println("\nType in the twitter handle name of the follower to whom you wish to send the message (without @ symbol): ");
		String twitterName = scan.next();
		/*Send and receive direct messages via Twitter.sendDirectMessage() /Twitter.getDirectMessages()*/	
		twitter.sendDirectMessage(twitterName, directMessage);
		System.out.println("---Message Sent Successfully!---"); 
			
		/*No.6 Get Friends and Followers of any public user.*/
			
		System.out.println("\n\n\nEnter the twitter id of the user to display his/her followers and following list : ");
		String username = scan.next();
			
	        long cursor = -1;
	        IDs ids;
	        System.out.println("\n\n\nListing followers's ids : ");
	        do 
	        {
	             ids = twitter.getFollowersIDs(username,cursor);
	             for (long id : ids.getIDs()) 
	             {
	            	 	System.out.print(id);
	                User user = twitter.showUser(id);
	                System.out.println(" " + user.getName());
	             }
	         } while ((cursor = ids.getNextCursor()) != 0);
	                
	             
	         long cursor2 = -1;
	         IDs ids2;
	         System.out.println("\n\n\nList of your friends's ids (following) : ");
	         do 
	         {
	              ids2 = twitter.getFriendsIDs(username,cursor2);
	              for (long id : ids2.getIDs())
	              {
	                  System.out.print(id);
	                  User user = twitter.showUser(id);
	                  System.out.println(" " + user.getName());
	              }
	         } while ((cursor2 = ids2.getNextCursor()) != 0);
	     
	          scan.close(); 
	}

}

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;


/**
 * @auth lsg
 * @version 1.0.0
 */
public class MongoUtils {
	public static Mongo getMongo() throws UnknownHostException{
		return new Mongo("mongo.test.server", 20011);
	}
	
	public static void auth(DB db){
		if(!db.isAuthenticated()){
			boolean b = db.authenticate("test", "test".toCharArray());
			System.out.println(b);
		}
	}
	
	public static void addUser(DB db){
			db.addUser("test", "test".toCharArray());
	}
}

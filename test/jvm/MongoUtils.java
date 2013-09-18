import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.fest.assertions.api.filter.Filters;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
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
	
	public static void main(String[] args) throws IOException {
		Map<String, Object> o = Maps.newConcurrentMap();
		List<String> s = ImmutableList.of("1");
		o = ImmutableMap.of();
		Files.readLines(new File("/export/one.txt"), Charsets.UTF_8);
		
		int compare = Ints.compare(1, 2); 
		byte[] data = Ints.toByteArray(1992);
		Ints.fromByteArray(data);
		
		CharMatcher.DIGIT.retainFrom("some text 89983 and more");
		
		int[] numbers = { 1, 2, 3, 4, 5 };  
		String numbersAsString = Joiner.on(";").join(Ints.asList(numbers)); 
		Ints.join(";", numbers);  
		
		Splitter.on(",").omitEmptyStrings().trimResults().split("sdf,sdf,,,sdf");  
		
		int count = 1; //Commons 也有
		Preconditions.checkArgument(count > 0, "must be positive: %s", count);
		Preconditions.checkNotNull("one");
		
		//Map<K, List<V>>这样的
		Multimap<Integer, String> multimap = ArrayListMultimap.create();
		multimap.get(1);
		
		Throwables.propagate(new Exception());
		
		Iterables.filter(Ints.asList(numbers), new Predicate<Integer>() {
			@Override
			public boolean apply(Integer input) {
				return (input % 2) == 0;
			}
		});
		
		
	}
}

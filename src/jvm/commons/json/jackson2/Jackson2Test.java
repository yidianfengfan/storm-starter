package commons.json.jackson2;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.googlecode.jtype.TypeUtils;
import com.googlecode.jtype.Types;
/**
 * 
JsonIdentityInfo
JsonTypeInfo

StdValueInstantiator
JacksonPolymorphicDeserialization
check="false" lazy="true">

mvn release:prepare -DignoreSnapshots=true  -DdryRun=true

 * @auth lsg
 * @version 1.0.0
 */
public class Jackson2Test {

	@Test
	public void t() throws IOException {
		Type type = new TypeReference<Map<String, List<Integer>>>(){}.getType();
		String typeStr = type.toString();
		System.out.println(typeStr);
		Type t = Types.valueOf(typeStr);
		
		User value = new User();
		ObjectMapper objectMapper1 = new ObjectMapper();
		objectMapper1.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper1.registerModule(new JodaModule());
		//objectMapper1.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		
		String str = objectMapper1.writeValueAsString(value);
		System.out.println(str);
		
		objectMapper1.getFactory();
		JsonNode  jsonNode = objectMapper1.readTree(str);
		String className = jsonNode.get("@class").asText();
		System.out.println(className);
		
		User tt = (User)(objectMapper1.reader(Object.class).readValue(str));
		System.out.println(tt);

		// 并不是标准的， 还是用snakeYaml吧
		YAMLFactory factory = new YAMLFactory();
		ObjectMapper objectMapper2 = new ObjectMapper(factory);
		objectMapper2.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper2.registerModule(new JodaModule());
		str = objectMapper2.writeValueAsString(value);
		System.out.println(str);

	}

	//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
	public static class User {
		private String name = "name";
		private int age = 29;
		private Date created = new Date();
		private float money = 192.0123f;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}

		public float getMoney() {
			return money;
		}

		public void setMoney(float money) {
			this.money = money;
		}

		@Override
		public String toString() {
			return "User [age=" + age + ", created=" + created + ", money=" + money + ", name=" + name + "]";
		}
	}

	@Test
	public void shouldDeserializeDiscriminator() throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		Dog dog = mapper.reader(Dog.class).readValue("{ \"name\":\"hunter\", \"discriminator\":\"B\"}");
		System.out.println(dog);
	}
	

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "discriminator")
	@JsonSubTypes( { @JsonSubTypes.Type(value = Beagle.class, name = "B"), @JsonSubTypes.Type(value = Loulou.class, name = "L") })
	private static abstract class Dog {
		@JsonProperty("name")
		String name;
		@JsonProperty("discriminator")
		String discriminator;
	}

	private static class Beagle extends Dog {
	}

	private static class Loulou extends Dog {
	}
}

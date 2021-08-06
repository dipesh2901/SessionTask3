package com.neosoft.java8;


//Goal of Serializing an object into a JSON string.

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface JsonField{
	public String value() default "";
}


class Car{
	@JsonField("manufacturer")
	private final String make;
	@JsonField
	private final String model;
	@JsonField
	private final String year;
	
	public Car(String make, String model, String year) {		
		this.make = make;
		this.model = model;
		this.year = year;
	}	
	
	
	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public String getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return make+" "+model+" "+year;
	}
}

@SuppressWarnings("serial")
class JsonSerializerException extends Exception{
	public JsonSerializerException(String message) {
		super(message);
	}
}

class JsonSerializer{
	
	public String serialize(Object object) throws JsonSerializerException{
		Class<?> objectClass = (object).getClass(); //car
		//System.out.println(objectClass.getSimpleName());
		Map<String,String> jsonElement = new HashMap<>();
		
		try {			
			for(Field field: objectClass.getDeclaredFields()) {
				
				field.setAccessible(true);
				
				if(field.isAnnotationPresent(JsonField.class)) {
					jsonElement.put(getSerializedKey(field), (String) field.get(object));
				}
			}
			
			System.out.println(toJsonString(jsonElement,objectClass));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return toJsonString(jsonElement,objectClass);
	}

	private String toJsonString(Map<String, String> jsonMap,Class<?> objectClass) {
		
        String elementsString = jsonMap.entrySet()
                .stream()
                .map(entry -> "<"  +  entry.getKey() + ">" + entry.getValue() +"</"+entry.getKey()+">")
                .collect(Collectors.joining(">"));
        
        return "<"+objectClass.getSimpleName()+">\n"+ elementsString + "\n<"+objectClass.getSimpleName()+">";
    }


	private static String getSerializedKey(Field field) {
        String annotationValue = field.getAnnotation(JsonField.class).value();
       
        if (annotationValue.isEmpty()) {
            return field.getName();
        }
        else {
            return annotationValue;
        }
    }
}

public class Program22 {

	public static void main(String[] args) throws JsonSerializerException {
		Car car = new Car("Ford", "F150", "2018");
		
		JsonSerializer js = new JsonSerializer();
		js.serialize(car)
;
	}
}
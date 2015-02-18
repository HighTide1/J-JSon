package com.hightide.jjson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *Copyright (C) {2015}  {Jered Tupik}
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 * A JSon Object, as defined by the ECMA-404 Standard for JSon
 * data interchanging, is an unordered collection of name/values pairs.
 * The external object is a string wrapped in curly braces({}), 
 * with each name/value pair being separated by a colon(:), and difference 
 * name/value pairs separated by a comma(,). An example of this format is as follows:
 * 
 * <pre>
 * {"name":"CodeMonkey", "job":"Programmer", "Manager":"Rob"}
 * </pre>
 * 
 * <p>The previous example of a JSON String would create an object with a the 3 properties
 * listed, and would assign each value to its respective property. The JSon String itself,
 * however, does not need to follow the whitespace format given above, and could be seperated
 * by any number of lines or spaces. As long as it conforms to the name/value pair rules and object
 * structure, a valid JSon Object will be produced</p>
 * 
 * <p>A JSonObject stores its name/value pairs in a {@codejava.util.Map}, allowing any derived
 * subclasses to be passed to the JSonObject. Additional constructors are provided for
 * {@code java.lang.String} or HTML-based JSonObjects.</p>
 * 
 * <p>A JSonObject class provides {@code getName}, {@code getNameOccurences}, {@code getDefaultName},
 * {@code getValue}, {@code getValueOccurences}, and the {@code getDefaultValue} method for returning
 * name/value pair information, and provides {@code setName}, {@code setValue} and {@code putPair}
 * methods for adding/setting a name/value pair. Any class which can be read by the {@code JSonConverter}
 * can be converted to a JSonObject. If any issues arise with this conversion, please contact me</p>
 * 
 * <p>Some of the JSon-Java library was used as an inspiration/base for this project, most noticably the
 * structure/commenting of this file. If this infringes upon any rights or licenses granted to the Java-JSon
 * library, please contact me to resolve this issue. All rights and references to JSon-Java are purely
 * informational in nature, and as such do not mean to infringe upon any previously mentioned grants.</p>
 * 
 * @author Jered Tupik
 * @version 1.0 2015-2-15
 */
public class JSonObject implements Comparable<JSonObject>, Serializable{
	
	/**
	 * Serializable ID for JSonObject
	 */
	private static final long serialVersionUID = 3470611986211472533L;

	/**
	 * {@code JSonObject.NULL} is equivalent to JavaScript's value null, while 
	 * Java's interpretation of {@code null} is equivalent to JavaScript's
	 * value of undefined.
	 */
	public static final class Null{
		
		/**
		 * The hashCode of Null
		 */
		private static final int hashCode = 0;
		
		/**
		 * This class functions as a helper for null values, both in Java and
		 * JSon. As such, only one copy is needed, so the clone method returns
		 * only this
		 * 
		 * @return Null
		 */
		@Override
		protected Object clone(){
			return this;
		}
		
		/**
		 * Determines if {@code Other} is equivalent to the Null. That is, if 
		 * {@code object} equals either {@code null} or {@code this}
		 *
		 * @param Other The object to compare
		 * @return If {@code Null} and {@code Other} are equal.
		 */
		@Override
		public boolean equals(Object Other){
			return Other == null || Other == this;
		}
		
		/**
		 * Returns the hashCode for the Null object. Since all Null objects
		 * reference the same class, the hashCode for this is the same value
		 * throughout.
		 * 
		 * @return The hashCode of Null
		 */
		@Override
		public int hashCode(){
			return hashCode;
		}
		
		/**
		 * Returns the String representation of this class,
		 * which is "null".
		 * 
		 * @return "null"
		 */
		@Override
		public String toString(){
			return "null";
		}
	}
	
	/**
	 * The Map of {@code JSonObject} Name/Value Pair Properties
	 */
	private Map<String, Object> JSonProperties;
	
	/**
	 * Instance of the {@code JSonObject.NULL} Null object.
	 */
	public static final JSonObject.Null NULL = new JSonObject.Null();
	
	/**
	 * Will create a {@code JSonObject} with no properties.
	 */
	public JSonObject(){
		
		JSonProperties = new HashMap<String, Object>();
	}
	
	/**
	 * Will create a {@code JSonObject} if it conforms to
	 * JSon formatting.
	 * 
	 * @param JSonString The JSonString to use as a source of input
	 */
	public JSonObject(String JSonString){
		
		try{
			JSonProperties = JSonConverter.createJSonPropertiesString(JSonString);
		}catch(JSonParseException JSPE){
			JSonProperties = new HashMap<String, Object>();
		}
	}
	
	/**
	 * Will create a {@code JSonObject} from the properties supplied
	 * by {@code JSonURL}
	 * 
	 * @param JSonURL The URL of the JSon String to read
	 */
	public JSonObject(URL JSonURL){
		
		try{
			BufferedReader Input = new BufferedReader(new InputStreamReader(JSonURL.openConnection().getInputStream()));
			JSonProperties = JSonConverter.createJSonPropertiesString(Input.readLine());
			Input.close();
		}catch(Exception E){
			JSonProperties = new HashMap<String, Object>();
		}
	}
	
	/**
	 * Will create a {@code JSonObject} with the properties of {@code JSP}
	 * 
	 * @param JSP A Map of Name/Value Pairs for the JSon Object
	 */
	public JSonObject(Map<String, Object> JSP){
		
		JSonProperties = JSP;
	}
	
	@Override
	public String toString(){
		String Data = "{";
		int currIndex = 0;
		for(Map.Entry<String, Object> P: JSonProperties.entrySet()){
			Data += "\"" + P.getKey() + "\"" + ":";
			if(P.getValue() instanceof String){
				Data += "\"" + P.getValue().toString() + "\"";
			}else{
				Data += P.getValue().toString();
			}
			if(currIndex + 1 != JSonProperties.entrySet().size()){
				Data += ",";
			}
			currIndex++;
		}
		Data += "}";
		return Data;
	}
	
	/**
	 * Returns the {@code JSonProperties}
	 * 
	 * @return The JSonProperties Map of the current {@code JSonObject}
	 */
	public Map<String, Object> getJSonProperties(){
		return JSonProperties;
	}
	
	/**
	 * Sets {@code JSonProperties} to the supplied map {@code JSP}
	 * 
	 * @param JSP The new {@code JSonProperties} of the current {@code JSonObject}
	 */
	public void setJSonProperties(Map<String, Object> JSP){
		JSonProperties = JSP;
	}
	
	public String getName(Object Value){
		for(Map.Entry<String, Object> P: JSonProperties.entrySet()){
			if(P.getValue().equals(Value)){
				return P.getKey();
			}
		}
		return null;
	}
	
	public ArrayList<String> getNameOccurences(Object Value){
		ArrayList<String> NameOccurences = new ArrayList();
		for(Map.Entry<String, Object> P: JSonProperties.entrySet()){
			if(P.getValue().equals(Value)){
				NameOccurences.add(P.getKey());
			}
		}
		return NameOccurences;
	}
	
	public String getDefaultName(Object Value, String DefaultName){
		String Name = this.getName(Value);
		return (Name == null ? DefaultName : Name);
	}
	
	public void addName(String NewName){
		JSonProperties.put(NewName, NULL);
	}
	
	public void setName(String OldName, String NewName){
		Object OldValue = null;
		for(Map.Entry<String, Object> P : JSonProperties.entrySet()){
			if(P.getKey().equals(OldName)){
				OldValue = P.getValue();
				break;
			}
		}
		if(OldValue != null){
			JSonProperties.remove(OldName);
			JSonProperties.put(NewName, OldValue);
		}
	}
	
	public void setName(Object Value, String NewName){
		String OldName = null;
		for(Map.Entry<String, Object> P : JSonProperties.entrySet()){
			if(P.getValue().equals(Value)){
				OldName = P.getKey();
				break;
			}
		}
		if(OldName != null){
			JSonProperties.remove(OldName);
			JSonProperties.put(NewName, Value);
		}
	}
	
	public Object getValue(String Name){
		for(Map.Entry<String, Object> P: JSonProperties.entrySet()){
			if(P.getKey().equals(Name)){
				return P.getValue();
			}
		}
		return null;
	}
	
	public ArrayList<Object> getValueOccurences(String Name){
		ArrayList<Object> ValueOccurences = new ArrayList();
		for(Map.Entry<String, Object> P: JSonProperties.entrySet()){
			if(P.getKey().equals(Name)){
				ValueOccurences.add(P.getValue());
			}
		}
		return ValueOccurences;
	}
	
	public Object getDefaultValue(String Name, Object DefaultValue){
		Object Value = this.getValue(Name);
		return (Value == null ? DefaultValue : Value);
	}
	
	public void setValue(Object OldValue, Object NewValue){
		String Key = this.getName(OldValue);
		JSonProperties.replace(Key, OldValue, NewValue);
	}
	
	public void setValue(String Name, Object NewValue){
		JSonProperties.replace(Name, NewValue);
	}
	
	public void addPair(String Name, Object Value){
		JSonProperties.put(Name, Value);
	}
	
	/**
	 * Compares 2 JSonObjects based on their respective name/property
	 * pair amount. If {@code Other} and {@code this} have the same number
	 * of pairs, it will return 0. If {@code Other} has more, it will return -1.
	 * If {@code Other} has less, it will return 1.
	 * 
	 * @param Other The JSonObject to compare to
	 * @return The comparison value.
	 */
	public int compareTo(JSonObject Other){
		if(Other.getJSonProperties().size() < this.getJSonProperties().size()){
			return 1;
		}else if(Other.getJSonProperties().size() > this.getJSonProperties().size()){
			return -1;
		}else{
			return 0;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((JSonProperties == null) ? 0 : JSonProperties.hashCode());
		return result;
	}
	
	/**
	 * Determines if {@code Other} equals, or is equivalent to the current
	 * JSONObject. If {@code Other} can be cast to a {@code JSonObject}, it will be cast and
	 * then compared. If it is not, an equivalent {@code JSonObject} will be created and compared.
	 * 
	 * @return Whether or not Other equals the current JSonObject
	 */
	public boolean equals(Object Other){
		boolean equalObjects = false;
		if(Other instanceof JSonObject){
			JSonObject CastOther = (JSonObject)Other;
			
		}
		return equalObjects;
	}
	
	public static void main(String args[]){
		try {
			System.out.println(new JSonObject(new URL("http://www.fimfiction.net/api/story.php?story=18087")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

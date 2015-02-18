package com.hightide.jjson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
 * A JSon String, as defined by the ECMA-404 Standard for JSon
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
 * <p>The JSonConverter class functions as a collection of methods used for the processing and
 * interpretation of JSonString. The {@code createJSonPropertiesString} and {@code createJSonPropertiesHTML}
 * methods provide for the creation of a map of name/value pairs with {@code java.langString} or HTML input.
 * Additionally, the {@code createJSonObjectString}, {@code createJSonObjectHTML},
 * {@code createJSonArrayString}, and {@code createJSonArrayHTML} methods provide for the creation of
 * JSonObjects and JSonArrays</p>
 * 
 * <p>In addition to providing methods for the creation of JSonObjects and JSonArrays, the
 * JSonConverter also provides the ability for any class that supports reflection or 
 * extends the {@code JSonObject} and {@code JSonArray} classes to be converted to an
 * ECMA-404 valid JSon string through the {@code createJSonString}.</p>
 * 
 * @author Jered Tupik
 * @version 1.0 2015-2-15
 */
public class JSonConverter{
	
	/**
	 * Creates a {@code HashMap<String, Object>} of name/value pairs from
	 * the supplied string {@code JSonString}
	 * 
	 * @param JSonString The {@code java.lang.String} to convert to a {@code HashMap<String, Object>}
	 * @throws JSonParseException If {@code JSonString} is not correctly formatted
	 * @return A {@code HashMap} of the name/value pairs of {@code JSonString}, or a blank {@code HashMap} if {@code JSonString} is null
	 */
	public static HashMap<String, Object> createJSonPropertiesString(String JSonString) throws JSonParseException{
		HashMap<String, Object> JSonProperties = new HashMap<String, Object>();
		StringBuilder Key = new StringBuilder("");
		Object Value = null;
		boolean creatingKey = true;
		int currIndex = 1;
		JSonString = JSonString.trim();
		
		if(JSonString.charAt(0) != JSonConstants.BEGIN_JSON_OBJECT){
			throw new JSonParseException("The supplied string does not start with {", 0);
		}
		while(currIndex < (JSonString.length() - 1)){
			
			/**
			 * The following code handles the parsing of JSon-formatted Strings
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.JSON_STRING){
				currIndex++;
				if(!creatingKey){
					Value = new StringBuilder("");
				}
				while(JSonString.charAt(currIndex) != JSonConstants.JSON_STRING){
					if(JSonString.charAt(currIndex) == '\\' &&
					   JSonString.charAt(currIndex + 1) == '/'){
						(creatingKey ? Key : (StringBuilder)Value).append('/');
						currIndex += 2;
					}else{
						(creatingKey ? Key : (StringBuilder)Value).append(JSonString.charAt(currIndex++));
					}
				}
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted numbers
			 */
			if(Character.isDigit(JSonString.charAt(currIndex)) || JSonString.charAt(currIndex) == '-'){
				int beginIndex = currIndex;
				while(JSonString.charAt(currIndex) != ',' && JSonString.charAt(currIndex) != ' ' 
					  && JSonString.charAt(currIndex) != '}'){
					currIndex++;
				}
				Value = Double.parseDouble(JSonString.substring(beginIndex, currIndex));
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted boolean
			 */
			if(JSonString.charAt(currIndex) == 't' && currIndex + 3 < JSonString.length()){
				if(JSonString.substring(currIndex, currIndex + 4).toLowerCase().equals("true")){
					Value = Boolean.TRUE;
				}
			}else if(JSonString.charAt(currIndex) == 'f' && currIndex + 4 < JSonString.length()){
				if(JSonString.substring(currIndex, currIndex + 5).toLowerCase().equals("false")){
					Value = Boolean.FALSE;
				}
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted Objects
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.BEGIN_JSON_OBJECT){
				String ObjectString = getJSonSubstring(JSonString.substring(currIndex), JSonConstants.BEGIN_JSON_OBJECT);
				Value = new JSonObject(ObjectString);
				currIndex += ObjectString.length() - 1;
			}
			
		    /**
		     * The following code handles the parsing of JSon-formatted Arrays
		     */
			if(JSonString.charAt(currIndex) == JSonConstants.BEGIN_JSON_ARRAY){
				String ArrayString = getJSonSubstring(JSonString.substring(currIndex), JSonConstants.BEGIN_JSON_ARRAY);
				Value = new JSonArray(ArrayString);
				currIndex += ArrayString.length() - 1;
			}
			
			/**
			 * Whenever the ':' symbol is hit in a JSon String, the mode of this parser
			 * is switched to creating a value.
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.JSON_PAIR){
				creatingKey = false;
			}
			
			/**
			 * The occurrence of a ',' means a separate name/value pair
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.JSON_COMMA){
				creatingKey = true;
			}
			
			if(Value != null){
				JSonProperties.put(Key.toString(), (Value instanceof StringBuilder ? Value.toString() : Value));
				Key = new StringBuilder("");
				Value = null;
			}
			currIndex++;
		}
		
		if(JSonString.charAt(JSonString.length() - 1) != JSonConstants.END_JSON_OBJECT){
			throw new JSonParseException("The supplied string does not end with }", JSonString.length() - 1);
		}
		
		return JSonProperties;
	}
	
	/**
	 * Creates a {@code HashMap<String, Object>} of name/value pairs from
	 * the supplied URL {@code JSonURL}
	 * 
	 * @param JSonURL The URL to read from
	 * @return A {@code HashMap<String, Object>} comprised of the {@code JSonURL}'s data
	 */
	public static HashMap<String, Object> createJSonPropertiesHTML(URL JSonURL){
		HashMap<String, Object> JSonProperties;
		try{
			BufferedReader Input = new BufferedReader(new InputStreamReader(JSonURL.openConnection().getInputStream()));
			JSonProperties = JSonConverter.createJSonPropertiesString(Input.readLine());
			Input.close();
		}catch(Exception E){
			JSonProperties = new HashMap<String, Object>();
		}
		return JSonProperties;
	}
	
	/**
	 * Returns a correctly determined substring from {@code JSonString}. That is,
	 * it will return the correct JSon object string needed, regardless of how many occurences
	 * of a symbol occur. See the example below.
	 * 
	 * <pre>
	 * "{"name":"CodeMonkey", "job":"Programmer", "manager":{"name":"Rob", "job":"manager"}}"
	 * </pre>
	 * 
	 * <p>In the given example, the {@code getJSonSubstring} method could be used to return
	 * the JSonObject from the manager name/value pair. This serves as a method to be used to
	 * 'simplify' the {@code JSonConverter}'s job.</p>
	 * 
	 * @param JSonString The string to return the substring from
	 * @param WRAP_CHARACTER The enclosing character symbol, i.e. {, ", or /
	 * @return A String compossing of the complete {@code WRAP_CHARACTER} pair.
	 */
	private static String getJSonSubstring(String JSonString, char WRAP_CHARACTER){
		Stack<Character> WrapOccurences = new Stack<Character>();
		WrapOccurences.push(WRAP_CHARACTER);
		char WRAP_PAIR = '}';
		int currIndex = 1;
		
		/**
		 * The following switch statement sets the character paired with
		 * any wrapping character. For JSon Objects, the {} pair is used.
		 * For arrays, the [] characters are used.
		 */
		switch(WRAP_CHARACTER){
			case '{':
				WRAP_PAIR = '}';
				break;
			case '[':
				WRAP_PAIR = ']';
				break;
			default:
				WRAP_PAIR = '}';
		}
		
		/**
		 * Iterates through the string looking for opening and closing pair occurences
		 */
		while(currIndex < JSonString.length()){
			if(JSonString.charAt(currIndex) == WRAP_CHARACTER){
				WrapOccurences.push(WRAP_CHARACTER);
			}else if(JSonString.charAt(currIndex) == WRAP_PAIR){
				WrapOccurences.pop();
				
			}
			if(WrapOccurences.isEmpty()){
				return JSonString.substring(0, currIndex + 1);
			}
			currIndex++;
		}
		return null;
	}
	
	/**
	 * Creates a {@code JSonObject} from
	 * the supplied string {@code JSonString}
	 * 
	 * @param JSonString The {@code java.lang.String} to convert to a {@code HashMap<String, Object>}
	 * @throws JSonParseException If {@code JSonString} is not correctly formatted
	 * @return A {@code JSonObject} of the name/value pairs of {@code JSonString}, or a blank {@code JSonObject} if {@code JSonString} is null
	 */
	public static JSonObject createJSonObjectString(String JSonString){
		try{
			return new JSonObject(createJSonPropertiesString(JSonString));
		}catch(JSonParseException E){
			return new JSonObject();
		}
	}
	
	/**
	 * Creates a {@code JSonObject} of from
	 * the supplied URL {@code JSonURL}
	 * 
	 * @param JSonURL The URL to read from
	 * @return A {@code JSonObject} comprised of the {@code JSonURL}'s data
	 */
	public static JSonObject createJSonObjectHTML(URL JSonURL){
		return new JSonObject(createJSonPropertiesHTML(JSonURL));
	}
	
	/**
	 * Creates a {@code ArrayList<Object>} of values from
	 * the supplied string {@code JSonString}
	 * 
	 * @param JSonString The {@code java.lang.String} to convert to a {@code HashMap<String, Object>}
	 * @throws JSonParseException If {@code JSonString} is not correctly formatted
	 * @return A {@code ArrayList} of the values pairs of {@code JSonString}, or a blank {@code HashMap} if {@code JSonString} is null
	 */
	public static ArrayList<Object> createJSonArrayString(String JSonString) throws JSonParseException{
		ArrayList<Object> JSonArrayList = new ArrayList<Object>();
		Object Value = null;
		int currIndex = 1;
		JSonString = JSonString.trim();
		
		if(JSonString.charAt(0) != JSonConstants.BEGIN_JSON_ARRAY){
			throw new JSonParseException("The supplied string does not start with [", 0);
		}
		while(currIndex < (JSonString.length() - 1)){
			
			/**
			 * The following code handles the parsing of JSon-formatted Strings
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.JSON_STRING){
				Value = new StringBuilder("");
				currIndex++;
				while(JSonString.charAt(currIndex) != JSonConstants.JSON_STRING){
					if(JSonString.charAt(currIndex) == '\\' &&
					   JSonString.charAt(currIndex + 1) == '/'){
						((StringBuilder)Value).append('/');
						currIndex += 2;
					}else{
						((StringBuilder)Value).append(JSonString.charAt(currIndex++));
					}
				}
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted numbers
			 */
			if(Character.isDigit(JSonString.charAt(currIndex)) || JSonString.charAt(currIndex) == '-'){
				int beginIndex = currIndex;
				while(JSonString.charAt(currIndex) != ',' && JSonString.charAt(currIndex) != ' ' 
					  && JSonString.charAt(currIndex) != '}'){
					currIndex++;
				}
				Value = Double.parseDouble(JSonString.substring(beginIndex, currIndex));
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted boolean
			 */
			if(JSonString.charAt(currIndex) == 't' && currIndex + 3 < JSonString.length()){
				if(JSonString.substring(currIndex, currIndex + 4).toLowerCase().equals("true")){
					Value = Boolean.TRUE;
				}
			}else if(JSonString.charAt(currIndex) == 'f' && currIndex + 4 < JSonString.length()){
				if(JSonString.substring(currIndex, currIndex + 5).toLowerCase().equals("false")){
					Value = Boolean.FALSE;
				}
			}
			
			/**
			 * The following code handles the parsing of JSon-formatted Objects
			 */
			if(JSonString.charAt(currIndex) == JSonConstants.BEGIN_JSON_OBJECT){
				String ObjectString = getJSonSubstring(JSonString.substring(currIndex), JSonConstants.BEGIN_JSON_OBJECT);
				Value = new JSonObject(ObjectString);
				currIndex += ObjectString.length() - 1;
			}
			
		    /**
		     * The following code handles the parsing of JSon-formatted Arrays
		     */
			if(JSonString.charAt(currIndex) == JSonConstants.BEGIN_JSON_ARRAY){
				String ArrayString = getJSonSubstring(JSonString.substring(currIndex), JSonConstants.BEGIN_JSON_ARRAY);
				Value = new JSonArray(ArrayString);
				currIndex += ArrayString.length() - 1;
			}
			
			if(Value != null){
				JSonArrayList.add(Value);
				Value = null;
			}
			currIndex++;
		}
		
		if(JSonString.charAt(JSonString.length() - 1) != JSonConstants.END_JSON_ARRAY){
			throw new JSonParseException("The supplied string does not end with ]", JSonString.length() - 1);
		}
		
		return JSonArrayList;
	}
	
	public static void main(String args[]){
		HashMap<String, Object> Properties = new HashMap<String, Object>();
		try{
			//Properties = createJSonPropertiesString("{\"name\":\"CodeMonkey\", \"job\":\"Programmer\", \"manager\":{\"name\":\"Rob\", \"job\":\"manager\", \"Alive\":false}, \"Alive\":true}");
			//Properties = createJSonPropertiesString("{\"name\":\"CodeMonkey\", \"job\":\"Programmer\", \"manager\":{\"name\":\"Rob\", \"job\":\"manager\"}}");
			//Properties = createJSonPropertiesString("{\"name\":\"CodeMonkey\", \"job\":\"Programmer\", \"Manager\":\"Rob\"}");
			System.out.println(new JSonObject(Properties = createJSonPropertiesString("{\"story\":{\"id\":18087,\"title\":\"The Best Night Ever\",\"url\":\"http:\\\\www.fimfiction.net\\story\\18087\\the-best-night-ever\",\"short_description\":\"Grand Galloping Gala meets Groundhog Day time-loop\",\"description\":\"Prince Blueblood thought the Grand Galloping Gala was over. He thought he could just go to sleep and put it behind him. He never expected to be reliving the same disaster of a day, over and over... and over.\r\n\r\nTV tropes page here:\r\nhttp:\\\\tvtropes.org\\pmwiki\\pmwiki.php\\FanFic\\TheBestNightEver\r\n\r\nThanks to all the people who wrote the page and all the people who gave me a little wakeup call and comment to become aware of that fact! You can't see it, but you guys put a real smile on my face tonight.\r\n\r\nAlso, recently, extra thanks to RD Dash for giving TBNE a thorough editing. I've updated the fic accordingly (7.11.12)\",\"date_modified\":1333106641,\"image\":\"\\\\www.fimfiction-static.net\\images\\story_images\\18087_r.jpg?1333063053\",\"full_image\":\"\\\\www.fimfiction-static.net\\images\\story_images\\18087.jpg?1333063053\",\"views\":37448,\"total_views\":123665,\"words\":53935,\"chapter_count\":5,\"comments\":567,\"author\":{\"id\":22220,\"name\":\"Capn_Chryssalid\"},\"status\":\"Complete\",\"content_rating_text\":\"Teen\",\"content_rating\":1,\"categories\":{\"Romance\":true,\"Tragedy\":false,\"Sad\":false,\"Dark\":false,\"Comedy\":true,\"Random\":false,\"Crossover\":false,\"Adventure\":true,\"Slice of Life\":false,\"Alternate Universe\":false,\"Human\":false,\"Anthro\":false},\"likes\":3731,\"dislikes\":53,\"chapters\":[{\"id\":55591,\"title\":\"Chapter One\",\"words\":12054,\"views\":37448,\"link\":\"http:\\\\www.fimfiction.net\\story\\18087\\1\\the-best-night-ever\\chapter-one\",\"date_modified\":1364967313},{\"id\":55610,\"title\":\"Chapter Two\",\"words\":7885,\"views\":20340,\"link\":\"http:\\\\www.fimfiction.net\\story\\18087\\2\\the-best-night-ever\\chapter-two\",\"date_modified\":1365014389},{\"id\":55612,\"title\":\"Chapter Three\",\"words\":10489,\"views\":20526,\"link\":\"http:\\\\www.fimfiction.net\\story\\18087\\3\\the-best-night-ever\\chapter-three\",\"date_modified\":1383860641},{\"id\":55614,\"title\":\"Chapter Four\",\"words\":12873,\"views\":20805,\"link\":\"http:\\\\www.fimfiction.net\\story\\18087\\4\\the-best-night-ever\\chapter-four\",\"date_modified\":1347378093},{\"id\":55615,\"title\":\"Chapter Five\",\"words\":10634,\"views\":24546,\"link\":\"http:\\\\www.fimfiction.net\\story\\18087\\5\\the-best-night-ever\\chapter-five\",\"date_modified\":1364890171}]}}")));
		}catch(JSonParseException Ex){
			
		}
	}
}

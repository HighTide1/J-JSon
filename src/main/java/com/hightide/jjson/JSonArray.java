package com.hightide.jjson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
 * A JSon Array, as defined by the ECMA-404 Standard for JSon
 * data interchanging, is an unordered collection of JSon values.
 * These values can range from JSon Objects, boolean expressions,
 * other JSon Arrays, or any other ECMA-404 supported format. Take the
 * following code as an example.
 * 
 * <pre>
 * {"name":"CodeMonkey", "jobs":["name":"bartender", "name":"programmer"]}
 * </pre>
 *
 * <p>As defined by the ECMA-404 Standard for JSon data interchanging format,
 * the order of values in a JSON Array is significant, and must be kept in
 * the order specified. In order to promote compatability, this JSonArray
 * will accept any properly-formatted JSon String as a valid input.</p>
 * 
 * <p>A JSonArray stores its values in an {@code java.util.ArrayList<Object>}.
 * Constructors for the JSonArray allow for the use of {@code java.lang.String},
 * {@code java.util.ArrayList}, and HTML based-input.</p>
 * 
 * <p>A JSonArray provides the {@code get} method for returning the value stored
 * at a particular index. Additionally, the {@code add}, {@code put}, and {@code set}
 * methods are provided to modify/add values at a given index.</p>
 * 
 * <p>Some of the JSon-Java library was used as an inspiration/base for this project, most noticably the
 * structure/commenting of this file. If this infringes upon any rights or licenses granted to the Java-JSon
 * library, please contact me to resolve this issue. All rights and references to JSon-Java are purely
 * informational in nature, and as such do not mean to infringe upon any previously mentioned grants.</p>
 * 
 * @author Jered Tupik
 * @version 1.0 2015-2-18
 */
public class JSonArray implements Serializable{

	/**
	 * Serializable ID for the JSonArray
	 */
	private static final long serialVersionUID = -4812524086196846396L;
	
	/**
	 * The ArrayList of the {@code JSonArray}'s values
	 */
	private ArrayList<Object> JSonArrayList;
	
	/**
	 * Will create a default(blank) {@code JSonArray}
	 */
	public JSonArray(){
		
		JSonArrayList = new ArrayList<Object>();
	}
	
	/**
	 * Will create a {@code JSonArray} from the given ArrayList<Object> {@code JSAL}
	 * 
	 * @param JSAL The {@code java.util.ArrayList} to set the JSonArray to.
	 */
	public JSonArray(ArrayList<Object> JSAL){
		
		JSonArrayList = JSAL;
	}
	
	/**
	 * Will create a {@code JSonArray} from the given String {@code JSonString}
	 * 
	 * @param JSonString The String to use an input source
	 */
	public JSonArray(String JSonString){
		
		try{
			JSonArrayList = JSonConverter.createJSonArrayString(JSonString);
		}catch(JSonParseException JSPE){
			JSonArrayList = new ArrayList<Object>();
		}
	}
	
	/**
	 * Will create a {@code JSonArray} from the given URL {@code JSonURL}
	 * 
	 * @param JSonURL The URL to use as an input source
	 */
	public JSonArray(URL JSonURL){
		
		try{
			BufferedReader Input = new BufferedReader(new InputStreamReader(JSonURL.openConnection().getInputStream()));
			JSonArrayList = JSonConverter.createJSonArrayString(Input.readLine());
			Input.close();
		}catch(Exception E){
			JSonArrayList = new ArrayList<Object>();
		}
	}
	
	@Override
	public String toString(){
		String Data = "[";
		for(int i = 0; i < JSonArrayList.size(); i++){
			Data += JSonArrayList.get(i).toString();
			if(i + 1 != JSonArrayList.size()){
				Data += ", ";
			}
			
		}
		Data += "]";
		return Data;
	}
	
	/**
	 * Returns the value at {@code Index}
	 * 
	 * @param Index The Index to retrieve the value at
	 * @return The value at {@code Index}, or null if Index > size
	 */
	public Object get(int Index){
		return JSonArrayList.get(Index);
	}
	
	/**
	 * Adds {@code newObject} to the {@code JSonArray}
	 * 
	 * @param newObject The Object to add
	 */
	public void add(Object newObject){
		JSonArrayList.add(newObject);
	}
	
	/**
	 * Puts {@code newObject} at the given {@code Index},
	 * or at the end of the {@code JSonArray}
	 * 
	 * @param newObject The Object to add
	 * @param Index The Index to add at
	 */
	public void put(Object newObject, int Index){
		JSonArrayList.add(Index, newObject);
	}
}

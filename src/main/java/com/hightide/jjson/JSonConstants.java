package com.hightide.jjson;

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
 * A collection of JSonConstants, mainly used for the conversion/deconversion
 * of JSonStrings and {@code JSonObject/JSonArray}.
 * 
 * @author Jered Tupik
 * @version 1.0 2015-2-16
 */

public class JSonConstants{
	
	/**
	 * The beginning character of a JSonObject
	 */
	public static final char BEGIN_JSON_OBJECT = '{';
	
	/**
	 * The ending character of a JSonObject
	 */
	public static final char END_JSON_OBJECT = '}';
	
	/**
	 * The beginning character of a JSonArray
	 */
	public static final char BEGIN_JSON_ARRAY = '[';
	
	/**
	 * The ending character of a JSonArray
	 */
	public static final char END_JSON_ARRAY = ']';
	
	/**
	 * The character signifying the seperation of
	 * a name/value pair in JSon
	 */
	public static final char JSON_PAIR = ':';
	
	/**
	 * The character signifying a JSON String
	 */
	public static final char JSON_STRING = '"';
	
	/**
	 * The character signifying seperate JSon values
	 */
	public static final char JSON_COMMA = ',';
}

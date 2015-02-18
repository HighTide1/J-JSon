package com.hightide.jjson;

import java.text.ParseException;

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
 * The JSonParseException serves as exception representing any error
 * in processing a string to and from a {@code JSonObject} and 
 * {@code JSonArray}.
 * 
 * @author Jered Tupik
 * @version 1.0 2015-2-15
 */

public class JSonParseException extends ParseException {

	/**
	 * Serializable ID of the JSonParseException
	 */
	private static final long serialVersionUID = -6785884862585422721L;

	public JSonParseException(String Cause, int ErrorLocation) {
		super(Cause, ErrorLocation);
	}

}

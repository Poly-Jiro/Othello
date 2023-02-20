package Util;

import java.awt.Point;


public class PointConverter {

	public static Point convertPointByString(final String address) {

		char addressX = address.charAt(0);
		final int x;
		switch (addressX) {
		case 'a':
			x = 0;
			break;
		case 'b':
			x = 1;
			break;
		case 'c':
			x = 2;
			break;
		case 'd':
			x = 3;
			break;
		case 'e':
			x = 4;
			break;
		case 'f':
			x = 5;
			break;
		case 'g':
			x = 6;
			break;
		case 's':
			return null;
		default:
			x = 7;
		}
		final int y = Integer.parseInt(address.substring(address.length() - 1)) - 1;

		return new Point(x,y);
	}
}

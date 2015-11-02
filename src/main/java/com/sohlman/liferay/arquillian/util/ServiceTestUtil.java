package com.sohlman.liferay.arquillian.util;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 * @author Alexander Chow
 * @author Manuel de la Peña
 */
public class ServiceTestUtil {


	public static Date newDate() throws Exception {
		return new Date();
	}

	public static Date newDate(int month, int day, int year) throws Exception {
		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.YEAR, year);

		return calendar.getTime();
	}

	public static Date nextDate() throws Exception {
		return new Date();
	}

	public static double nextDouble() throws Exception {
		return CounterLocalServiceUtil.increment();
	}

	public static int nextInt() throws Exception {
		return (int)CounterLocalServiceUtil.increment();
	}

	public static long nextLong() throws Exception {
		return CounterLocalServiceUtil.increment();
	}

	public static boolean randomBoolean() throws Exception {
		return _random.nextBoolean();
	}

	public static int randomInt() throws Exception {
		int value = _random.nextInt();

		if (value > 0) {
			return value;
		}
		else if (value == 0) {
			return randomInt();
		}
		else {
			return -value;
		}
	}

	public static Map<Locale, String> randomLocaleStringMap() throws Exception {
		return randomLocaleStringMap(LocaleUtil.getDefault());
	}

	public static Map<Locale, String> randomLocaleStringMap(Locale locale)
		throws Exception {

		Map<Locale, String> map = new HashMap<Locale, String>();

		map.put(LocaleUtil.getDefault(), randomString());

		return map;
	}

	public static long randomLong() throws Exception {
		long value = _random.nextLong();

		if (value > 0) {
			return value;
		}
		else if (value == 0) {
			return randomLong();
		}
		else {
			return -value;
		}
	}

	public static String randomString() throws Exception {
		return StringUtil.randomString();
	}

	public static String randomString(int length) throws Exception {
		return StringUtil.randomString(length);
	}

	private static Random _random = new Random();

}
package com.dosth.app.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dosth.app.dto.AppUser;

public class UserCache {
	public static Map<String, AppUser> userMap = new ConcurrentHashMap<>();
}
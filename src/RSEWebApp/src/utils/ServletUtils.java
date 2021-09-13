package utils;

import datafiles.dto.UserDto;
import datafiles.user.User;
import livechat.ChatManager;
import logicinterface.RizpaEngine;
import logicinterface.RizpaInterface;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
	private static final String ENGINE_ATTRIBUTE_NAME = "engine";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	private static final Object engineLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static RizpaInterface getEngine(ServletContext servletContext) {

		synchronized (engineLock) {
			if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new RizpaEngine());
			}
		}
		return (RizpaInterface) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
	}

	public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}


	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ignored) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

	public static boolean isAdmin(User user) {
		return user != null && user.getName().equalsIgnoreCase("admin");
	}

	public static boolean isAdmin(UserDto user) {
		return user != null && user.getName().equalsIgnoreCase("admin");
	}

    public static class ErrorMessage {
		public final int status;
		public final String message;

		public ErrorMessage(int status, String message) {
			this.status = status;
			this.message = message;
		}
	}
}

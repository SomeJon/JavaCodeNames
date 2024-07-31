package codenames;

import com.google.gson.Gson;
import data.server.controllers.ServerManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static Adapter.AdapterAddon.getGson;


public class ServerUtils {
	private final static Gson gson = getGson();
    private static final String SERVER_MANAGER_ATTRIBUTE_NAME = "ServerManager";
    private static final Object ServerManagerLock = new Object();
    public static ServerManager getServerManager(ServletContext servletContext) {

		synchronized (ServerManagerLock) {
			if (servletContext.getAttribute(SERVER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SERVER_MANAGER_ATTRIBUTE_NAME, new ServerManager());
			}
		}
		return (ServerManager) servletContext.getAttribute(SERVER_MANAGER_ATTRIBUTE_NAME);
	}

	public static void moveObjectIntoResponse(HttpServletResponse response, Object toWrite) throws IOException {
		String json = gson.toJson(toWrite);

		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    	response.getWriter().write(json);
	}
}

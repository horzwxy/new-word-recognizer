package me.horzwxy.wordservant.test;


public interface Values
{
	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 5555;
	
	public static final String HTML_HEAD = "<html>\n<head>\n<title>Marked artical</title>\n</head>\n<body>\n";
	public static final String HTML_TAIL = "</body>\n</html>\n";
	
	public static final String LABEL_P_START = "<p>";
	public static final String LABEL_P_END = "</p>\n";
	
	public static final String LABEL_A_START1 = "<a href=\"" + SERVER_IP + ":" + SERVER_PORT + "/";
	public static final String LABEL_A_START2 = "\">";
	public static final String LABEL_A_END = "</a>";
}

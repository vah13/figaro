package burp;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BurpExtender implements IBurpExtender, IHttpListener
{
    public IBurpExtenderCallbacks mCallbacks;
    private IExtensionHelpers     helpers;
    IBurpExtenderCallbacks cc = null;
    private PrintWriter           stdout;
    private PrintWriter           stderr;
    
    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks)
    {
        cc = callbacks;
        this.mCallbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        callbacks.setExtensionName("@vah_13_figaro");
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stderr = new PrintWriter(callbacks.getStderr(), true);
        
        mCallbacks.registerHttpListener(this);
    }
    
	@Override
	public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
		// TODO Auto-generated method stub
		
		// only process requests
        if (messageIsRequest)
        {
            // get the HTTP service for the request
        	
            IHttpService httpService = messageInfo.getHttpService();
            byte[] req = messageInfo.getRequest();
            String s2 = new String(req);
            String[] arr = s2.split("\n");
            String _coo = "";
            Calendar now = Calendar.getInstance();
            int sec = now.get(Calendar.SECOND);
            if (sec>0 && sec<5)
            {
            	try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            try {
				_coo = readFile("C:\\cookie.txt", StandardCharsets.UTF_8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            for (int i =0;i<arr.length;i++)
            {
            	if (arr[i].contains("Cookie:"))
            	{
            		stdout.println("--------------------------");
            		stdout.println(arr[i]);
            		arr[i] = "Cookie: "+_coo; //arr[i].replace("Cookie:", "Cookie: aaa=bbb;");
            		stdout.println(arr[i]);
                    stdout.println("--------------------------");
            		stdout.println("");stdout.println("");
            	}
            }
            s2 = strJoin(arr,"\n")+"\n\n";       
            stdout.println(s2); stdout.println("");stdout.println("");
            messageInfo.setRequest(s2.getBytes());
            
            messageInfo.setHttpService(helpers.buildHttpService(httpService.getHost(), httpService.getPort(), httpService.getProtocol()));
        }

	}
	public static String strJoin(String[] aArr, String sSep) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.length; i < il; i++) {
	        if (i > 0)
	            sbStr.append(sSep);
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}
	public String readFile(String path, Charset encoding) throws IOException 
		{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
		}
  }

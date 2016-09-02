package burp;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
            
            for (int i =0;i<arr.length;i++)
            {
            	if (arr[i].contains("Cookie:"))
            	{
            		stdout.println("--------------------------");
            		stdout.println(arr[i]);
            		arr[i] = arr[i].replace("Cookie:", "Cookie: aaa=bbb;");
            		stdout.println(arr[i]);
                    stdout.println("--------------------------");
            		stdout.println("");stdout.println("");
            	}
            }
            s2 = String.join("\n",arr)+"\n\n";       
            messageInfo.setRequest(s2.getBytes());
            
            messageInfo.setHttpService(helpers.buildHttpService(httpService.getHost(), httpService.getPort(), httpService.getProtocol()));
        }

	}
  }

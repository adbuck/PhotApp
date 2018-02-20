package photApp;

public class GetProxy {

public void findProxyForURL(String url) {

    if(url.matches(".*nysdot.private")|| url.equals("http://intradot/"))  {
        System.out.println(url + "doesn't need proxy");

        System.clearProperty("https.proxyHost");
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");
        System.clearProperty("https.proxyPort");
    }
else{
        System.setProperty("https.proxyHost", "gateway.zscalertwo.net");
        System.setProperty("http.proxyHost", "gateway.zscalertwo.net");
        System.setProperty("http.proxyPort", "80");
        System.setProperty("https.proxyPort", "443");
    }

	}

}
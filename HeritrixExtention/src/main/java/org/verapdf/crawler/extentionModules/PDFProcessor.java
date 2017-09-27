package org.verapdf.crawler.extentionModules;

import org.apache.commons.httpclient.Header;
import org.archive.modules.CrawlURI;
import org.archive.modules.writer.MirrorWriterProcessor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFProcessor extends MirrorWriterProcessor {
    public String getLogiusUrl() {
        return logiusUrl;
    }

    public void setLogiusUrl(String logiusUrl) {
        this.logiusUrl = logiusUrl;
    }

    private String logiusUrl;

    @Override
    protected void innerProcess(CrawlURI curi) {
        try {
            super.innerProcess(curi);
            String baseDir = getPath().getFile().getCanonicalPath();
            String mps = (String)curi.getData().get(A_MIRROR_PATH);
            String time = "Last-Modified: Thu, 01 Jan 1970 00:00:01 GMT";
            Header header = curi.getHttpMethod().getResponseHeader("Last-Modified");
            if(header != null) {
                time = header.toString().substring(0, header.toString().length() - 2);
            }

            String data = "{\"filepath\":\"" + baseDir + File.separator + mps +
                    "\", \"jobDirectory\":\"" + baseDir + "\", \"" +
                    "time\":\"" + time + "\", \"uri\":\"" + curi.getURI() + "\"}";
            URL url = new URL(logiusUrl + "api/heritrix/pdf");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();
            int code = connection.getResponseCode();
            if(code != 204) {
                System.out.println("Response code from logius: " + code);
            }
        } catch (IOException e) {
            System.out.println("PDF processor error");
            e.printStackTrace();
        }
    }
}
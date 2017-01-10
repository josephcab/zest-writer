package com.zestedesavoir.zestwriter.utils;

import com.zestedesavoir.zestwriter.MainApp;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.commons.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Markdown {
    private String htmlTemplate;
    private static final String CONTENT_KEYWORD = "<!--content-->";

    private String getHTMLTemplate() {
        if(htmlTemplate == null) {
            final String htmlTemplateLocation = "assets/static/html/template.html";
            InputStream is = MainApp.class.getResourceAsStream(htmlTemplateLocation);

            String template = "";
            try {
                template= IOUtils.toString(is, "UTF-8");
            } catch (IOException e) {
                log.error("Error when reading the template stream.", e);
            }

            Matcher pathMatcher = Pattern.compile("%%(.*)%%").matcher(template);

            StringBuffer sbCheatSheet = new StringBuffer();

            while (pathMatcher.find()) {
                String path = MainApp.class.getResource("assets" + pathMatcher.group(1)).toExternalForm();
                pathMatcher.appendReplacement(sbCheatSheet, path);
            }
            pathMatcher.appendTail(sbCheatSheet);
            htmlTemplate = new String(sbCheatSheet);
        }
        return htmlTemplate;
    }

    public String addHeaderAndFooter(String content) {
        return getHTMLTemplate().replaceFirst(CONTENT_KEYWORD, Matcher.quoteReplacement(content));
    }
}

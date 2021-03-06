package com.vjudge.ecnuvj.remote.shared.codeforces;

import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.remote.crawler.SimpleCrawler;
import com.vjudge.ecnuvj.tool.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public abstract class CFStyleCrawler extends SimpleCrawler {

    @Override
    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) throws Exception {
        String problemNum = problemId.replaceAll("^\\d*", "");

        info.title = Tools.regFind(html, "<div class=\"title\">\\s*" + problemNum + "\\. ([\\s\\S]*?)</div>").trim();
        Double timeLimit = 1000 * Double.parseDouble(Tools.regFind(html, "</div>([\\d\\.]+) seconds?\\s*</div>"));
        info.timeLimit = timeLimit.intValue();
        info.memoryLimit = 1024 * Integer.parseInt(Tools.regFind(html, "</div>(\\d+) megabytes\\s*</div>"));
        info.description = Tools.regFind(html, "standard output\\s*</div></div><div>([\\s\\S]*?)</div><div class=\"input-specification");
        if (StringUtils.isEmpty(info.description)) {
            info.description = "<div>" + Tools.regFind(html, "(<div class=\"input-file\">[\\s\\S]*?)</div><div class=\"input-specification");
        }
        info.input = Tools.regFind(html, "<div class=\"section-title\">\\s*Input\\s*</div>([\\s\\S]*?)</div><div class=\"output-specification\">");
        info.output = Tools.regFind(html, "<div class=\"section-title\">\\s*Output\\s*</div>([\\s\\S]*?)</div><div class=\"sample-tests\">");
        info.sampleInput = "<style type=\"text/css\">.input, .output {border: 1px solid #888888;} .output {margin-bottom:1em;position:relative;top:-1px;} .output pre,.input pre {background-color:#EFEFEF;line-height:1.25em;margin:0;padding:0.25em;} .title {background-color:#FFFFFF;border-bottom: 1px solid #888888;font-family:arial;font-weight:bold;padding:0.25em;}</style>" + Tools.regFind(html, "<div class=\"sample-test\">([\\s\\S]*?)</div>\\s*</div>\\s*</div>");
        info.hint = Tools.regFind(html, "<div class=\"section-title\">\\s*Note\\s*</div>([\\s\\S]*?)</div></div></div></div>");
        info.source = Tools.regFind(html, "(<a[^<>]+/contest/\\d+\">.+?</a>)");
    }

}

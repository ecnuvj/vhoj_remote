package com.vjudge.ecnuvj.remote.manager;

import com.vjudge.ecnuvj.entity.RawProblem;
import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.executor.Task;
import com.vjudge.ecnuvj.mapper.RawProblemMapper;
import com.vjudge.ecnuvj.remote.common.RemoteOj;
import com.vjudge.ecnuvj.remote.crawler.Crawler;
import com.vjudge.ecnuvj.remote.crawler.CrawlersHolder;
import com.vjudge.ecnuvj.remote.crawler.RawProblemInfo;
import com.vjudge.ecnuvj.tool.GsonUtil;
import com.vjudge.ecnuvj.tool.Handler;
import com.vjudge.ecnuvj.tool.HtmlHandleUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tcg
 * @date 2021/3/24
 */
@Component
public class CrawlProblemManager {
    private final static Logger log = LoggerFactory.getLogger(CrawlProblemManager.class);

    @Autowired
    RawProblemMapper rawProblemMapper;

    private final ConcurrentHashMap<String, Date> triggerCache = new ConcurrentHashMap<String, Date>();

    public void crawlProblem(RemoteOj remoteOj, String remoteProblemId, boolean enforce) {
        if (remoteOj == null || remoteProblemId == null || remoteProblemId.length() > 36) {
            log.error("Illegal remoteOJ or remoteProblemId");
            return;
        }
        int remoteOjCode = remoteOj.getCode();
        remoteProblemId = remoteProblemId.trim();

        RawProblem problem = rawProblemMapper.findRawProblemByRemoteOjAndRemoteId(remoteOjCode, remoteProblemId);
        if (problem == null) {
            problem = new RawProblem();
            problem.setRemoteOj(remoteOjCode);
            problem.setRemoteProblemId(remoteProblemId);
            problem.setCreatedAt(new Date());
            problem.setUpdatedAt(triggerCache.get(remoteOj + remoteProblemId));
            problem.setTitle("N/A");
        }
        crawlProblem(problem, enforce);
    }

    public void crawlProblem(RawProblem problem, boolean enforce) {
        long sinceTriggerTime = Long.MAX_VALUE;
        if (problem.getUpdatedAt() != null) {
            sinceTriggerTime = System.currentTimeMillis() - problem.getUpdatedAt().getTime();
        }
        boolean condition1 = sinceTriggerTime > 7L * 86400L * 1000L;
        boolean condition2 = problem.getTimeLimit() != null && problem.getTimeLimit().equals("2") && sinceTriggerTime > 600L * 1000L;
        boolean condition3 = enforce && (problem.getTimeLimit() != null && !problem.getTimeLimit().equals("1") || sinceTriggerTime > 3600L * 1000L);
        if (condition1 || condition2 || condition3) {
            problem.setTimeLimit("1");
            problem.setUpdatedAt(new Date());
            rawProblemMapper.addOrModifyRawProblem(problem);
            new CrawlProblemTask(problem).submit();

            triggerCache.put(problem.getRemoteOj() + problem.getRemoteProblemId(), new Date());
            if (triggerCache.size() > 1000) {
                triggerCache.clear();
            }
        }
    }

    class CrawlProblemTask extends Task<Void> {

        protected static final boolean SAVE_IMG_TO_VJ_SERVER = true;

        private RawProblem problem;

        public CrawlProblemTask(RawProblem problem) {
            super(ExecutorTaskType.UPDATE_PROBLEM_INFO);
            this.problem = problem;
        }

        @Override
        public Void call() throws Exception {
            Crawler crawler = null;
            try {
                crawler = CrawlersHolder.getCrawler(RemoteOj.codeValueOf(problem.getRemoteOj()));
                Validate.notNull(crawler);
            } catch (Throwable t) {
                _onError(t);
                return null;
            }
            crawler.crawl(problem.getRemoteProblemId(), new Handler<RawProblemInfo>() {

                @Override
                public void handle(RawProblemInfo info) {
                    problem.setTitle(info.title);
                    problem.setTimeLimit("" + info.timeLimit);
                    problem.setMemoryLimit("" + info.memoryLimit);
                    problem.setSource(info.source);

                    if (SAVE_IMG_TO_VJ_SERVER) {
                        problem.setDescription(HtmlHandleUtil.transformImgUrlToLocal(info.description));
                        problem.setInput(HtmlHandleUtil.transformImgUrlToLocal(info.input));
                        problem.setOutput(HtmlHandleUtil.transformImgUrlToLocal(info.output));
                        problem.setHint(HtmlHandleUtil.transformImgUrlToLocal(info.hint));
                    } else {
                        problem.setDescription(info.description);
                        problem.setInput(info.input);
                        problem.setOutput(info.output);
                        problem.setHint(info.hint);
                    }
                    problem.setSampleInput(info.sampleInput);
                    problem.setSampleOutput(info.sampleOutput);
                    problem.setUpdatedAt(new Date());
                    rawProblemMapper.addOrModifyRawProblem(problem);
                    System.out.println(GsonUtil.toJson(info));
                }

                @Override
                public void onError(Throwable t) {
                    log.error(t.getMessage(), t);
                    _onError(t);
                }
            });

            return null;
        }

        private void _onError(Throwable t) {
            log.error(t.getMessage(), t);
        }

//        private Description getSystemDescription() {
//            if (problem.getDescriptions() != null) {
//                for (Description desc : problem.getDescriptions()) {
//                    if ("0".equals(desc.getAuthor())) {
//                        return desc;
//                    }
//                }
//            }
//            Description description = new Description();
//            description.setAuthor("0");
//            description.setRemarks("Initialization.");
//            description.setVote(0);
//            description.setProblem(problem);
//
//            return description;
//        }

    }
}

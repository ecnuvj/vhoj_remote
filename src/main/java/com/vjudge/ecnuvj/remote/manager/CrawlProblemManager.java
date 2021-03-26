package com.vjudge.ecnuvj.remote.manager;

import com.vjudge.ecnuvj.bean.Description;
import com.vjudge.ecnuvj.bean.Problem;
import com.vjudge.ecnuvj.executor.ExecutorTaskType;
import com.vjudge.ecnuvj.executor.Task;
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

    private ConcurrentHashMap<String, Date> triggerCache = new ConcurrentHashMap<String, Date>();

    public void crawlProblem(String remoteOj, String remoteProblemId, boolean enforce) {
        if (remoteOj == null || remoteProblemId == null || remoteProblemId.length() > 36) {
            log.error("Illegal remoteOJ or remoteProblemId");
            return;
        }
        remoteOj = remoteOj.trim();
        remoteProblemId = remoteProblemId.trim();

        Problem problem = null;
        //judgeService.findProblem(remoteOj, remoteProblemId);
        if (problem == null) {
            problem = new Problem();
            problem.setOriginOJ(remoteOj);
            problem.setOriginProb(remoteProblemId);
            problem.setTriggerTime(triggerCache.get(remoteOj + remoteProblemId));
            problem.setTitle("N/A");
        }
        crawlProblem(problem, enforce);
    }

    public void crawlProblem(Problem problem, boolean enforce) {
        long sinceTriggerTime = Long.MAX_VALUE;
        if (problem.getTriggerTime() != null) {
            sinceTriggerTime = System.currentTimeMillis() - problem.getTriggerTime().getTime();
        }
        boolean condition1 = sinceTriggerTime > 7L * 86400L * 1000L;
        boolean condition2 = problem.getTimeLimit() == 2 && sinceTriggerTime > 600L * 1000L;
        boolean condition3 = enforce && (problem.getTimeLimit() != 1 || sinceTriggerTime > 3600L * 1000L);
        if (condition1 || condition2 || condition3) {
            problem.setTimeLimit(1);
            problem.setTriggerTime(new Date());
            //baseService.addOrModify(problem);
            new CrawlProblemTask(problem).submit();

            triggerCache.put(problem.getOriginOJ() + problem.getOriginProb(), new Date());
            if (triggerCache.size() > 1000) {
                triggerCache.clear();
            }
        }
    }

    class CrawlProblemTask extends Task<Void> {

        protected static final boolean SAVE_IMG_TO_VJ_SERVER = true;

        private Problem problem;

        public CrawlProblemTask(Problem problem) {
            super(ExecutorTaskType.UPDATE_PROBLEM_INFO);
            this.problem = problem;
        }

        @Override
        public Void call() throws Exception {
            Crawler crawler = null;
            try {
                crawler = CrawlersHolder.getCrawler(RemoteOj.valueOf(problem.getOriginOJ()));
                Validate.notNull(crawler);
            } catch (Throwable t) {
                _onError(t);
                return null;
            }
            crawler.crawl(problem.getOriginProb(), new Handler<RawProblemInfo>() {

                @Override
                public void handle(RawProblemInfo info) {
                    problem.setTitle(info.title);
                    problem.setTimeLimit(info.timeLimit);
                    problem.setMemoryLimit(info.memoryLimit);
                    problem.setSource(info.source);
                    problem.setUrl(info.url);

                    Description description = getSystemDescription();
                    if (SAVE_IMG_TO_VJ_SERVER) {
                        description.setDescription(HtmlHandleUtil.transformImgUrlToLocal(info.description));
                        description.setInput(HtmlHandleUtil.transformImgUrlToLocal(info.input));
                        description.setOutput(HtmlHandleUtil.transformImgUrlToLocal(info.output));
                        description.setHint(HtmlHandleUtil.transformImgUrlToLocal(info.hint));
                    } else {
                        description.setDescription(info.description);
                        description.setInput(info.input);
                        description.setOutput(info.output);
                        description.setHint(info.hint);
                    }
                    description.setSampleInput(info.sampleInput);
                    description.setSampleOutput(info.sampleOutput);
                    description.setUpdateTime(new Date());
                    //baseService.addOrModify(problem);
                    //baseService.addOrModify(description);
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
            if (problem.getDescriptions() == null || problem.getDescriptions().isEmpty()) {
                // Never crawled successfully
                //baseService.delete(problem);
            } else {
                problem.setTimeLimit(2);
                //baseService.addOrModify(problem);
            }
        }

        private Description getSystemDescription() {
            if (problem.getDescriptions() != null) {
                for (Description desc : problem.getDescriptions()) {
                    if ("0".equals(desc.getAuthor())) {
                        return desc;
                    }
                }
            }
            Description description = new Description();
            description.setAuthor("0");
            description.setRemarks("Initialization.");
            description.setVote(0);
            description.setProblem(problem);

            return description;
        }

    }
}

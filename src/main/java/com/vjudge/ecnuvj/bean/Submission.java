package com.vjudge.ecnuvj.bean;

import com.vjudge.ecnuvj.remote.status.RemoteStatusType;
import lombok.Data;

import java.util.Date;

/**
 * 提交记录
 *
 * @author Isun
 */
@Data
public class Submission {
    private int id;            //Hibernate统编ID

    private String status;        //状态
    private String statusCanonical;

    private String additionalInfo;    //额外信息，记录编译错误等信息
    private String realRunId;    //在原OJ的RunId

    private int time;            //运行时间(未AC提交为空    单位:ms)
    private int memory;            //运行内存(未AC提交为空    单位:KB)
    private Date subTime;        //提交时间

    private String language;    //语言 remote oj 真实提交语言代号
    private String source;        //源代码
    private int isOpen;            //代码是否公开

    private String dispLanguage;//用于显示的语言
    private String username;    //提交者用户名

    private String originOJ;    //原始OJ
    private String originProb;    //原始OJ题号
    private int isPrivate;        //是否不公开

    private String remoteAccountId; //交题账号
    private Date remoteSubmitTime;    //原始OJ提交时间
    private int queryCount;            //累计查询状态次数
    private Date statusUpdateTime;    //最后查询状态时间
    private int failCase = -1;

    private String languageCanonical;

    private long problemId;
    private long userId;


    public void reset() {
        setStatus("Pending");
        setStatusCanonical(RemoteStatusType.PENDING.name());
        setAdditionalInfo(null);
        setRealRunId(null);
        setTime(0);
        setMemory(0);
        setRemoteAccountId(null);
        setRemoteSubmitTime(null);
        setQueryCount(0);
        setStatusUpdateTime(null);
        setFailCase(-1);
    }
}

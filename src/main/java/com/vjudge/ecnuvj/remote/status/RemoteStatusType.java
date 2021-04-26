package com.vjudge.ecnuvj.remote.status;

/**
 * submission remote status type
 *
 * @author Isun
 */
public enum RemoteStatusType {

    PENDING(false, 0), // to submit to remote OJ

    SUBMIT_FAILED_TEMP(false, 1), // failed submitting to remote OJ, due to unknown reason
    SUBMIT_FAILED_PERM(true, 2), // failed submitting to remote OJ, due to known reason

    SUBMITTED(false, 3), // submitted to remote OJ

    QUEUEING(false, 4), // queuing in remote OJ
    COMPILING(false, 5), // compiling in remote OJ
    JUDGING(false, 6), // judging in remote OJ

    AC(true, 7),
    PE(true, 8),
    WA(true, 9),
    TLE(true, 10),
    MLE(true, 11),
    OLE(true, 12),
    RE(true, 13),
    CE(true, 14),
    FAILED_OTHER(true, 15),


    ;


    public boolean finalized;
    public int code;

    RemoteStatusType(boolean finalized, int code) {
        this.finalized = finalized;
        this.code = code;
    }

}

package org.atinject.core.management.threading;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ThreadDumpService {

    @Inject
    private ThreadMXBean threadMXBean;

    private boolean dumpLockedMonitors;
    private boolean dumpLockedSynchronizers;

    @PostConstruct
    public void initialize() {
        dumpLockedMonitors = true;
        dumpLockedSynchronizers = true;
    }

    public List<ThreadInfo> getThreadDump() {
        return Arrays.asList(
            threadMXBean.dumpAllThreads(dumpLockedMonitors, dumpLockedSynchronizers));
    }

    public List<ThreadInfo> getThreadDump(String regexFilter) {
        return filterThreadDump(
            getThreadDump(),
            regexFilter);
    }

    private List<ThreadInfo> filterThreadDump(List<ThreadInfo> threadInfos, String regexFilter) {
    	return threadInfos.stream()
    			.filter(threadInfo -> threadInfo.toString().matches(regexFilter))
    			.collect(Collectors.toList());
    }

    public String getThreadDumpAsString() {
        return threadDumpToString(getThreadDump());
    }

    public String getThreadDumpAsString(String regexFilter) {
        return threadDumpToString(getThreadDump(regexFilter));
    }

    private String threadDumpToString(List<ThreadInfo> threadInfos) {
    	return threadInfos.stream()
    			.map(threadInfo -> threadInfo.toString())
    			.collect(Collectors.joining("\n"));
    }
}
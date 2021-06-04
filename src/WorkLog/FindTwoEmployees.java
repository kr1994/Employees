package WorkLog;

import java.util.*;
import java.util.stream.Collectors;

public class FindTwoEmployees {

    public static WorkLogPair findMostLongerCoworkers(List<WorkLogDto> allWorkLogs) {
        // Group all WorkLogDto objects by project id
        Map<Integer, List<WorkLogDto>> workLogsMappedByProjectId = allWorkLogs.stream()
                .collect(Collectors.groupingBy(WorkLogDto::getProjectId));

        // Received all worklog durations and find the longest one
        Map.Entry<String, Long> workDurations = calculateWorkDurations(workLogsMappedByProjectId).entrySet()
                .stream().max(Comparator.comparing(Map.Entry::getValue)).orElseThrow();
        int days = (int) (workDurations.getValue() / (1000 * 60 * 60 * 24));  // convert milliseconds to days
        return fillWorkLogPair(workDurations, days);
    }

    public static Map<String, Long> calculateWorkDurations(Map<Integer, List<WorkLogDto>> workLogsMappedByProjectId) {
        Map<String, Long> pairDurations = new HashMap<>();
        calculateEachPairWorkDurations(workLogsMappedByProjectId, pairDurations);
        return pairDurations;
    }

    private static void calculateEachPairWorkDurations(Map<Integer, List<WorkLogDto>> workLogsMappedByProjectId,
                                                       Map<String, Long> pairDurations) {

        // check employees each other who are/were working together and if they did, put their worktimes
        // in pairDurations object

        for (List<WorkLogDto> workLogsPerProject : workLogsMappedByProjectId.values()) {
            for (int i = 0; i < workLogsPerProject.size(); i++) {
                for (int j = 1; j < workLogsPerProject.size(); j++) {

                    int firstEmployeeId = workLogsPerProject.get(i).getEmployeeId();
                    int secondEmployeeId = workLogsPerProject.get(j).getEmployeeId();
                    int projectId = workLogsPerProject.get(i).getProjectId();

                    if (firstEmployeeId < secondEmployeeId) {
                        String employeePairId = firstEmployeeId + "-" + secondEmployeeId + "-" + projectId;

                        long startTimeStamp = calculateStartTimeStamp(i, j, workLogsPerProject);
                        long endTimeStamp = calculateEndTimeStamp(i, j, workLogsPerProject);

                        compareWorkDurations(pairDurations, employeePairId, startTimeStamp, endTimeStamp);
                    }
                }
            }
        }
    }

    private static void compareWorkDurations(Map<String, Long> pairDurations, String employeePairId,
                                             long startTimeStamp, long endTimeStamp) {

        // compare work durations of a pair of employees, if they worked together,
        // the duration is added to pairDurations object
        if (startTimeStamp < endTimeStamp) {
            long duration = endTimeStamp - startTimeStamp;
            if (pairDurations.get(employeePairId) == null) {
                pairDurations.put(employeePairId, duration);
            } else {
                long addDuration = pairDurations.get(employeePairId);
                addDuration += duration;
                pairDurations.replace(employeePairId, addDuration);
            }
        }
    }

    private static long calculateStartTimeStamp(int i, int j, List<WorkLogDto> workLogsPerProject) {
        // find when started working in the project the employee who joined it later
        long startTimeOfTheFirstEmployee = workLogsPerProject.get(i).getDateFrom().getTime();
        long startTimeOfTheSecondEmployee = workLogsPerProject.get(j).getDateFrom().getTime();
        return Long.max(startTimeOfTheFirstEmployee, startTimeOfTheSecondEmployee);
    }

    private static long calculateEndTimeStamp(int i, int j, List<WorkLogDto> workLogsPerProject) {
        // find when finished working in the project the employee who quitted it earlier
        long endTimeOfTheFirstEmployee = workLogsPerProject.get(i).getDateTo().getTime();
        long endTimeOfTheSecondEmployee = workLogsPerProject.get(j).getDateTo().getTime();
        return Long.min(endTimeOfTheFirstEmployee, endTimeOfTheSecondEmployee);
    }

    private static WorkLogPair fillWorkLogPair(Map.Entry<String, Long> workDurations, int days) {
        // create a workpair object with information about the two employee Ids, project Id and duration
        // of their worktime together
        String[] ids = workDurations.getKey().split("-");
        return new WorkLogPair(ids[0], ids[1], ids[2], days);
    }

}

package WorkLog;

public class WorkLogPair {

    private String employeeId1;
    private String employeeId2;
    private String projectId;
    private long duration;

    public WorkLogPair(String employeeId1, String employeeId2, String projectId, long duration) {
        this.employeeId1 = employeeId1;
        this.employeeId2 = employeeId2;
        this.projectId = projectId;
        this.duration = duration;
    }

    public String getEmployeeId1() {
        return employeeId1;
    }

    public String getEmployeeId2() {
        return employeeId2;
    }

    public String getProjectId() {
        return projectId;
    }

    public long getDuration() {
        return duration;
    }

}

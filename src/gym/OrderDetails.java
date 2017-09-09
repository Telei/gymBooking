package gym;

import java.util.Date;

public class OrderDetails {
    private String gymName;
    private Date startTime;
    private String timePeriod;
    private boolean isBreachOfContract;//是否是违约金
    private int inCome;

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getInCome() {
        return inCome;
    }

    public void setInCome(int inCome) {
        this.inCome = inCome;
    }

    public boolean isBreachOfContract() {
        return isBreachOfContract;
    }

    public void setBreachOfContract(boolean breachOfContract) {
        isBreachOfContract = breachOfContract;
    }
}

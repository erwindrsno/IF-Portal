package pertemuan;

public class TimeSlot implements Comparable<TimeSlot> {
    private String id;
    private String startTime;
    private String endTime;
    private String day;


    public String getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return startTime.substring(0, 5) + " - " +endTime.substring(0, 5);
    }

    public TimeSlot(String id, String startTime, String endTime, String day) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    @Override
    public int compareTo(TimeSlot timeSlot) {
        int startHourThis = IFPortalDateTimeFormatter.getHourFromTime(startTime);
        int startHourOther = IFPortalDateTimeFormatter.getHourFromTime(timeSlot.startTime);
        int startMinuteThis = IFPortalDateTimeFormatter.getMinuteFromTime(startTime);
        int startMinuteOther = IFPortalDateTimeFormatter.getMinuteFromTime(timeSlot.startTime);
        int hourDiff = startHourThis - startHourOther;
        if(hourDiff == 0){
            return startMinuteThis - startMinuteOther;
        }
        return hourDiff;
    }
}

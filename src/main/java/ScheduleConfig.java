public record ScheduleConfig(int intervalSeconds, boolean enabled) {

    public ScheduleConfig {
        if(intervalSeconds < 10){
            throw new IllegalArgumentException("Interval must be at least 10 seconds");
        }
    }
}

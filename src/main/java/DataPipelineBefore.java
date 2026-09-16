public class DataPipelineBefore {

    private String name;
    private SourceType sourceType;
    private String destination;
    private ScheduleConfig schedule;
    private int batchSize;
    private int retryCount;
    private int parallelism;
    private boolean compression;
    private boolean encryption;
    private String format;
    private boolean monitoring;

    public DataPipelineBefore(String name, SourceType sourceType, String destination,
                              ScheduleConfig schedule, int batchSize, int retryCount,
                              int parallelism, boolean compression, boolean encryption,
                              String format, boolean monitoring) {

        this.name = name;
        this.sourceType = sourceType;
        this.destination = destination;
        this.schedule = schedule;
        this.batchSize = batchSize;
        this.retryCount = retryCount;
        this.parallelism = parallelism;
        this.compression = compression;
        this.encryption = encryption;
        this.format = format;
        this.monitoring = monitoring;
    }
}

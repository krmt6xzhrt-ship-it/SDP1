public final class DataPipeline {
    private final String name;
    private final SourceType sourceType;
    private final String destination;
    private final ScheduleConfig schedule;

    private final int batchSize;
    private final int retryCount;
    private final int parallelism;
    private final boolean compression;
    private final boolean encryption;
    private final String format;
    private final boolean monitoring;

    private DataPipeline(Builder builder){
        this.name = builder.name;
        this.sourceType = builder.sourceType;
        this.destination = builder.destination;
        this.schedule = builder.schedule;

        this.batchSize = builder.batchSize;
        this.retryCount = builder.retryCount;
        this.parallelism = builder.parallelism;
        this.compression = builder.compression;
        this.encryption = builder.encryption;
        this.format = builder.format;
        this.monitoring = builder.monitoring;
    }

    public String getName() {
        return name;
    }
    public SourceType getSourceType() {
        return sourceType;
    }
    public String getDestination() {
        return destination;
    }
    public ScheduleConfig getSchedule() {
        return schedule;
    }
    public int getBatchSize() {
        return batchSize;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public int getParallelism() {
        return parallelism;
    }
    public boolean isCompression() {
        return compression;
    }
    public boolean isEncryption() {
        return encryption;
    }
    public String getFormat() {
        return format;
    }
    public boolean isMonitoring() {
        return monitoring;
    }

    @Override
    public String toString() {
        return "DataPipeline{" +
                "name='" + name + '\'' +
                ", sourceType=" + sourceType +
                ", destination='" + destination + '\'' +
                ", interval=" + schedule.intervalSeconds() +
                ", batchSize=" + batchSize +
                ", retryCount=" + retryCount +
                ", parallelism=" + parallelism +
                ", compression=" + compression +
                ", encryption=" + encryption +
                ", format='" + format + '\'' +
                ", monitoring=" + monitoring +
                '}';
    }

    public static class Builder {
        // required fields
        private final String name;
        private final SourceType sourceType;
        private final String destination;
        private final ScheduleConfig schedule;

        // optional fields, these values are used if user does not change them
        private int batchSize = 500;
        private int retryCount = 3;
        private int parallelism = 1;
        private boolean compression = false;
        private boolean encryption = false;
        private String format = "JSON";
        private boolean monitoring = false;

        public Builder(String name, SourceType sourceType,
                       String destination, ScheduleConfig schedule) {
            if(name == null || name.isBlank())
                throw new IllegalArgumentException("Pipeline name is required");
            if(sourceType == null)
                throw new IllegalArgumentException("Source type is required");

            if(destination == null || destination.isBlank())
                throw new IllegalArgumentException("Destination is required");
            if(schedule == null)
                throw new IllegalArgumentException("Schedule is required");

            this.name = name;
            this.sourceType = sourceType;
            this.destination = destination;
            this.schedule = schedule;
        }

        public Builder batchSize(int size) {
            if(size < 100 || size > 10000)
                throw new IllegalArgumentException("Batch size must be from 100 to 10000");
            this.batchSize = size;
            return this;
        }


        public Builder retryCount(int count){

            if(count < 0 || count > 10)
                throw new IllegalArgumentException("Retry count must be from 0 to 10");

            this.retryCount = count;
            return this;
        }


        public Builder parallelism(int value){

            if(value < 1 || value > 16)
                throw new IllegalArgumentException("Parallelism must be from 1 to 16");

            this.parallelism = value;
            return this;
        }


        public Builder enableCompression(){
            this.compression = true;
            return this;
        }


        public Builder enableEncryption(){
            this.encryption = true;
            return this;
        }

        public Builder format(String format){
            if(format == null || format.isBlank())
                throw new IllegalArgumentException("Format cannot be empty");
            this.format = format.toUpperCase();
            return this;
        }

        public Builder enableMonitoring(){
            this.monitoring = true;
            return this;
        }
        public DataPipeline build(){

            validateDependencies();

            return new DataPipeline(this);
        }
        private void validateDependencies(){
            // I put dependent checks here because one field depends on another one
            if(encryption && !(destination.startsWith("https://") || destination.startsWith("s3://"))){
                throw new IllegalStateException(
                        "Encryption requires secure destination (https:// or s3://)"
                );
            }
            // with high parallelism I require bigger batches so workers are not almost empty
            if(parallelism > 4 && batchSize < 1000){
                throw new IllegalStateException(
                        "Parallelism above 4 requires batch size at least 1000"
                );
            }
            // stream data should be checked often
            if(sourceType == SourceType.STREAM && schedule.intervalSeconds() > 60){
                throw new IllegalStateException(
                        "STREAM source requires interval 60 seconds or less"
                );
            }
        }
    }
}

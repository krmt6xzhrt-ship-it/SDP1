public final class PipelinePresets {

    private PipelinePresets(){}

    public static DataPipeline basic(){

        return new DataPipeline.Builder(
                "Basic file pipeline",
                SourceType.FILE,
                "/data/output",
                new ScheduleConfig(300,true)
        )
                .batchSize(500)
                .retryCount(2)
                .format("csv")
                .build();
    }
    public static DataPipeline safe(){
        return new DataPipeline.Builder(
                "Safe customer pipeline",
                SourceType.DATABASE,
                "https://backup.example.com/customer-data",
                new ScheduleConfig(120,true)
        )
                .batchSize(1000)
                .retryCount(5)
                .enableCompression()
                .enableEncryption()
                .enableMonitoring()
                .format("json")
                .build();
    }

    public static DataPipeline performance(){
        return new DataPipeline.Builder(
                "Fast analytics pipeline",
                SourceType.API,
                "s3://analytics-bucket/output",
                new ScheduleConfig(30,true)
        )
                .batchSize(4000)
                .retryCount(3)
                .parallelism(8)
                .enableCompression()
                .enableMonitoring()
                .format("parquet")
                .build();
    }
}

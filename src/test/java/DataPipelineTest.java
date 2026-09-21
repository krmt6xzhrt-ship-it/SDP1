
class DataPipelineTest {


    @Test
    void basicPresetIsValid(){

        DataPipeline pipeline = PipelinePresets.basic();

        assertEquals("CSV", pipeline.getFormat());
        assertEquals(500, pipeline.getBatchSize());
    }


    @Test
    void safePresetIsValid(){

        DataPipeline pipeline = PipelinePresets.safe();

        assertTrue(pipeline.isEncryption());
        assertTrue(pipeline.isMonitoring());
    }


    @Test
    void performancePresetIsValid(){

        DataPipeline pipeline = PipelinePresets.performance();

        assertEquals(8, pipeline.getParallelism());
        assertEquals(4000, pipeline.getBatchSize());
    }


    @Test
    void blankNameIsInvalid(){

        assertThrows(IllegalArgumentException.class, () ->
                new DataPipeline.Builder(
                        "",
                        SourceType.FILE,
                        "/data/out",
                        new ScheduleConfig(60,true)
                )
        );
    }


    @Test
    void tooSmallBatchIsInvalid(){

        assertThrows(IllegalArgumentException.class, () ->
                new DataPipeline.Builder(
                        "test",
                        SourceType.FILE,
                        "/data/out",
                        new ScheduleConfig(60,true)
                ).batchSize(50)
        );
    }


    @Test
    void retryCountAboveTenIsInvalid(){

        assertThrows(IllegalArgumentException.class, () ->
                new DataPipeline.Builder(
                        "test",
                        SourceType.FILE,
                        "/data/out",
                        new ScheduleConfig(60,true)
                ).retryCount(11)
        );
    }


    @Test
    void batchSize100IsBoundary(){

        DataPipeline pipeline = new DataPipeline.Builder(
                "boundary",
                SourceType.FILE,
                "/data/out",
                new ScheduleConfig(60,true)
        )
                .batchSize(100)
                .build();

        assertEquals(100,pipeline.getBatchSize());
    }


    @Test
    void parallelism16IsBoundary(){

        DataPipeline pipeline = new DataPipeline.Builder(
                "boundary",
                SourceType.API,
                "/data/out",
                new ScheduleConfig(60,true)
        )
                .batchSize(1000)
                .parallelism(16)
                .build();

        assertEquals(16,pipeline.getParallelism());
    }


    @Test
    void encryptionNeedsSecureDestination(){

        assertThrows(IllegalStateException.class, () ->
                new DataPipeline.Builder(
                        "secret pipeline",
                        SourceType.DATABASE,
                        "/local/output",
                        new ScheduleConfig(60,true)
                )
                        .enableEncryption()
                        .build()
        );
    }


    @Test
    void oldProductDoesNotChangeWhenBuilderIsReused(){

        DataPipeline.Builder builder = new DataPipeline.Builder(
                "reusable",
                SourceType.FILE,
                "/data/out",
                new ScheduleConfig(60,true)
        );

        DataPipeline first = builder.batchSize(500).build();
        DataPipeline second = builder.batchSize(2000).build();

        assertEquals(500,first.getBatchSize());
        assertEquals(2000,second.getBatchSize());
    }
}

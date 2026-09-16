public class Main {

    public static void main(String[] args) {

        DataPipeline basic = PipelinePresets.basic();
        DataPipeline safe = PipelinePresets.safe();
        DataPipeline performance = PipelinePresets.performance();

        System.out.println("BASIC:");
        System.out.println(basic);

        System.out.println("\nSAFE:");
        System.out.println(safe);

        System.out.println("\nPERFORMANCE:");
        System.out.println(performance);
    }
}


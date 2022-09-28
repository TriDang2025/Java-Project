public class Main {

    public static void main(String[] args) {
        System.out.println(ThreadColor.ANSI_PURPLE + "Hello From the main thread.");

        Thread anotherThread = new AnotherThread();
        anotherThread.setName("== AnotherThread ==");
        anotherThread.start();

        new Thread(){
            public void run(){
                System.out.println(ThreadColor.ANSI_GREEN +"Hello from the anonymous class thread");
            }
        }.start();

        Thread myRunnableThread = new Thread(new MyRunnable()){
            @Override
            public void run() {
                System.out.println(ThreadColor.ANSI_RED + "Hello from the anonymous class of run");
                try {
                    anotherThread.join(2000);
                    System.out.println(ThreadColor.ANSI_RED + " AnotherThread terminated, so I'm running again.");
                } catch (InterruptedException e) {
                    System.out.println(ThreadColor.ANSI_RED + "I couldn't wait after all. I was interrupted");
                }
            }
        };
        myRunnableThread.start();

        System.out.println(ThreadColor.ANSI_PURPLE + "Hello again from the main thread");
    }

}

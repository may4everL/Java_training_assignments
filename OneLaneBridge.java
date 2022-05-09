package test;
import java.util.concurrent.*;

public class OneLaneBridge {
    public static void main(String[] args) {
        final Bridge bridge = new Bridge();  //constructs bridge calls semaphore from bridge
        Thread South = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Vehicle vehicle = new Vehicle(bridge);
                    Thread a = new Thread(vehicle);
                    vehicle.setName("South Vehicle: "+a.getId());  //sets vehicle name to South Vehicle and gets the thread ID
                    a.start();
                    try {
                        TimeUnit.SECONDS.sleep((long)(Math.random()*10));
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread North = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Vehicle vehicle = new Vehicle(bridge);
                    Thread a = new Thread(vehicle);
                    vehicle.setName("North Vehicle:  "+a.getId());  //sets vehicle name to North Vehicle and gets the thread ID
                    a.start();
                    try {
                        TimeUnit.SECONDS.sleep((long)(Math.random()*10));
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        South.start();  //starts south thread
        North.start();  //starts north thread
    }
}

class Bridge {
    private final Semaphore sema;  //calls Semaphore from Java.lang.*.  Semaphore guards against entry by more than N threads at a time.  Sends signals between two threads.

    public Bridge() {
        sema = new Semaphore(1);  //Constructs the semaphore. The semaphore controls access.  Allows
    }

    public void crossBridge(Vehicle vehicle) {
        try {
            System.out.printf("%s is WAITING to cross the bridge.\n",vehicle.getName());  //takes next argument and output the string
            sema.acquire();  //Acquires a permit and blocks until permit is released.
            System.out.printf("%s is CROSSING the bridge.\n",vehicle.getName());  //takes next argument and prints to string
            long duration = (long)(Math.random()*10);
            TimeUnit.SECONDS.sleep(duration);
        } catch(InterruptedException e) {   //prints the stack trace of the Exception to System.err.  Helps diagnose Exception.
            e.printStackTrace();
        } finally {
            System.out.printf("%s has FINISHED CROSSING the bridge.\n",vehicle.getName());  //takes next argument and prints to string
            sema.release();  //permit is released
        }
    }
}

class Vehicle implements Runnable {
    //private variables
    private String name;
    private Bridge bridge;
    public Vehicle(Bridge bridge) {
        this.bridge = bridge;  //assigns value of parameter bridge to variable with same name
    }
    //method that is implemented when there is a Runnable interface
    public void run()
    {
        bridge.crossBridge(this);
    }
    //get method for vehicle name
    public String getName()
    {
        return name;
    }
    //set method for vehicle name
    public void setName(String name)
    {
        this.name = name;
    }
}
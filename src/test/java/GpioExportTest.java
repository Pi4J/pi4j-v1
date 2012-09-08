import com.pi4j.wiringpi.GpioUtil;

public class GpioExportTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO export test program");
        
        // set GPIO 4 as the input trigger 
        GpioUtil.export(24, GpioUtil.DIRECTION_IN);
        System.out.println("GPIO 24 is now exported");

        System.out.println("GPIO 24 is exported ? : "+ GpioUtil.isExported(24));
        System.out.println("GPIO 24 edge ? : "+ GpioUtil.getDirection(24));
        
        System.out.println("GPIO 24 edge = 'none'");
        GpioUtil.setEdgeDetection(24,GpioUtil.EDGE_NONE);        
        System.out.println("GPIO 24 edge ? : "+ GpioUtil.getEdgeDetection(24));
        Thread.sleep(5000);
        
        System.out.println("GPIO 24 edge = 'both'");
        GpioUtil.setEdgeDetection(24,GpioUtil.EDGE_BOTH);        
        System.out.println("GPIO 24 edge ? : "+ GpioUtil.getEdgeDetection(24));
        Thread.sleep(5000);
        
        System.out.println("GPIO 24 edge = 'rising'");
        GpioUtil.setEdgeDetection(24,GpioUtil.EDGE_RISING);        
        System.out.println("GPIO 24 edge ? : "+ GpioUtil.getEdgeDetection(24));
        Thread.sleep(5000);
        
        System.out.println("GPIO 24 edge = 'falling'");
        GpioUtil.setEdgeDetection(24,GpioUtil.EDGE_FALLING);        
        System.out.println("GPIO 24 edge ? : "+ GpioUtil.getEdgeDetection(24));
        Thread.sleep(5000);

        System.out.println("GPIO 24 direction = 'out'");
        GpioUtil.setDirection(24, GpioUtil.DIRECTION_OUT);     
        System.out.println("GPIO 24 direction ? : "+ GpioUtil.getDirection(24));
        Thread.sleep(5000);

        System.out.println("GPIO 24 direction = 'in'");
        GpioUtil.setDirection(24, GpioUtil.DIRECTION_IN);     
        System.out.println("GPIO 24 direction ? : "+ GpioUtil.getDirection(24));
        Thread.sleep(5000);
        
        GpioUtil.unexport(24);
        System.out.println("GPIO 24 is now unexported");
        
        System.out.println("GPIO 24 is exported ? : "+ GpioUtil.isExported(24));        
    }
}


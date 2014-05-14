import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.lang.String.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

class server
{
	// Setting GPIO pin 01, 02 , 03 , 04 , 06 for motor control. Remember to install  Pi4J
	final static GpioController gpio= GpioFactory.getInstance(); // GPIO Controller 
	final static GpioPinDigitalOutput enable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "enable", PinState.HIGH);
	final static GpioPinDigitalOutput left_motor1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "left_motor1");     
	final static GpioPinDigitalOutput left_motor2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "left_motor2");
	final static GpioPinDigitalOutput right_motor1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "right_motor1");
	final static GpioPinDigitalOutput right_motor2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "right_motor2");
	
	public static void main(String str[])
	{

	String message;
	String reply;

	try
	{
  		ServerSocket ss=new ServerSocket(8888);
		Socket s;
		System.out.println("waiting for client connection"); // For debug purpose
		BufferedReader in;
		DataOutputStream out;
		
		while(true)
		{
			s=ss.accept();
			in= new BufferedReader(new InputStreamReader(s.getInputStream()));
			out= new DataOutputStream(s.getOutputStream());
			
			System.out.println("client connected");	 // For debug purpose
			message=in.readLine();
			if(!message.equals(null))
			{
				System.out.println("Android : "+ message); // For debug purpose
	
				reply= toPin(message);
				System.out.println("R Pi : "+ reply); // For debug purpose
				out.writeUTF(reply);
			}	
		}

	}
	catch(Exception e)
	{ 
		System.out.println("Error : "+e);
	}

	}

	static String toPin(String command) throws InterruptedException
	{

	
		switch(command)
		{
			case "left" : left_motor1.low();
				      left_motor2.high();
				      right_motor1.high();
				      right_motor2.low();
				      return("success");

			case "right" :left_motor1.high();
				      left_motor2.low();
				      right_motor1.low();
				      right_motor2.high();
				      return("success");

			case "up" :   left_motor1.high();
				      left_motor2.low();
				      right_motor1.high();
				      right_motor2.low();
				      return("success");
			
			case "down" : left_motor1.low();
				      left_motor2.low();
				      right_motor1.low();
				      right_motor2.low();
				      return("success");
 
			default : return("fail");

		}		
	}
}
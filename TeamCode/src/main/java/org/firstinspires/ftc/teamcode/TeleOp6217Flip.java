/* Team 6217
    The Fellowship
    2016 - 2017 Velocity Vortex Robot Code */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.abs;

@TeleOp(name="Preciousss: TeleOp6217Flip_17:00", group="Preciousss")

public class TeleOp6217Flip extends OpMode
{
/*FR = Front Right Wheel, FL = Front Left Wheel, BR = Back Righ Wheelt, BL = Back Left Wheel, Con1= Conveyor 1, Con2= Conveyor 2*/
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor motorConL;
    DcMotor motorConR;
    DcMotor motorFlip;


   /* IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;*/

    //static final double     COUNTS_PER_MOTOR_REV    = 420 ; // PPR for NeveRest 400
    //static final double     limitRocker = COUNTS_PER_MOTOR_REV / 4 ; // 90 degrees
    private ElapsedTime     runtime = new ElapsedTime();

    private boolean RunLauncher = false;
    private int DelayCounter = 0;
    private int DelayTimer = 400 ;
    public TeleOp6217Flip() {}

    @Override
    public void init()
    {
      /*
       * Wheels: controller 1, motors 0,1,2,3
       */
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.FORWARD);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.FORWARD);

        // Conveyor

        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);


        // Rocker Motors

        motorFlip = hardwareMap.dcMotor.get("motorFlip");
        motorFlip.setDirection(DcMotor.Direction.FORWARD);


    }

    /*
    * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */
    @Override
    public void init_loop() {
        // Start wrist in stowed position
        //servoWrist.setPosition(.85);
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */


    @Override
    public void loop() {

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Read values from Game Controller
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        // Stick values range from -1 to 1, where -1 is full up, and 1 is full down
        // Trigger values range from 0 to 1

        /* posx = Position X on Left Joystick, posy = Position Y on Left Joystick, posxR = Position X on Right Joystick,
        posyR = Position Y on Right Joystick, LT = Left Trigger, RT = Right Trigger, a = Button a, y = Button y, x = Button x,
         b = Button b, UP = on Dpad Loads Glyph, DOWN = on Dpad Shoots Glyph, rightPad = Right on Dpad, leftPad = Left on Dpad */
        float FLBRPower = 0.f;
        float FRBLPower = 0.f;
        float posx = gamepad1.left_stick_x;
        float posy = gamepad1.left_stick_y;
        float posyFlip = gamepad1.right_stick_y;
        float posxFlip = gamepad1.right_stick_x;
        float LT = gamepad1.left_trigger;
        float RT = gamepad1.right_trigger;
        boolean a = gamepad1.a;
        boolean b = gamepad1.b;
        boolean y = gamepad1.y;
        boolean x = gamepad1.x;
        boolean rightPad = gamepad1.dpad_right;
        boolean leftPad = gamepad1.dpad_left;
        boolean dpad_up = gamepad1.dpad_up;
        boolean dpad_down = gamepad1.dpad_down;
         /* ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Limit x and y values and adjust to power curve
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        posx = Range.clip(posx, -1, 1);
        posy = Range.clip(posy, -1, 1);
        posxFlip = Range.clip(posxFlip, -1, 1);
        posyFlip = Range.clip(posyFlip, -1, 1);

        posx = (float) powerCurve(posx);
        posy = (float) powerCurve(posy);
        posxFlip = (float) pCurve(posxFlip);
        posyFlip = (float) pCurve(posyFlip);


        LT = (float) powerCurve(LT);
        RT = (float) powerCurve(RT);

        //  Loading Glyphs
        // Directions will require testing
        if (dpad_up && !b && !x) {
            motorConL.setPower(.7);
            motorConR.setPower(-.7);

        } else if (dpad_down && ! b && !x ) {
            motorConL.setPower(-.7);
            motorConR.setPower(.7);

        } else {
            motorConL.setPower(0);
            motorConR.setPower(0);

        }

        // Rocker (for verticle flip, use negative power)
        if (posyFlip <= -.2 || posyFlip >= .2) {
            motorFlip.setPower(-(posyFlip *0.5));
        } else {
            motorFlip.setPower(0);
        }

        //  pivot left
        if (LT != 0)  {

            motorFL.setPower(1);
            motorBL.setPower(1);
            motorFR.setPower(-1);
            motorBR.setPower(-1);
        }


        //  pivot right
        else if (RT != 0)  {

            motorFL.setPower(-1);
            motorBL.setPower(-1);
            motorFR.setPower(1);
            motorBR.setPower(1);
        }



        /*if (b && leftPad ){
            servoThumb.setPosition(0);
        }
        else if (b && rightPad){
            servoThumb.setPosition(1);
        } */
        if(b && dpad_up){
            //servoWrist.setPosition(1);
        }
        else if (b && dpad_down){
            //servoWrist.setPosition(.4);
        }

        //  Driving
        if ( ( posy != 0) || ( posx != 0 ) ) {

            FRBLPower = (-posy) - posx;
            FLBRPower = (-posy) + posx;
            motorFR.setPower( FRBLPower );
            motorFL.setPower( FLBRPower );
            motorBR.setPower( FLBRPower );
            motorBL.setPower( FRBLPower );

        }

        else {

            motorFR.setPower(0);
            motorFL.setPower(0);
            motorBR.setPower(0);
            motorBL.setPower(0);
        }

        // CR Servo Bag Arm


        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Write telemetry back to driver station
            NOTE: Output is sorted by the first field, hence the numbering
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */


        telemetry.addData("1", "Power, FRBL/FLBR: " +
                String.format("%.2f", FRBLPower) + " " +
                String.format("%.2f", FLBRPower));

        telemetry.addData("2", "Raw Joystick X, Y: " +
                String.format("%.2f", gamepad1.left_stick_x) + " " +
                String.format("%.2f", gamepad1.left_stick_y));

        telemetry.addData("3", "D-Pad U, R, D, L: " +
                String.format("%b", gamepad1.dpad_up) + " " +
                String.format("%b", gamepad1.dpad_right) + " " +
                String.format("%b", gamepad1.dpad_down) + " " +
                String.format("%b", gamepad1.dpad_left));

        telemetry.addData("0", "Trigger: " +
                String.format("%.2f", gamepad1.left_trigger));
        telemetry.addData("0", "Trigger: " +
                String.format("%.2f", gamepad1.right_trigger));
        telemetry.addData("4", "Right Bumper Left Bumper" +
                String.format("%b", gamepad1.right_bumper)+ " " +
                String.format("%b", gamepad1.left_bumper));

        telemetry.addData("5", "Actuator A, B,: " +
                String.format("%b", gamepad1.a) + " " +
                String.format("%b", gamepad1.b));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
    }

   double powerCurve ( float dVal ) {
    /*
     * This method adjusts the joystick input to apply a power curve that
     * is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    // LB = left bumper, RB = right bumper.
       boolean LB = gamepad1.left_bumper;
       boolean RB = gamepad1.right_bumper;


       double dead = .2;

       // Regular
       double max = .5;
       if (LB == true && RB == false){
           // Turbo with left bumper
           max = 1;
       } else if (LB == false && RB == true){
           // Turtle with right bumper
           max = .3;
       }

       int direction = 1;
       if (dVal < 0) {
           direction = -1;
       }

       double dScale = 0.0;
       if (abs(dVal) < dead) {
           dScale = 0.0;
       } else {
           dScale = ((dVal * dVal) * max) * direction;
       }

       return (dScale);
   }

    double pCurve ( float dVal ) {
    /*
     * This method adjusts the joystick input to apply a power curve that
     * is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    // LB = left bumper, RB = right bumper.



        double dead = .2;

        // Regular
        double max = 1;

        int direction = 1;
        if (dVal < 0) {
            direction = -1;
        }

        double dScale = 0.0;
        if (abs(dVal) < dead) {
            dScale = 0.0;
        } else {
            dScale = ((dVal * dVal) * max) * direction;
        }

        return (dScale);
    }
}

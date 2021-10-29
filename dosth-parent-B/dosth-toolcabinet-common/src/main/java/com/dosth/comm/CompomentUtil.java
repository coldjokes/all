package com.dosth.comm;

import java.util.regex.Pattern;

public class CompomentUtil {

//	private static final String uiShiftRegex = "^Shift,([0-9]*)+(,[0-9]*-[0-9]*)+$";
//	public static final Pattern uiShiftCmdPatt = Pattern.compile(uiShiftRegex);
//	
//	private static final String liftMoveRegex = "^Lift,[0-9]*$";
//	public static final Pattern liftMoveCmdPatt = Pattern.compile(liftMoveRegex);
	
	private static final String motorErrRegex = "^[0-9]*,[0-9]*,Motor_Exception$";
	public static final Pattern motorErrPatt = Pattern.compile(motorErrRegex);
	
	public static final String One_Motor_Work_Done = "One_Motor_Work_Done";
	public static final String All_Motors_On_Board_Work_Done = "All_Motors_On_Board_Work_Done";
	public static final String Motor_Board_PLC_Conn_Exception = "Motor_Board_PLC_Conn_Exception";
	public static final String Motor_Board_NO_IP_MATCH_ROW_Exception = "Motor_Board_NO_IP_MATCH_ROW_Exception";
	public static final String Motor_Response_Exception = "Motor_Response_Exception";
	public static final String Already_Taken_Out_All_Row = "Already_Taken_Out_All_Row";
	public static final String Lift_Arrive_At_Door = "Lift_Arrive_At_Door";
	public static final String Open_Door = "Open_Door";
	public static final String Succ_To_Open_Door = "Succ_To_Open_Door";
	public static final String Fail_To_Open_Door = "Fail_To_Open_Door";
	public static final String Lift_Response_Arrived = "Lift_Response_Arrived";
	public static final String Lift_Response_Exception = "Lift_Response_Exception";
	public static final String Return_Starting_Scan = "Return_Starting_Scan";
	public static final String Appointment_Starting_Scan = "Appointment_Starting_Scan";
	public static final String Appointment_Cmd = "Appointment_Cmd";
}
package com.jintoufs.logstics.utils;

import com.fntech.Loger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;

/**
 * 中正指纹验证
 * Created by Administrator on 2017/7/10 0010.
 */

public class FingerUtil {
    //static private boolean openFlag=false;
    static private SerialPort sp = null;//File device, int baudrate, int databit,int stopbit,int parity
    static public boolean enableLog = true;

    static public void open(String serialPortName, int baudrate) {
        if (sp == null)//|| openFlag==false
        {
            try {
                //sp=new SerialPort(new File("/dev/ttyHSL0"),57600,8,1,0);
                sp = new SerialPort(new File(serialPortName), baudrate, 0);
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                sp = null;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                sp = null;
                e.printStackTrace();
            }
        }
        //openFlag=true;
    }

    static public void close() {
        if (sp != null) {
            sp.close();
            sp = null;
            //openFlag=false;
        }
    }

    static public Result collectFingerTemplet() {
        Result result = new Result();
        if (sp == null) {
            result.code = -101;
            result.value = "串口模块未打开";
            return result;
        }
        if (FingerUtil.DetectFinger()) {
            if (FingerUtil.GetImage()) {
                if (FingerUtil.GenTemplet()) {
                    String text = FingerUtil.UpTemplet();
                    if (text.length() > 0) {
                        result.code = 0;
                        result.value = text;
                    } else {
                        result.code = -4;
                        result.value = "UpTemplet失败";
                    }
                } else {
                    result.code = -3;
                    result.value = "GenTemplet失败";
                }
            } else {
                result.code = -2;
                result.value = "GetImage失败";
            }
        } else {
            result.code = -1;
            result.value = "DetectFinger失败";
        }
        return result;
    }

    static public Result compareFinger(String fingerTemplet1, String fingerTemplet2) {
        Result result = new Result();

        if (fingerTemplet1.length() == 0 || fingerTemplet2.length() == 0) {
            result.code = -100;
            result.value = "对比指纹不能为空";
            return result;
        }
        if (sp == null) {
            result.code = -101;
            result.value = "串口模块未打开";
            return result;
        }

        if (FingerUtil.DownTemplet(fingerTemplet1, 1)) {
            if (FingerUtil.CheckDownStatus()) {
                if (FingerUtil.DownTemplet(fingerTemplet2, 2)) {
                    if (FingerUtil.CheckDownStatus()) {
                        int rr = FingerUtil.MatchTwoTemplet();
                        if (rr >= 0) {
                            result.code = 0;
                            result.value = "" + rr;
                        } else {
                            if (rr == -8) {
                                result.code = -8;
                                result.value = "指纹不匹配";
                            } else if (rr == -1) {
                                result.code = -1;
                                result.value = "包有错误";
                            } else if (rr == -2) {
                                result.code = -2;
                                result.value = "通信异常";
                            }
                        }
                    } else {
                        result.code = -5;
                        result.value = "指纹特征2 CheckDownStatus失败";
                    }
                } else {
                    result.code = -4;
                    result.value = "指纹特征2 DownTemplet失败";
                }
            } else {
                result.code = -3;
                result.value = "指纹特征1 CheckDownStatus失败";
            }
        } else {
            result.code = -2;
            result.value = "指纹特征1 DownTemplet失败";
        }
        return result;
    }

    static private void disk_log(String caption, String log, String moduleName) {
        if (enableLog) {
            Loger.disk_log(caption, log, moduleName);
        }
    }

    static private void disk_log(String caption, byte[] buffer, String moduleName) {
        if (enableLog) {
            Loger.disk_log(caption, buffer, moduleName);
        }
    }

    static private void disk_log(String caption, byte[] buffer, int buffer_len, String moduleName) {
        if (enableLog) {
            Loger.disk_log(caption, buffer, buffer_len, moduleName);
        }
    }

    static private int listLen(List<byte[]> list) {
        int len = 0;
        for (int i = 0; i < list.size(); i++) {
            len += list.get(i).length;
        }
        return len;
    }

    static private void listAdd(List<byte[]> list, byte[] arr, int arr_len) {
        byte[] data = new byte[arr_len];
        System.arraycopy(arr, 0, data, 0, arr_len);
        list.add(data);
    }

    static private byte[] listToArr(List<byte[]> list) {
        int len = 0;
        for (int i = 0; i < list.size(); i++) {
            len += list.get(i).length;
        }
        byte[] data = new byte[len];
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            byte[] item = list.get(i);
            System.arraycopy(item, 0, data, count, item.length);
            count += item.length;
            //System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        }
        return data;
    }

    static private String decode(String data) {
        int len = data.length();
        String temp = "";
        for (int i = 0; (i * 2) < len; i++) {
            if (temp.length() == 0) {
                temp += data.substring(i * 2, i * 2 + 2);
            } else {
                temp += "," + data.substring(i * 2, i * 2 + 2);
            }
        }
        temp = temp.toUpperCase();
        temp = temp.replace("DB,DC", "C0");
        temp = temp.replace("DB,DD", "DB");
        temp = temp.replace(",", "");
        return temp;
    }

    static private String encode(String data) {
        int len = data.length();
        String temp = "";
        for (int i = 0; (i * 2) < len; i++) {
            if (temp.length() == 0) {
                temp += data.substring(i * 2, i * 2 + 2);
            } else {
                temp += "," + data.substring(i * 2, i * 2 + 2);
            }
        }
        temp = temp.toUpperCase();
        temp = temp.replace("DB", "DB,DD");
        temp = temp.replace("C0", "DB,DC");
        temp = temp.replace(",", "");
        return temp;
    }

    static private Ack recv(boolean recv_act_data) {
        Ack ackResult = new Ack();
        ackResult.code = -1;
        final byte[] ack = new byte[512];
        final Result ackCount = new Result();
        ackCount.code = 0;
        final InputStream is = sp.getInputStream();
        final Object obj = new Object();
        final boolean recv_act_data_bak = recv_act_data;
        final List<byte[]> dataRecv = new ArrayList<byte[]>();
        //final List<byte[]> dataRecvAckData=new ArrayList<byte[]>();
        final Result result_code = new Result();
        result_code.code = -1;
        final Result exitThread = new Result();
        exitThread.code = 0;
        Thread tRecv = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                int k = 0;
                byte[] buff = new byte[512];
                int packetLen = 0;
                int from = 0;
                boolean recv_ack_ok = false;
                while (true) {
                    if (exitThread.code == 1)
                        break;
                    try {
                        disk_log("finger begin read,tid" + Thread.currentThread().getId(), "", "t3_zz");
                        int recv = 0;
                        recv = is.read(buff);

								/*
                                if(k==1)
								{
									 //buff=Tools.HexString2Bytes("301F5F4C311EFF0A321D5E14321EDF593207DF000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EFAC0");
									buff=Tools.HexString2Bytes("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005BEC0");


									 recv=buff.length;
									 k++;
								}

								if(k==0)
								{
									 //buff=Tools.HexString2Bytes("C007000000000001000008C0C0020000000000800302361F0000800F000F000F000F000F000F000F000F000F000F000F000F000FFFFFFFFFFFFFFFFFFFFFFFFF000000000000000000000000590321FF39081E9F3C0A07BF5E0A1BDF060B067E510B1C7F350C1E7F3E0E1DDF560F057E5E131C3E5D18859F4C1A84DF1C1C1DDF101F061F5920869F4222865F492285BF3D2406FF2346C0C008000000000080282488DF0C249B9E2427203F3128077F52291C3F4D2B06BF162B071E0D2E869E42");
									 buff=Tools.HexString2Bytes("C007000000000001000008C0C00200000000008003025B160000FFFFDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FDBDC0FFFFFFFFFFFFF00000000000000000000000049069CBE36091B7E3D161B9E4C16855E50171B9E291F1ADE45229D5F20241A3F5224871F1A29191F122D169E242E1A7E3A2E87DE1C3319BF5D35079F1438149F29381D5E163B085E2875C0C008000000000080463C891F213E1DDE153F07FF2E3F0CDF0000");

									recv=buff.length;
									 k++;
								}

								if(k==100)
								{
									recv=is.read(buff);
								}
								*/

                        //recv=buff.length;
                        //if(recv<0)
                        //recv=is.read(buff);
                        if (recv > 0) {
                            disk_log("finger read", buff, recv, "t3_zz");

                            listAdd(dataRecv, buff, recv);
                            //int total=listLen(dataRecv);
                            byte[] list = listToArr(dataRecv);

                            list = listToArr(dataRecv);
                            list = Tools.HexString2Bytes(decode(Tools.Bytes2HexString(list, list.length)));

                            byte[] list2 = new byte[list.length - from];//the data after 'from'
                            System.arraycopy(list, from, list2, 0, list2.length);
                            int total = list2.length;
                            disk_log("finger parse list2", list2, list2.length, "t3_zz");
                            if (total >= 8 && list2[0] == (byte) 0xC0) {
                                packetLen = ((list2[6] << 0x8) | (list2[7] & 0x00ff)) & 0x0000ffff;
                                if (packetLen + 11 == total)//ok
                                {
                                    disk_log("finger parse", "ok", "t3_zz");
                                    //??
                                    from += packetLen + 11;
                                    if (recv_ack_ok == false) {
                                        System.arraycopy(list2, 0, ack, 0, packetLen + 11);
                                        ackCount.code = packetLen + 11;
                                        recv_ack_ok = true;
                                        disk_log("finger parse...1", "", "t3_zz");
                                    }
                                    if (recv_act_data_bak == false) {
                                        result_code.code = 0;
                                        break;
                                    } else {
                                        if (list2[1] == 0x8)//end packet,???
                                        {
                                            disk_log("finger parse...2", "", "t3_zz");
                                            result_code.code = 0;
                                            break;
                                        } else {
                                            disk_log("finger parse...3", "", "t3_zz");
                                            continue;
                                        }
                                    }
                                } else if (packetLen + 11 > total) {
                                    disk_log("finger parse", ">>>> packetLen+11>total", "t3_zz");
                                    continue;
                                } else if (packetLen + 11 < total) {
                                    disk_log("finger parse", "<<<< packetLen+11>total", "t3_zz");
                                    if (recv_ack_ok == false) {
                                        System.arraycopy(list2, 0, ack, 0, packetLen + 11);
                                        ackCount.code = packetLen + 11;
                                        recv_ack_ok = true;
                                        disk_log("finger parse...4", "", "t3_zz");
                                    }
                                    if (recv_act_data_bak == false) {
                                        //here could be a error!!!
                                        disk_log("finger parse...5err", "", "t3_zz");
                                        result_code.code = 0;
                                        break;
                                    } else {
                                        if (list2[1] == 0x8)//end packet,???
                                        {
                                            //here could be a error!!!
                                            disk_log("finger parse...6err", "", "t3_zz");
                                            result_code.code = 0;
                                            break;
                                        }
                                    }
                                    from += packetLen + 11;

                                    list2 = new byte[list.length - from];//the data after 'from'
                                    System.arraycopy(list, from, list2, 0, list2.length);
                                    total = list2.length;
                                    disk_log("finger parse list2", list2, list2.length, "t3_zz");

                                    if (recv_act_data_bak == false) {
                                        //here could be a error!!!
                                        disk_log("finger parse...7err", "", "t3_zz");
                                        result_code.code = 0;
                                        break;
                                    } else {
                                        while (true) {
                                            if (total >= 8 && list2[0] == (byte) 0xC0) {
                                                packetLen = ((list2[6] << 0x8) | (list2[7] & 0x00ff)) & 0x0000ffff;
                                                if (packetLen + 11 == total) {
                                                    if (list2[1] == 0x8)//end packet
                                                    {
                                                        disk_log("finger parse...8", "", "t3_zz");
                                                        result_code.code = 0;
                                                        break;
                                                    } else {
                                                        from += packetLen + 11;
                                                        list2 = new byte[list.length - from];//the data after 'from'
                                                        System.arraycopy(list, from, list2, 0, list2.length);
                                                        total = list2.length;
                                                    }
                                                } else if (packetLen + 11 > total) {
                                                    break;
                                                } else if (packetLen + 11 < total) {
                                                    if (list2[1] == 0x8)//end packet
                                                    {
                                                        //err??
                                                        disk_log("finger parse...9err", "", "t3_zz");
                                                        result_code.code = 0;
                                                        break;
                                                    } else {
                                                        from += packetLen + 11;
                                                        list2 = new byte[list.length - from];//the data after 'from'
                                                        System.arraycopy(list, from, list2, 0, list2.length);
                                                        total = list2.length;
                                                    }
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        if (result_code.code == 0) {
                                            break;
                                        }

                                        //go parse
                                                 /*if(total>=8 && list2[0]==(byte)0xC0)
												 {


													 disk_log("finger parse...8", "", "t3_zz");
													 packetLen=((list2[6]<<0x8)|list2[7]) & 0x0000ffff;
													 if(packetLen+11==total && list2[1]==0x8)//end packet
													 {
														 disk_log("finger parse...9", "", "t3_zz");
														 result_code.code=0;
														 break;
													 }
													 else
													 {
														 while(true)
														 {
															 if(packetLen+11<=total)
															 {
																 from+=packetLen+11;
																 list2=new byte[list.length-from];//the data after 'from'
																 System.arraycopy(list, from, list2, 0,list2.length);
																 total=list2.length;
																 if(total>=8 && list2[0]==(byte)0xC0)
																 {

																 }
																 else
																 {

																 }
																 disk_log("finger parse list2", list2,list2.length, "t3_zz");
															 }
															 else
															 {
																 break;
															 }
														 }
													 }
												 }
												 else
												 {
													 disk_log("finger parse...10", "", "t3_zz");
													 continue;
												 }
												 */

                                    }
                                }
                            }
                        }//if(recv>0)

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        disk_log("finger parse exception1", e.getMessage(), "t3_zz");
                    }
                }//while

					/*synchronized(obj)
					{
						obj.notify();
					}
					*/
            }
        });
        tRecv.start();


        try {
				/*synchronized(obj)
				{
					obj.wait(1000*5);
				}
				*/
            //exitThread.code=1;
            tRecv.join();

            if (result_code.code == 0) {
                try {
                    ackResult.code = 0;
                    ackResult.ack = new byte[ackCount.code];
                    System.arraycopy(ack, 0, ackResult.ack, 0, ackCount.code);
                    disk_log("finger parse...11", "", "t3_zz");
                    if (recv_act_data) {
                        disk_log("finger parse...12", "", "t3_zz");
                        byte[] list = listToArr(dataRecv);
                        list = Tools.HexString2Bytes(decode(Tools.Bytes2HexString(list, list.length)));
                        ackResult.ack_data = new byte[list.length - ackCount.code];
                        System.arraycopy(list, ackCount.code, ackResult.ack_data, 0, list.length - ackCount.code);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    disk_log("finger parse exception2", e.getMessage(), "t3_zz");
                    ackResult.code = -2;
                }

            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            disk_log("finger parse exception3", e.getMessage(), "t3_zz");
            e.printStackTrace();
        }

        return ackResult;

    }

    static private Ack sendRecv(byte[] cmd, byte[] cmd_data, boolean send_cmd_data, boolean recv_act_data) {
        OutputStream os = sp.getOutputStream();
        Ack ack = new Ack();
        ack.code = -1;
        try {
            os.write(cmd, 0, cmd.length);
            disk_log("finger write1", cmd, cmd.length, "t3_zz");
            ack = recv(recv_act_data);
            if (ack.code == 0) {
                disk_log("finger recv result1", ack.ack, ack.ack.length, "t3_zz");
                if (ack.ack_data != null) {
                    disk_log("finger recv result2", ack.ack_data, ack.ack_data.length, "t3_zz");
                }
                if (send_cmd_data) {
                    os.write(cmd_data, 0, cmd_data.length);
                    disk_log("finger write2", cmd, cmd.length, "t3_zz");
                    ack.code = 0;
                }
            } else {
                disk_log("finger recv result3", "error", "t3_zz");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ack;
    }


    static private boolean DetectFinger() {
        //01H 00H x 4 0001H 01H 0003H
        String cmdString = "C001000000000001010003C0";
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {

            disk_log("DetectFinger", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            return ack.ack[8] == 0;
        }
        return false;
    }


    static private boolean GetImage() {
        //01H 00H x 4 0001H 02H 0004H
        String cmdString = "C001000000000001020004C0";
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {

            disk_log("GetImage", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            return ack.ack[8] == 0;
        }
        return false;
    }


    static private boolean GenTemplet() {
        //01H 00H x 4 0002H 03H BufferID sum
        String cmdString = "C00100000000000203010007C0";//bufferA
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {

            disk_log("GenTemplet", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            return ack.ack[8] == 0;
        }
        return false;
    }

    static private String UpTemplet() {
        String finger_str = "";
        String cmdString = "C0010000000000020901000DC0";//bufferA
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, true);
        if (ack.code == 0) {
            disk_log("UpTemplet", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            if (ack.ack_data != null)
                disk_log("UpTemplet", "ack_data=" + Tools.Bytes2HexString(ack.ack_data, ack.ack_data.length), "t3_zz");
            if (ack.ack_data != null) {
                try {
                    int from = 0;
                    int len = ack.ack_data.length;
                    List<byte[]> data = new ArrayList<byte[]>();
                    disk_log("UpTemplet,ack.ack_data", ack.ack_data, "t3_zz");
                    while (true) {
                        int packet_len = ((ack.ack_data[from + 6] << 8) | (ack.ack_data[from + 7] & 0x00ff)) & 0x0000ffff;
                        byte[] buff = new byte[packet_len];
                        System.arraycopy(ack.ack_data, from + 8, buff, 0, buff.length);
                        listAdd(data, buff, buff.length);
                        from += packet_len + 11;
                        if (from >= len) {
                            break;
                        }
                    }
                    byte[] finger = listToArr(data);
                    finger_str = Tools.Bytes2HexString(finger, finger.length);
                    //finger_str=decode(finger_str);
                    disk_log("UpTemplet", "finger_str=" + finger_str, "t3_zz");
                } catch (Exception e) {
                    disk_log("UpTemplet", "getMessage=" + e.getMessage(), "t3_zz");
                }
            }
            //return ack.ack[8]==0;
        }
        return finger_str;
    }


    static private boolean DownTemplet(String finger, int finger_no) {
        //01H 00H x 4 0002H 0aH BufferID sum
        String cmdString = "";//bufferA
        if (finger_no == 1)
            cmdString = "C0010000000000020A01000EC0";
        else if (finger_no == 2)
            cmdString = "C0010000000000020A02000FC0";
        else
            return false;
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }

        //finger=encode(finger);
        int j = finger.length() / 256 + (finger.length() % 256 > 0 ? 1 : 0);
        List<byte[]> list = new ArrayList<byte[]>();
        //包标识02, 设备地址码,包长度,数据,校验和
        //包标识08, 设备地址码,包长度,数据,校验和
        for (int i = 0; i < j; i++) {
            String temp = "";
            if (i == j - 1) {
                temp = finger.substring(i * 256, finger.length());

                String len = Tools.Bytes2HexString(new byte[]{(byte) (temp.length() / 2)}, 1);
                len = "00" + len;
                byte[] arr_temp = Tools.HexString2Bytes("0800000000" + len + temp);
                int sum = 0;
                for (byte b : arr_temp) {
                    sum += (b & 0x00ff);
                }
                byte[] sum_arr = new byte[]{(byte) ((sum >> 0x8) & 0x00ff), (byte) ((sum >> 0x0) & 0x00ff)};
                list.add(Tools.HexString2Bytes("C0" + encode("0800000000" + len + temp + Tools.Bytes2HexString(sum_arr, sum_arr.length)) + "C0"));
            } else {
                temp = finger.substring(i * 256, i * 256 + 256);
                byte[] arr_temp = Tools.HexString2Bytes("0200000000" + "0080" + temp);
                int sum = 0;
                for (byte b : arr_temp) {
                    sum += (b & 0x00ff);
                }
                byte[] sum_arr = new byte[]{(byte) ((sum >> 0x8) & 0x00ff), (byte) ((sum >> 0x0) & 0x00ff)};
                list.add(Tools.HexString2Bytes("C0" + encode("0200000000" + "0080" + temp + Tools.Bytes2HexString(sum_arr, sum_arr.length)) + "C0"));
            }
        }

        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {
            disk_log("DownTemplet", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            if (ack.ack[8] == 0) {
                OutputStream os = sp.getOutputStream();
                for (int i = 0; i < list.size(); i++) {
                    try {
                        byte[] arrWrite = list.get(i);
                        disk_log("DownTemplet write:", arrWrite, "t3_zz");
                        os.write(arrWrite);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        disk_log("DownTemplet IOException", e.getMessage(), "t3_zz");
                        return false;
                    }
                }
                return true;

            }
        }
        return false;
    }


    static private boolean CheckDownStatus() {
        //01H 00H x 4 0001H 31H 0033H

        //String cmdString = "C001000000000001310033C0";//bufferA

        String cmdString = "C001000000000001310033C0";//bufferA

        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {

            disk_log("CheckDownStatus", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            return ack.ack[8] == 0;
        }
        return false;
    }

    static private int MatchTwoTemplet() {
        //01H 00H x 4 0001H 04H 0006H
        String cmdString = "C001000000000001040006C0";
        if (cmdString.length() % 2 != 0) {
            disk_log("cmd error", cmdString, "t3_zz");
        }
        byte[] cmd = Tools.HexString2Bytes(cmdString);
        Ack ack = sendRecv(cmd, null, false, false);
        if (ack.code == 0) {
            //9,10
            disk_log("MatchTwoTemplet", ack.ack[8] + "_" + Tools.Bytes2HexString(ack.ack, ack.ack.length), "t3_zz");
            int score = ((ack.ack[9] << 8) | ack.ack[10]) & 0x0000ffff;
            if (ack.ack[8] == 0) {
                return score;
            } else {
                return 0 - ack.ack[8];
            }
        }
        return -2;
    }
}

package gym;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public  class Site {
    public final int workingDay1=30;//9~12
    public final int workingDay2=50;//12~18
    public final int workingDay3=80;//18~20
    public final int workingDay4=60;//20~22
    public final int offDay1=40;//9~12
    public final int offDay2=50;//12~18
    public final int offDay3=60;//18~22

    public int  calculateCost(String bookingDate,String bookTimeStart,String  bookTimeEnd,boolean breachOfContract) throws Exception{
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        long timeFrame1=format.parse("09:00").getTime();
        long timeFrame2=format.parse("12:00").getTime();
        long timeFrame3=format.parse("18:00").getTime();
        long timeFrame4=format.parse("20:00").getTime();
        int week=dayForWeek(bookingDate);
        long startTime=format.parse(bookTimeStart).getTime();
        long endTime=format.parse(bookTimeEnd).getTime();
        int inCome=0;
        if(week==7 || week==6){
            if(startTime>=timeFrame1&&startTime<timeFrame2){
                if (endTime<=timeFrame2){
                    inCome=(int)(endTime-startTime)/(1000*60*60)*offDay1;//9~12
                }else if(endTime>timeFrame2&&endTime<=timeFrame3){
                    inCome=(int)(timeFrame2-startTime)/(1000*60*60)*offDay1+(int)(endTime-timeFrame2)/(1000*60*60)*offDay2;//9~18
                }else {
                    inCome=(int)(timeFrame2-startTime)/(1000*60*60)*offDay1+6*offDay2+(int)(endTime-timeFrame3)/(1000*60*60)*offDay3;//9~22
                }
            }else if(startTime>=timeFrame2 &&startTime<timeFrame3){
                if(endTime<=timeFrame3){
                    inCome=(int)(endTime-startTime)/(1000*60*60)*offDay2;//12~18
                }else {
                    inCome=(int)(timeFrame3-startTime)/(1000*60*60)*offDay2+(int)(endTime-timeFrame3)/(1000*60*60)*offDay3;//12~22
                }
            }else {
                inCome=(int)(endTime-startTime)/(1000*60*60)*offDay3;//18~22
            }
            if(breachOfContract){
                inCome=inCome/4;
            }
        }else{
            if(startTime>=timeFrame1&&startTime<timeFrame2){
                if(endTime<=timeFrame2){
                    inCome=(int) (endTime-startTime)/(1000*60*60)*workingDay1;//9~12
                }else if(endTime>timeFrame2&&endTime<=timeFrame3){
                    inCome=(int)(timeFrame2-startTime)/(1000*60*60)*workingDay1+(int)(endTime-timeFrame2)/(1000*60*60)*workingDay2;//9~18
                }else if(endTime>timeFrame3&&endTime<=timeFrame4){
                    inCome=(int)(timeFrame2-startTime)/(1000*60*60)*workingDay1+6*workingDay2+(int)(endTime-timeFrame3)/(1000*60*60)*workingDay3;//9~20
                }else {
                    inCome=(int)(timeFrame2-startTime)/(1000*60*60)*workingDay1+6*workingDay2+2*workingDay3+(int)(endTime-timeFrame4)/(1000*60*60)*workingDay4;//9~22
                }

            }else if(startTime>=timeFrame2 &&startTime<timeFrame3){
                if(endTime<=timeFrame3){
                    inCome=(int) (endTime-startTime)/(1000*60*60)*workingDay2;//12~18
                }else if(endTime>timeFrame3&&endTime<=timeFrame4){
                    inCome=(int)(timeFrame3-startTime)/(1000*60*60)*workingDay2+(int)(endTime-timeFrame3)/(1000*60*60)*workingDay3;//12~20
                }else {
                    inCome=(int)(timeFrame3-startTime)/(1000*60*60)*workingDay2+2*workingDay3+(int)(endTime-timeFrame4)/(1000*60*60)*workingDay4;//12~22
                }

            }else if(startTime>=timeFrame3&&startTime<timeFrame4){
                if(endTime<=timeFrame4){
                    inCome=(int) (endTime-startTime)/(1000*60*60)*workingDay3;//18~20
                }else {
                    inCome=(int)(timeFrame4-startTime)/(1000*60*60)*workingDay3+(int)(endTime-timeFrame4)/(1000*60*60)*workingDay4;//18~22
                }

            }else {
                    inCome=(int)(endTime-startTime)/(1000*60*60)*workingDay4;//20~22
            }
            if(breachOfContract){
                inCome=inCome/2;
            }
        }

        return inCome;

    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

}

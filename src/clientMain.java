import gym.OrderDetails;
import gym.Site;
import tool.SortClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class clientMain {

    public static void main(String[] args) throws Exception{
        Map<String,List<String>>orders=new HashMap<>();//场馆使用
        Map<String,List<OrderDetails>> ordersMap=new HashMap<>();//订单详情
        Map<String,String> customer=new HashMap<>();
        List<OrderDetails> odList=null;
        List<String> listTime=new ArrayList<>();
        ordersMap.put("A",odList);
        ordersMap.put("B",odList);
        ordersMap.put("C",odList);
        ordersMap.put("D",odList);
        Scanner sc= new Scanner(System.in);
        while(true) {
            String st = sc.nextLine();
            if(st.equals("exit")) break;
            if(st.equals(" ")){
                System.out.println("收入汇总");
                System.out.println("-------");
                int total=0;
                for (String key : ordersMap.keySet()) {
                    System.out.println("场地："+key);
                    int subtotal=0;
                    if(ordersMap.get(key)!=null){
                        List<OrderDetails> readOrder=ordersMap.get(key);
                        SortClass sortClass=new SortClass();
                        Collections.sort(readOrder,sortClass);//按时间排序
                        for (OrderDetails orderDetails :readOrder){
                            subtotal+=orderDetails.getInCome();
                            System.out.print(orderDetails.getTimePeriod()+" ");
                            if (orderDetails.isBreachOfContract()){
                                System.out.println("违约金 "+orderDetails.getInCome()+"元");
                            }else{
                                System.out.println(orderDetails.getInCome()+"元");
                            }
                        }
                    }
                    total+=subtotal;
                    System.out.println("小计:"+subtotal+" 元");
                    System.out.println();
                }
                System.out.println("----");
                System.out.println("总计:"+total);
            }else{
                    Pattern p = Pattern.compile("\\w{3,5}+[ ]\\d{4}+[-]\\d{1,2}+[-]\\d{1,2}+[ ]\\d{1,2}+[:]\\d{1,2}+[~]\\d{1,2}+[:]\\d{1,2}+[ ][A-D]");
                    Pattern p1 = Pattern.compile("\\w{3,5}+[ ]\\d{4}+[-]\\d{1,2}+[-]\\d{1,2}+[ ]\\d{1,2}+[:]\\d{1,2}+[~]\\d{1,2}+[:]\\d{1,2}+[ ][A-D][ ][C]+");
                    Matcher m = p.matcher(st);
                    Matcher m1 = p1.matcher(st);
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    if(m.matches()){
                        String[] input=st.split(" ");
                        String[] periodArr=input[2].split("~");
                        String startTime=input[1]+" "+periodArr[0];
                        String endTime=input[1]+" "+periodArr[1];
                        List<String>  period=orders.get(input[3]);
                        if(!wholePoint(periodArr[0]) || !wholePoint(periodArr[1])){
                            System.out.println("> Error:The booking is not the whole point!");
                        }else if(period!=null&&!RepeatedVerification(startTime,endTime,period)){
                            System.out.println("> Error:The booking conflicts with existing booking!");
                        }else if(!timePeriod(periodArr[0],periodArr[1])){
                            System.out.println(">Error: Not in business hours or the end time is earlier than the start time!");
                        }else{
                            listTime.add(startTime+"--"+endTime);
                            orders.put(input[3],listTime);
                            customer.put(input[0]+input[1]+input[2]+input[3],input[3]);
                            Site site=new Site();
                           int money=site.calculateCost(input[1],periodArr[0],periodArr[1],false);
                            if(ordersMap.get(input[3])!=null){
                                odList=ordersMap.get(input[3]);
                            }else {
                                odList=new ArrayList<>();
                            }
                           OrderDetails od=new OrderDetails();
                           od.setGymName(input[3]);
                           od.setInCome(money);
                           od.setStartTime(sdf.parse(startTime));
                           od.setTimePeriod(input[1]+" "+input[2]);
                           od.setBreachOfContract(false);
                           odList.add(od);
                           ordersMap.put(input[3],odList);
                           System.out.println("Success: the booking is accepted!");
                        }
                    }else if(m1.matches()){
                        String[] input=st.split(" ");
                        String[] periodArr=input[2].split("~");
                        String startTime=input[1]+" "+periodArr[0];
                        String endTime=input[1]+" "+periodArr[1];
                        if(!wholePoint(periodArr[0]) || !wholePoint(periodArr[1])){
                            System.out.println("> Error:The booking is not the whole point!");
                        }else if(!timePeriod(periodArr[0],periodArr[1])){
                            System.out.println(">Error: Not in business hours or the end time is earlier than the start time!");
                        }else {
                            String booked=customer.get(input[0]+input[1]+input[2]+input[3]);
                            if (booked==null){
                                System.out.println("> Error:The booking being cancelled does not exist!");
                            }else if(booked.equals(input[3])){
                                customer.remove(input[0]+input[1]+input[2]);
                                List<String> periods=orders.get(input[3]);
                                Iterator<String> stringIterator=periods.iterator();
                                while (stringIterator.hasNext()){
                                    if(stringIterator.next().equals(startTime+"--"+endTime)){
                                        stringIterator.remove();
                                        break;
                                    }
                                }
                                orders.remove(input[3]+input[1]+input[2]);
                                Site site=new Site();
                                int money=site.calculateCost(input[1],periodArr[0],periodArr[1],true);
                                if(ordersMap.get(input[3])!=null){
                                    odList=ordersMap.get(input[3]);
                                    Iterator<OrderDetails> iterable=odList.iterator();
                                    while (iterable.hasNext()){
                                        OrderDetails orderDetails=iterable.next();
                                        if(orderDetails.getTimePeriod().equals(input[1]+" "+input[2])){
                                            iterable.remove();
                                            break;
                                        }
                                    }
                                }else {
                                    odList=new ArrayList<>();
                                }
                                OrderDetails od=new OrderDetails();
                                od.setGymName(input[3]);
                                od.setInCome(money);
                                od.setStartTime(sdf.parse(startTime));
                                od.setTimePeriod(input[1]+" "+input[2]);
                                od.setBreachOfContract(true);
                                odList.add(od);
                                ordersMap.put(input[3],odList);
                                System.out.println("> Success: the booking is cancelled!");
                            }
                        }

                    }else {
                        System.out.println("> Error:The booking is invalid!");
                    }
            }
        }


    }

    /**
     * 比较时间重叠
     * @param startTime
     * @param endTime
     * @param periods
     * @return
     */

    public static boolean RepeatedVerification(String startTime,String endTime, List<String> periods){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            for (String period : periods){
                String dateArr[]=period.split("--");
                Date currDateStart = sdf.parse(startTime);//当前时间开始
                Date currDateEnd = sdf.parse(endTime);//当前时间结束
                Date startDate = sdf.parse(dateArr[0]);//每节开始时间
                Date endDate = sdf.parse(dateArr[1]);//每节结束时间
                if((currDateStart.getTime()==startDate.getTime()&&currDateEnd.getTime()==endDate.getTime())||(currDateStart.after(startDate) && currDateStart.before(endDate)) || (currDateEnd.after(startDate)&&currDateEnd.before(endDate))){
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 判断整点
     * @param time
     * @return
     */

    public static boolean wholePoint(String time){
       // SimpleDateFormat myFmt = new SimpleDateFormat("mmss");
       // String hourMinute = myFmt.format(time);
        if(time.contains("00")){
            return true;
        }else{
            return false;
        }
    }
    public static boolean timePeriod(String startTime,String endTime) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date currDateStart = sdf.parse(startTime);//当前时间开始
        Date currDateEnd = sdf.parse(endTime);//当前时间结束
        Date startDate = sdf.parse("09:00");//每节开始时间
        Date endDate = sdf.parse("22:00");//每节结束时间
        if(currDateStart.after(currDateEnd) || currDateStart.getTime()==currDateEnd.getTime()){
            return false;
        }else if(currDateStart.before(startDate) || currDateEnd.after(endDate)){
            return false;
        }
        return true;

    }
}

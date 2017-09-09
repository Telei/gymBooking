package tool;

import gym.OrderDetails;

import java.util.Comparator;

/**
 * 强行对某个对象 collection 进行整体排序 的比较函数
 */
public class SortClass implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        OrderDetails od1=(OrderDetails) o1;
        OrderDetails od2=(OrderDetails) o2;
        return od1.getStartTime().compareTo(od2.getStartTime());
    }
}

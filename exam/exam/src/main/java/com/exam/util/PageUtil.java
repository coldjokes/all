package com.exam.util;

import java.util.List;

import com.google.common.collect.Lists;

/**
 *
  * 分页工具类
 *
 * @author Yifeng Wang
 */
public class PageUtil {

    public static <T> List<T> page1(List<T> source, Integer curPage, Integer pageSize){
        Integer maxIndex = source.size() - 1;
        Integer indexStart = (curPage - 1) * pageSize;
        Integer indexEnd = curPage * pageSize - 1;
        if(indexStart > maxIndex){
            return source;
        }
        if(indexEnd > maxIndex){
            indexEnd = maxIndex;
        }
        List<T> paged = Lists.newArrayList();
        for(int i = indexStart; i <= indexEnd; i ++){
            paged.add(source.get(i));
        }
        return paged;
    }


    public static <T> List<T> page2(List<T> source, int page, int pageSize){
    	int start = getStart(page, pageSize);
    	int end = getEnd(page, pageSize);

        int size = source.size();
        if (size <= end) {
            return source.subList(start - 1, size);
        }
        List<T> list = source.subList(start - 1, end);
        return list;
    }

    public static int getStart(int page, int pageSize){
        return (page - 1) * pageSize + 1 ;
    }

    public static int getEnd(int page, int pageSize){
        return page * pageSize;
    }

    public static void main(String[] args) {
      List<Integer> source = Lists.newArrayList();
      for(int i = 1; i <= 15; i ++){
          source.add(i);
      }
      System.out.println(page2(source, 2, 10));
    }


}

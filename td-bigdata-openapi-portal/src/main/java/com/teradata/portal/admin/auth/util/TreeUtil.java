package com.teradata.portal.admin.auth.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 把一个list集合，里面的bean含有parentId转为树形结构
 * Created by Evan on 2016/7/7.
 */
public class TreeUtil<T extends TreeObject> {

    //public String getUserGrpTreeStr()


    /**
     * 根据父节点的ID获取所有子节点
     * @param list 分类表
     * @param parentId 传入的父节点的ID
     * @return
     */
    public  List<T> getChildResources(List<T> list, int parentId){

        List<T> returnList = new ArrayList<T>();
        for(Iterator<T> iterator = list.iterator();iterator.hasNext();){
            T t = iterator.next();
            //根据传入的某个父节点ID，遍历该父节点的所有子节点
            if(t.getParentId() == parentId){

                recursionFn(list,t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * @param list
     * @param t
     */
    private  void recursionFn(List<T> list,T t){

        List<T> childList = getChildList(list,t); //得到子节点列表
        t.setChildrens(childList);
        for(T tChild : childList){
            if(hasChild(list,tChild)){//判断是否有子节点
               Iterator<T> it = childList.iterator();
                while (it.hasNext()){
                    T n = it.next();
                    recursionFn(list,n);
                }
            }
        }

    }

    /**
     * 得到子节点列表
     * @param list
     * @param t
     * @return
     */
    private  List<T> getChildList(List<T> list,T t){

        List<T> tlist = new ArrayList<T>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()){
            T n = it.next();
            if(n.getParentId() == t.getId()){
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     * @param list
     * @param t
     * @return
     */
    private  boolean hasChild(List<T> list,T t){

        return getChildList(list,t).size() > 0 ? true : false;

    }


    /**
     *
     * @param list
     * @return
     */
    public List<T> getAllTreeNode(List<T> list,List<T> returnList){

        if(returnList != null){
            returnList = new ArrayList<T>();
        }
        if(list != null){
            if(list.size() > 0){
                for(Iterator<T> iterator = list.iterator();iterator.hasNext();){
                    T t = iterator.next();
                    returnList.add(t);
                    if(t.getChildrens() != null && t.getChildrens().size() > 0){
                        getAllTreeNode(t.getChildrens(),returnList);
                    }
                }
            }
        }
        return returnList;
    }


}






























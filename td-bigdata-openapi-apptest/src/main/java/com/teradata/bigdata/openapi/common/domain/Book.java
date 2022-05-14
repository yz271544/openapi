package com.teradata.bigdata.openapi.common.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/10.
 */
@XmlRootElement  /**以实现XML和POJO之间的转换，@XmlRootElement标识Book作为XML结构的根元素*/
public class Book implements Serializable{

    private Long bookId;
    private String bookName;
    private String publisher;

    public Book(){
        super();
    }

    public Book(final Long bookId){
        this.bookId = bookId;
    }

    public Book(final String bookName){
        this.bookName = bookName;
    }

    public Book(final Long bookId,final String bookName){
        super();
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public Book(final Long bookId,final String bookName,final String publisher){
        this.bookId = bookId;
        this.bookName = bookName;
        this.publisher = publisher;
    }


}

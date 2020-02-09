package top.hcy.webtable.entity;

import lombok.Data;
import top.hcy.webtable.annotation.field.*;
import top.hcy.webtable.annotation.table.WEnadbleInsert;
import top.hcy.webtable.annotation.table.WEnadbleSort;
import top.hcy.webtable.annotation.table.WEnadbleUpdate;
import top.hcy.webtable.annotation.table.WTable;
import top.hcy.webtable.common.enums.WebFieldType;

/**
 * @ProjectName: webtabletest
 * @Package: top.hcy.webtabletest.entity
 * @ClassName: Book
 * @Author: hcy
 * @Description:
 * @Date: 20-2-6 23:16
 * @Version: 1.0
 **/
@Data
@WTable(aliasName = "图书管理模块",tableName = "Books")
@WEnadbleInsert
@WEnadbleUpdate
@WEnadbleSort
public class Book {
    @WField(aliasName = "书籍ID",columnName = "book_id",fieldType = WebFieldType.NUMBER)
    @WFindField
    private String bookId;

    @WField(aliasName = "书名",columnName = "book_name",fieldType = WebFieldType.STRING)
    @WInsertField
    @WUpdateField
    @WFindField
    private String bookName;

    @WField(aliasName = "简介",columnName = "book_desc",fieldType = WebFieldType.STRING)
    @WInsertField
    @WUpdateField
    private String bookDesc;

    @WField(aliasName = "封面",columnName = "book_pic",fieldType = WebFieldType.IMAGEURL)
    @WInsertField
    @WUpdateField
    private String bookPic;

    @WField(aliasName = "价格",columnName = "book_price",fieldType = WebFieldType.NUMBER)
    @WInsertField
    @WUpdateField
    @WSortField
    private String bookPrice;

    private int bookNum;
}
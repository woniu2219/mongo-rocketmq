package com.example.mongo.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author liwenbo
 * @Date 2019/11/25 9:37
 * @Description
 */
@Document
@Data
public class MongoTest {
    @Id
    private String id;
    private String name;
    private String address;
    private Integer age;

}

[![License](http://img.shields.io/:license-apache-blue.svg "2.0")](http://www.apache.org/licenses/LICENSE-2.0.html)
[![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")]()

## JpaMapper项目简介

- 如果你喜欢Jpa hibernate的简洁写法；
- 或许你不喜欢写sql；
- 或许你用了Mapper工具之后还是要写sql；

那就用JpaMapper吧！JpaMapper是尽量按照JPA hibernate的书写风格，对mybatis进行封装，是CRUD操作更加简单易用，免于不断写sql。

JpaMapper以动态生成sql替换手动生成sql的过程，并根据注解生成sqlsource的过程去生成sql，并将sql交给mybatis去管理，原理上和自己写sql是一致的，并不会去替换mybatis的底层实现。因为不用担心无法操控，任何可能出现的问题，只需要debug下查看生成的sql和预期的是否一致即可。

## 主要功能
v1.0.0:

 1. 增加(C): 提供单个实体保存、批量保存功能。
 2. 查询(R): 提供单/多查询，并支持findBy自定义字段名查询。
 3. 更新(U): 提供单个实体更新、批量更新功能。
 4. 删除(D): 提供单/多删除功能，并支持deleteBy自定义字段名删除。
 5. 简单易用，继承CrudMapper即可。
 6. 易于切换，从JPA hibernate替换为mybatis，只需要将CrudRepository替换为CrudMapper，当然，mybatis没办法方法重载，所以CrudRepository相同的方法名会做一定的区分。
 
v1.1

 1. 部分逻辑调整优化
 2. 增加简单分表功能

## 启动说明
 **springboot启动：** 
```xml
<dependency>
    <groupId>com.cff</groupId>
    <artifactId>jpa-mapper-spring-boot-starter</artifactId>
    <version>1.1</version>
</dependency>
```

 **非AutoConfiguration:** 
```xml
<dependency>
    <groupId>com.cff</groupId>
    <artifactId>jpa-mapper-core</artifactId>
    <version>1.1</version>
</dependency>
```
使用@Autowired注入List<SqlSessionFactory\> sqlSessionFactoryList;
调用：
```
MapperScanner mapperScanner = new MapperScanner();
mapperScanner.scanAndRegisterJpaMethod(sqlSessionFactoryList);
```

## 使用说明
**快速启动：**

基于mybatis注解方案，需要继承CrudMapper<T, ID>, CrudMapper中定义的方法可以直接使用，查询方法支持findBy + 字段名（And）查询。删除方法支持deleteBy + 字段名（And）删除。

这里获取字段的方式是根据接口中泛型类的字段去解析的，字段必须加上@Column注解，Id字段加上@Id可以不加@Column，这里没实现那么多功能，原因是个人觉得没多大必要，支持的越多，项目越臃肿，这就偏离了选择mybatis的初衷

**主键策略：**

1. @GeneratedValue(generator="JDBC"), 使用自增策略，对应mybatis的Jdbc3KeyGenerator
2. @GeneratedValue除generator="JDBC"外，支持@SelectKey注解（非mybatis的注解，但和mybatis的注解一致，这里是为了将SelectKey注解扩展到字段上）添加到字段上，和mybatis的@SelectKey注解功能一致，可以自定义主键策略。

**分表功能：**

1. 需要继承SimpleShardingMapper<T, ID>, SimpleShardingMapper中定义的方法可以直接使用，因为mybatis的sql是根据类名+方法名确定唯一，所有SimpleShardingMapper和CrudMapper不能同时使用。
2. 在实体中分表字段增加注解	@ShardingKey(prefix="_", methodPrecis="getTable", methodRange = "getTables")  methodPrecis为了根据字段值精确查找表的后缀。methodRange是为了支持分表字段的between and 操作。其余字段选填。
3. 在实体中增加静态方法（methodPrecis和methodRange指定的方法名），上所示的方法：<br>
```

	public static String getTable(Object mobile) {
		int index = Integer.parseInt(mobile.toString()) % 5;
		return String.valueOf(index);
	}
	
	public static String[] getTables(Object start, Object end) {
		Map<Integer, String> maps = new HashMap<>();
		int index = 0;
		for(int i = Integer.parseInt(start.toString()); i < Integer.parseInt(end.toString()); i++){
			if(index >= 5)break;
			maps.put(index, String.valueOf(i % 5));
			index++;	
		}
		
		List<String> mapValueList = new ArrayList<String>(maps.values()); 
		String[] arr = new String[mapValueList.size()];
		return mapValueList.toArray(arr);
	}
```

示例使用mobile的取余5寻找分表字段。

## 设计原理

https://blog.csdn.net/feiyangtianyao/article/category/8446635

## 版权声明
JpaMapper使用 Apache License 2.0 协议.

## 作者信息
      
   作者博客：https://blog.csdn.net/feiyangtianyao
   
   作者邮箱： fufeixiaoyu@163.com

## License
Apache License


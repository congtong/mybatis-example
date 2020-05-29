## 必备知识
1. 什么是JDBC  JAVA DATABASE Connectivity  这是面向关系型数据库的
2. 驱动分为哪四类 DBC-ODBC桥 本地API驱动 网络协议驱动 本地协议驱动
3. com.mysql.jdbc.Driver 和 com.mysql.cj.jdbc.Driver的区别
```
1. com.mysql.jdbc.Driver 是 mysql-connector-java 5中的，
2. com.mysql.cj.jdbc.Driver 是 mysql-connector-java 6中的  这个需要设定时区 serverTimezone
driverClassName=com.mysql.cj.jdbc.Driver
jdbc:mysql://192.168.1.2:3306/tc?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
```
## 什么是mybatis
```
1. 是一个持久层框架
2. 自定义SQL
3. 存储过程
4. 高级映射
5. 免除了所有JDBC代码以及设置参数和获取结果集的工作

Mybatis可以通过简单的XML或注解 配置映射和原始类型 接口 POJO
```
## 实例入门
1. 创建maven项目做测试，本示例用的idea 具体步骤省略
2. 配置pom.xml
```
1. 配置 JDK 版本和编码方式  设置后， 编码方式为 UTF-8， JDK 版本为 1.8
<plugins>
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-compiler-plugin</artifactId>
		<version>2.3.2</version>
		<configuration>
			<source>1.8</source>
			<target>1.8</target>
			<encoding>UTF-8</encoding>
		</configuration>
	</plugin>
</plugins>

2. 设置资源文件路径
Maven 中默认是只会打包 resource下的资源文件。如果我们的文件不放在resource， 则需要通过配置告知Maven
<resources>
    <resource>
        <directory>src/main/java</directory>
        <includes>
            <include>**/*.properties</include>
            <include>**/*.xml</include>
        </includes>
        <filtering>false</filtering>
    </resource>
</resources>
3. 添加 mybatis 依赖
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.4</version>
        </dependency>
4. 添加数据库驱动依赖 注意版本 6 之后和 6 之前的driver是不一样的 
<!--数据库 mysql 驱动-->

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.20</version>
</dependency>

5. 添加日志依赖  slf4j 是统一的接口 配置的实现时log4j
<!-- 实现slf4j接口并整合 -->
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>1.7.25</version>
</dependency>
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-log4j12</artifactId>
	<version>1.7.25</version>
</dependency>
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-core</artifactId>
	<version>2.10.0</version>
</dependency>
6. 添加测试
<!--junit 测试-->
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.12</version>
	<scope>test</scope>
</dependency>
```
3. 建表 example.sql文件里有现场的sql例子 可以做测试用
4. 创建配置文件
```
1. 第一个配置文件config.xml  这个名字可以随便起 只要是xml就行 因为是java指定加载的文件
里边的内容从官网上可以考一下约束 https://mybatis.org/mybatis-3/zh/getting-started.html
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
这里边写配置  写的时候配置项之间是有先后顺序的 否则会报错 idea会标红 调整一下就可以
正确的顺序是
properties?, 
settings?, 
typeAliases?, 
typeHandlers?, 
objectFactory?,
objectWrapperFactory?, 
plugins?, 
environments?, 
databaseIdProvider?, 
mappers?
</configuration>

里边的配置项可以在官网查到  
这里主要讲解用到的几个
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="${driver}"/>
            <property name="url" value="${url}"/>
            <property name="username" value="${username}"/>
            <property name="password" value="${password}"/>
        </dataSource>
    </environment>
</environments>
上边这个配置项 是配置连接数据库的参数  这里就配置了一个  是可以配置多个  多个时候的 environment 的id需要变 
别重复了
value里的变量都是自己定义的 随便定义能匹配上properties里的值就行

config.properties 这个名字随便起 位置随便放能找到就行
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://192.168.1.180:3306/mybbs?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
username=root
password=!xxxxxxx
因为是用的xml这个属性指定的 下边这个resource指定文件
<properties resource="config.properties">

</properties>

下边这个是配置的项 可以配置好多属性 可以看官网的介绍 这里就用到了配置日志  pom里引入的是log4j
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>

下边这个是别名 可以配置多个 也可以不配置 如果不配置写的xxMapper.xml就写全路径就行 
包括写的Interger String都得写包的全路径名字
<typeAliases>
    <package name="entity"/>
</typeAliases>

下边这个是指定mapper的路径
<mappers>
    <package name="mapper"/>
</mappers>
项目中一般是所有的xml都放一个目录下 指定下就行随便建个文件
```
5. 配置文件写完了 接下来写两个demo我是根据sql直接抄过来的能用
```java
//实体类pojo
@Data
public class Student {
    private Integer studentId;
    private String name;
    private String phone;
    private String email;
    private Byte sex;
    private Byte locked;
    private Date gmtCreated;
    private Date gmtModified;
}
// 对应数据表 主要是取出来的时候有个自动映射的功能
//@Data 是lombok的包里的 不用些get set了 idea得安装个插件
```
```java
public interface StudentMapper {
    List<Student> selectAll();
}
// 注意这是个interface 这是mybatis的特性java api 
// 对应到xml里能找到就行 xml是具体的实现 当然也可以写实现类通过java拼接sql使用比jdbc好用点，一般没见过这么用的
```
6. 写个测试类跑一下
```java
public class StudentMapperTest {
    private static SqlSessionFactory sqlSessionFactory;
    @BeforeClass
    public static void init() {
        try {
            Reader reader = Resources.getResourceAsReader("config.xml"); //这是自己指定的
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testSelectList() {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();

            List<Student> students = sqlSession.selectList("selectAll");
            for (int i = 0; i < students.size(); i++) {
                System.out.println(students.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
//sqlSession一定要记得关啊
```
```
// jdk1.7之后就这么写了
try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    List<Student> students = sqlSession.selectList("selectAll");
    for (int i = 0; i < students.size(); i++) {
        System.out.println(students.get(i));
    }
}
```
好了，能查出数据来了。但是可能会报个日志的错误 把例子中的log4j.properties拷过去就行了
示例完成了。
## 简单的记录了下官网的一些点
1. 从 XML 中构建 SqlSessionFactory
```
SqlSessionFactory这是一个实例
SqlSessionFactory 是通过 SqlSessionFactoryBuilder 获得
而SqlSessionFactoryBuilder 则可以从 XML的配置文件  或者 预先配置的Configuration 实例来构建出SqlSessionFactory 实例

mybaits 有一个Resources的工具类来加载配置文件
```
从SqlSessionFactory中获取 SqlSession
```
try(SqlSession session = sqlSessionFactory.openSession()) {
	Blog blog = (Blog) session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);
}
之前的命名空间中 命名空间（Namespaces）的作用并不大  但是现在 随着命名空间越发重要，你必须指定命名空间
每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。

如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 下面的示例就是一个确保 SqlSession 关闭的标准模式：

try (SqlSession session = sqlSessionFactory.openSession()) {
  // 你的应用逻辑代码
}
```
如果一个属性不只在一个地方进行了配置，mybatis的加载顺序
```
首先读取在 properties 元素体内指定的属性。
然后根据 properties 元素中的 resource 属性读取类路径下属性文件，或根据 url 属性指定的路径读取属性文件，并覆盖之前读取过的同名属性。
最后读取作为方法参数传递的属性，并覆盖之前读取过的同名属性。

因此，通过方法参数传递的属性具有最高优先级，resource/url 属性中指定的配置文件次之，最低优先级的则是 properties 元素中指定的属性
```
XML 映射器
```
SQL 映射文件只有很少的几个顶级元素（按照应被定义的顺序列出）：
cache – 该命名空间的缓存配置。
cache-ref – 引用其它命名空间的缓存配置。
resultMap – 描述如何从数据库结果集中加载对象，是最复杂也是最强大的元素。
sql – 可被其它语句引用的可重用语句块。
insert – 映射插入语句。
update – 映射更新语句。
delete – 映射删除语句
select – 映射查询语句。
```
动态SQL
if
```sql
<select id="findActiveBlogWithTitleLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE state = 'ACTIVE'
  <if test="title != null">
    AND title like #{title}
  </if>
</select>
```
choose、when、otherwise
```sql
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```

trim、where、set
```sql
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE
  <if test="state != null">
    state = #{state}
  </if>
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ...
</trim>
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

foreach
```sql
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list"
      open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

script 
```sql
    @Update({"<script>",
      "update Author",
      "  <set>",
      "    <if test='username != null'>username=#{username},</if>",
      "    <if test='password != null'>password=#{password},</if>",
      "    <if test='email != null'>email=#{email},</if>",
      "    <if test='bio != null'>bio=#{bio}</if>",
      "  </set>",
      "where id=#{id}",
      "</script>"})
    void updateAuthorValues(Author author);
```
bind
```sql
<select id="selectBlogsLike" resultType="Blog">
  <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
  SELECT * FROM BLOG
  WHERE title LIKE #{pattern}
</select>
```
多数据库支持
```sql
<insert id="insert">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    <if test="_databaseId == 'oracle'">
      select seq_users.nextval from dual
    </if>
    <if test="_databaseId == 'db2'">
      select nextval for seq_users from sysibm.sysdummy1"
    </if>
  </selectKey>
  insert into users values (#{id}, #{name})
</insert>
```
动态 SQL 中的插入脚本语言
```sql
MyBatis 从 3.2 版本开始支持插入脚本语言，这允许你插入一种语言驱动，并基于这种语言来编写动态 SQL 查询语句。
public interface LanguageDriver {
  ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
  SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);
  SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);
}

实现自定义语言驱动后，你就可以在 mybatis-config.xml 文件中将它设置为默认语言：

<typeAliases>
  <typeAlias type="org.sample.MyLanguageDriver" alias="myLanguage"/>
</typeAliases>
<settings>
  <setting name="defaultScriptingLanguage" value="myLanguage"/>
</settings>

或者，你也可以使用 lang 属性为特定的语句指定语言：
<select id="selectBlog" lang="myLanguage">
  SELECT * FROM BLOG
</select>


或者，在你的 mapper 接口上添加 @Lang 注解：

public interface Mapper {
  @Lang(MyLanguageDriver.class)
  @Select("SELECT * FROM BLOG")
  List<Blog> selectBlog();
}
```
### 接下来学习mybatis的分页 这个mybatis的分页用的是github上开源的程序 还是个中国人写的
使用起来很简单
第一步引入依赖
先说说mybaits如何引入
```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>latest version</version>   注意用最新版本 因为旧版本会提示包找不到的问题
</dependency>
```
还支持spring和spring-boot的引入方法
第二步加上配置
mybatis用法需要在mybatis的配置文件里加上一个插件的标签配置一些属性很简单
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
        这里边这些属性简单写一下github上有完整的解释
        helperDialect: 写数据库的类型 oracle, mysql, mariadb, sqlite, hsqldb, postgresql, db2, sqlserver, informix, h2, sqlserver2012, derby
	    offsetAsPageNum
	    rowBoundsWithCount
	    pageSizeZero
	    reasonable
	    params
	    supportMethodsArguments
	    autoRuntimeDialect
	    closeConn
	    aggregateFunctions
	</plugin>
</plugins>
注意plugins的位置就行
配置几个觉得有用的基本上就能用了2分钟极速入门
第三步骤 查询的时候调用一下他提供的方法就给分页了
用法也超级简单
好几种用法
1. use by RowBounds
```
List<User> list = sqlSession.selectList("x.y.selectIf", null, new RowBounds(0, 10));
```
2. interface
List<User> list = userMapper.selectIf(1, new RowBounds(0, 10));
3. PageRowBounds
PageRowBounds rowBounds = new PageRowBounds(0, 10);
List<User> list = userMapper.selectIf(1, rowBounds);
long total = rowBounds.getTotal();

4. use static method startPage 我用的这个 但是这个是有点安全隐患的啊
写了之后后边必须写上查询语句 还得挨着否则就线程不安全 一直挂那了 会乱 可能别的查询的时候就用这个了
PageHelper.startPage(1, 10);
List<User> list = userMapper.selectIf(1);
这个用着很清爽 我选择的这个 只需要注意跟查询挨着不会有什么问题

5. use static method offsetPage
PageHelper.offsetPage(1, 10);
List<User> list = userMapper.selectIf(1);
6. method parameters
public interface CountryMapper {
    List<User> selectByPageNumSize(
            @Param("user") User user,
            @Param("pageNum") int pageNum, 
            @Param("pageSize") int pageSize);
}
//config supportMethodsArguments=true
List<User> list = userMapper.selectByPageNumSize(user, 1, 10);
7. POJO parameters
public class User {
    //other fields
    //The following two parameters must be the same name as the params parameter
    private Integer pageNum;
    private Integer pageSize;
}
public interface CountryMapper {
    List<User> selectByPageNumSize(User user);
}
//When the pageNum! = null && pageSize! = null in the user instance, this method will be automatically pagination
List<User> list = userMapper.selectByPageNumSize(user);
8. ISelect interface
jdk6,7 anonymous class, return Page
Page<User> page = PageHelper.startPage(1, 10).doSelectPage(new ISelect() {
    @Override
    public void doSelect() {
        userMapper.selectGroupBy();
    }
});
//jdk8 lambda
Page<User> page = PageHelper.startPage(1, 10).doSelectPage(()-> userMapper.selectGroupBy());
9. return PageInfo
pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(new ISelect() {
    @Override
    public void doSelect() {
        userMapper.selectGroupBy();
    }
});
10 . in lambda
pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(() -> userMapper.selectGroupBy());

11. do count only
long total = PageHelper.count(new ISelect() {
    @Override
    public void doSelect() {
        userMapper.selectLike(user);
    }
});

12. lambda
total = PageHelper.count(()->userMapper.selectLike(user));

spring 和 springboot  跟直接使用没有任务区别 只是配置简单了
使用的实例可以看看测试的例子


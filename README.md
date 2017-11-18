# Configuration

## XML

xml is the most commonly used configuration approach for building `SqlSessionFactory` 

a configuration contains, in order inside the xml,

1. properties
2. settings
3. typeAliases
4. typeHandlers
5. environments
6. mappers

### Environment

- You can set multiple environments and changethe default environment to switch between them

- multiple database within the same application

  - a seperate environment for each database

  - a seperate SqlSessionFactory for each database

  - create the SqlSessionFactory with:

    ```java
    inputStream = Resources.getResourceAsStream("mybatis-config.xml");
    cartSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStre
    am,"shoppingcart");
    reportSqlSessionFactory = new SqlSessionFactoryBuilder().
    build(inputStream,"reports");
    ```

### DataSource

- needs 4 properties: `driver`, `url`, `username` and `password`
- the `type` attribute can be `POOLED`, `UNPOOLED` or `JNDI`
  - `UNPOOLED`: create a new connection and close that connection for each DB operation. best used for simple applications with little small concurrent users
  - `POOLED`: commonly used for developing/testing/production
  - `JDNI`:preferred in production

### Transaction

- 2 types: `JDBC` and `MANAGED`
- `JDBC`: 
  - <u>application</u> is responsible for managing connection life cycle: commit, rollback.
  - MyBatis uses `JdbcTransactionFactory` class to create `TransactionManager`
  - an application deployed on tomcat would manage transaction by itself
- `MANAGED`: 
  - <u>application server</u> is responsible for managing connection life cycle
  - `ManagedTransactionFactory`
  - For example, a JavaEE application deployed on an application server,
    such as JBoss, WebLogic, or GlassFish, can leverage the application
    server's transaction management capabilities using EJB. In these managed
    environments, you can use the MANAGED transaction manager. 

### Properties

- 3 ways to get properties

  1. external properties files(resource/url attributes)

     application.properties:

     ```properties
     jdbc.driverClassName=com.mysql.jdbc.Driver
     jdbc.url=jdbc:mysql://localhost:3306/mybatisdemo
     jdbc.username=root
     jdbc.password=admin
     ```

  2. properties specified in the body of the properties element

     mybatis-config:

     ```xml
     <properties resource="application.properties">
       <property name="jdbc.username" value="db_user"/>
       <property name="jdbc.password" value="verysecurepwd"/>
     </properties>
     <dataSource type="POOLED">
       <property name="driver" value="${jdbc.driverClassName}"/>
       <property name="url" value="${jdbc.url}"/>
       <property name="username" value="${jdbc.username}"/>
       <property name="password" value="${jdbc.password}"/>
     </dataSource>
     ```

  3. passed as a method parameter

- the highest priority properties are those <u>passed in as a method parameter(</u>3), followed by <u>resource/url attributes</u>(1) and finally the <u>properties specified in the body of the properties element</u>(2).

### typeAliases

3 ways to set typeAlias

1. manually
2. scan
3. `@Alias`
#### Manually
typeAlias are alias for fully qualified names of the JavaBeans for the `resultType` and `parameterTypes` attributes

configuration:

```xml
<typeAliases>
	<typeAlias alias="Student" type="com.mybatis3.domain.Student"/>
</typeAliases>
```

before:

```
<select id="findStudentById" parameterType="int"
resultType="com.mybatis3.domain.Student">
SELECT STUD_ID AS ID, NAME, EMAIL, DOB
FROM STUDENTS WHERE STUD_ID=#{Id}
</select>
```

after:

```
<select id="findStudentById" parameterType="int"
resultType="student">
SELECT STUD_ID AS ID, NAME, EMAIL, DOB
FROM STUDENTS WHERE STUD_ID=#{Id}
</select>
```
#### package scan
A easy way is to give the package name where MyBatis can scan and register alias 

```xml
<typeAliases>
	<package name="com.mybatis3.domain"/>
</typeAliases>
```

`com.mybatis3.domain.Student` will have typeAlias of `student`
#### @Alias
```java
@Alias("StudentAlias")
public class Student
```

@Alias overrides <typeAliases> configuration

### typeHandlers

please read the textbook

```java
public class PhoneTypeHandler extends BaseTypeHandler<PhoneNumber>
{
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i,
  PhoneNumber parameter, JdbcType jdbcType) throws
  SQLException {
  	ps.setString(i, parameter.getAsString());
  }
  @Override
  public PhoneNumber getNullableResult(ResultSet rs, String
  columnName)
  throws SQLException {
  	return new PhoneNumber(rs.getString(columnName));
  }
  @Override
  public PhoneNumber getNullableResult(ResultSet rs, int
  columnIndex)
  throws SQLException {
  	return new PhoneNumber(rs.getString(columnIndex));
  }
  @Override
  public PhoneNumber getNullableResult(CallableStatement cs, int
  columnIndex)
  throws SQLException {
  	return new PhoneNumber(cs.getString(columnIndex));
  }
}
```

in mybatis-config.xml

```xml
<typeHandlers>
	<typeHandler handler="com.mybatis3.typehandlers.PhoneTypeHandler"/>
</typeHandlers>
```

### Settings

The default MyBatis global settings, which can be overridden to better suit
application-specifc needs, are as follows: 

```xml
<settings>
<setting name="cacheEnabled" value="true"/>
<setting name="lazyLoadingEnabled" value="true"/>
<setting name="multipleResultSetsEnabled" value="true"/>
<setting name="useColumnLabel" value="true"/>
<setting name="useGeneratedKeys" value="false"/>
<setting name="autoMappingBehavior" value="PARTIAL"/>
<setting name="defaultExecutorType" value="SIMPLE"/>
<setting name="defaultStatementTimeout" value="25000"/>
<setting name="safeRowBoundsEnabled" value="false"/>
<setting name="mapUnderscoreToCamelCase" value="false"/>
<setting name="localCacheScope" value="SESSION"/>
<setting name="jdbcTypeForNull" value="OTHER"/>
<setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode
,toString"/>
</settings>
```

### Mappers

```xml
<mappers>
  <mapper resource="com/mybatis3/mappers/StudentMapper.xml"/>
  <mapper url="file:///D:/mybatisdemo/app/mappers/TutorMapper.xml"/>
  <mapper class="com.mybatis3.mappers.TutorMapper"/>
  <package name="com.mybatis3.mappers"/>
</mappers>
```

1. `resource`: mapper file in the classpath
2. `url`: point to a mapper file by fully qualified filesystem path or web URL
3. `class`: Mapper interface
4. `package`: package where Mapper interfaces can be found



> [official documentation about configuration](http://www.mybatis.org/mybatis-3/configuration.html)
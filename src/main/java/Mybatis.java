import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Mybatis {
  public static void main(String[] args) throws IOException {
    //
      InputStream resourceAsStream = Resources.getResourceAsStream("config.xml");
      final SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resourceAsStream);
  }
}

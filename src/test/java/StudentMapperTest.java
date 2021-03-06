import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class StudentMapperTest {
    private static SqlSessionFactory sqlSessionFactory;

    //@BeforeClass -> @Before -> @Test -> @After -> @AfterClass;
    //@Before -> @Test -> @After;

    @BeforeClass
    public static void init() {
        try {
            Reader reader = Resources.getResourceAsReader("config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testSelectList() {
//        SqlSession sqlSession = null;
//        try {
//            sqlSession = sqlSessionFactory.openSession();
//
//            List<Student> students = sqlSession.selectList("selectAll");
//            for (int i = 0; i < students.size(); i++) {
//                System.out.println(students.get(i));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqlSession != null) {
//                sqlSession.close();
//            }
//        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {

            List<Student> students = sqlSession.selectList("selectAll");
            for (int i = 0; i < students.size(); i++) {
                System.out.println(students.get(i));
            }
        }
    }

    @Test
    public void testSelectListPage() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PageHelper.startPage(1, 4);
            List<Student> students = sqlSession.selectList("selectAll");
            PageInfo pageInfo = new PageInfo(students);
            System.out.println(pageInfo.getTotal());
        }
    }
}

package no.kristiania.db;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedHashSet;

import static no.kristiania.db.MemberDaoTest.sampleMember;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskMemberDaoTest {
    private MemberDao memberDao;
    private TaskDao taskDao;
    private TaskMemberDao taskMemberDao;

    @BeforeEach
    void setup() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdatabase;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();

        taskDao = new TaskDao(dataSource);
        memberDao = new MemberDao(dataSource);
        taskMemberDao = new TaskMemberDao(dataSource);

    }

    @Test
    void shouldRetrieveAllMembersOnTask() throws SQLException {
        Task task1 = TaskDaoTest.sampleTask();
        taskDao.insert(task1);


        Member member1 = sampleMember();
        Member member2 = sampleMember();

        memberDao.insert(member1);
        memberDao.insert(member2);

        taskMemberDao.insert(task1.getId(), member1.getId());
        taskMemberDao.insert(task1.getId(), member2.getId());

        LinkedHashSet <Long> memberIds = taskMemberDao.retrieveMembersByTaskId(task1.getId());

        assertThat(memberIds).contains(member1.getId());
        assertThat(memberIds).contains(member2.getId());
    }

    @Test
    void shouldAllTasksOnMember() throws SQLException {
        Task task1 = TaskDaoTest.sampleTask();
        Task task2 = TaskDaoTest.sampleTask();

        taskDao.insert(task1);
        taskDao.insert(task2);

        Member member1 = sampleMember();
        memberDao.insert(member1);

        taskMemberDao.insert(task1.getId(), member1.getId());
        taskMemberDao.insert(task2.getId(), member1.getId());

        LinkedHashSet <Long> taskIds = taskMemberDao.retrieveTasksByMemberId(member1.getId());

        assertThat(taskIds).contains(task1.getId());
        assertThat(taskIds).contains(task2.getId());
    }

}

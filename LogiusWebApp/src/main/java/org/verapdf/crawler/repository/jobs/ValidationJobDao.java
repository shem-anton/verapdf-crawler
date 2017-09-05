package org.verapdf.crawler.repository.jobs;

import org.springframework.jdbc.core.JdbcTemplate;
import org.verapdf.crawler.domain.validation.ValidationJobData;
import org.verapdf.crawler.repository.DaoUtils;
import org.verapdf.crawler.repository.mappers.ValidationJobMapper;

import javax.sql.DataSource;
import java.util.List;

public class ValidationJobDao {
    private final JdbcTemplate template;
    private static final String VALIDATION_JOB_TABLE_NAME = "pdf_validation_jobs_queue";
    public static final String FIELD_FILEPATH = "filepath";
    public static final String FIELD_JOB_DIRECTORY = "job_directory";
    public static final String FIELD_FILE_URL = "document_url";
    public static final String FIELD_LAST_MODIFIED = "time_last_modified";
    private static final String FIELD_VALIDATION_STATUS = "validation_status";
    public static final String STATUS_NOT_STARTED = "not_started";
    private static final String STATUS_IN_PROGRESS = "in_progress";

    public ValidationJobDao(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public ValidationJobData getOneJob() {
        List<ValidationJobData> validationJobs = template.query("select * from " + VALIDATION_JOB_TABLE_NAME, new ValidationJobMapper());
        ValidationJobData result = null;
        if(!validationJobs.isEmpty()) {
            result = validationJobs.get(0);
            template.update(String.format("update %s set %s=?", VALIDATION_JOB_TABLE_NAME, FIELD_VALIDATION_STATUS), STATUS_IN_PROGRESS);
        }
        return result;
    }

    public void deleteJob(ValidationJobData job) {
        template.update(String.format("delete from %s where %s=?", VALIDATION_JOB_TABLE_NAME, FIELD_FILEPATH), job.getFilepath());
    }

    public void addJob(ValidationJobData job) {
        template.update(String.format("insert into %s (%s, %s, %s, %s) values (?,?,?,?)",
                VALIDATION_JOB_TABLE_NAME, FIELD_FILEPATH, FIELD_JOB_DIRECTORY, FIELD_FILE_URL,FIELD_LAST_MODIFIED),
                job.getFilepath(), job.getJobDirectory(), job.getUri(), DaoUtils.getSqlTimeFromLastmodified(job.getTime()));
    }

    public Integer getQueueSize() {
        return template.queryForObject(String.format("select count(*) from %s", VALIDATION_JOB_TABLE_NAME), Integer.class);
    }
}

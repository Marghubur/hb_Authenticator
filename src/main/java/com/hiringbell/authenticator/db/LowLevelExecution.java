package com.hiringbell.authenticator.db;

import com.hiringbell.authenticator.model.DbParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LowLevelExecution {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public <T> Map<String, Object> executeProcedure(String procedureName, List<DbParameters> sqlParams) throws Exception {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("hiringbell_dev")
                .withProcedureName(procedureName);

        Map<String, Object> paramSet = new HashMap<>();
        try {
            for (DbParameters dbParameters : sqlParams) {
                paramSet.put(dbParameters.parameter, dbParameters.value);
                simpleJdbcCall.addDeclaredParameter(
                        new SqlParameter(
                                dbParameters.parameter,
                                dbParameters.type
                        ));
            }

            return simpleJdbcCall.execute(paramSet);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

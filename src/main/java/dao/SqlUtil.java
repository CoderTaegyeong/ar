package dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import entity.mapper.StringListRowMapper;


public class SqlUtil {
	private JdbcTemplate jdbcTemplate;

	public SqlUtil(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public <T> Optional<T> selectOne(String sql, RowMapper<T> rowMapper, Object... args) {
		return jdbcTemplate.query(sql, rowMapper, args).stream().findAny();
	}
	
	public List<String> selectOne(String query){
		List<List<String>> result = jdbcTemplate.query(query, new StringListRowMapper());
		return result.get(0);
	}
	
	/**
	 * @return 첫 행은 칼럼 이름으로 사용된다.
	 */
	public List<List<String>> select(String query) {
		StringListRowMapper rowMapper = new StringListRowMapper();
		List<List<String>> result = jdbcTemplate.query(query, rowMapper);
		result.add(0, rowMapper.getColumnNames());
		return result;
	}

	public List<List<String>> selectTable(String tableName) {
		return select("SELECT * FROM " + tableName);
	}

	public <T> List<T> select(String query, RowMapper<T> rowMapper) {
		return jdbcTemplate.query(query, rowMapper);
	}

	public <T> List<T> selectTable(String tableName, RowMapper<T> rowMapper) {
		return select("SELECT * FROM " + tableName, rowMapper);
	}

	/**
	 * @param DB 테이블 칼럼 명과 rowData 의 필드 명이 같아야 합니다.
	 */
	public int insert(String tableName, Object rowData) {
		BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(rowData);
	    return new SimpleJdbcInsert(jdbcTemplate).withTableName(tableName).execute(bpsps);
	}
	
	/**
	 * @param excludes 업데이트에서 제외할 칼럼 명을 입력하세요 
	 * 
	 * ++primaryKey는 제외됩니다++
	 */
	public int update(String tableName, Object rowData, String primaryKey, String... excludes) {
		String query = "UPDATE " + tableName + " SET ";
		String setClause = "";

		Field pkField = null;
		try {
			pkField = rowData.getClass().getDeclaredField(primaryKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		BeanPropertySqlParameterSource bpsps = new BeanPropertySqlParameterSource(rowData);
		String[] propertyNames = bpsps.getReadablePropertyNames();
		List<Object> args = new ArrayList<>();
		List<Integer> argTypes = new ArrayList<>();
		List<String> excludeList = new ArrayList<>(Arrays.asList(excludes));
		excludeList.add("class");
		excludeList.add(pkField.getName());

		Loop: for (String property : propertyNames) {
			for (String exclude : excludeList) {
				if (exclude.equals(property))
					continue Loop;
			}
			setClause += property + "=?,";
			args.add(bpsps.getValue(property));
			argTypes.add(bpsps.getSqlType(property));
		}

		setClause = setClause.substring(0, setClause.length() - 1);
		query += setClause + " WHERE " + pkField.getName() + "=?";
		args.add(bpsps.getValue(pkField.getName()));
		argTypes.add(bpsps.getSqlType(pkField.getName()));
		return jdbcTemplate.update(query, args.toArray(), argTypes.stream().mapToInt(i -> i).toArray());
	}

	public int delete(String tableName, String key, String value) {
		String query = "DELETE FROM " + tableName + " WHERE " + key + "=?";
		return jdbcTemplate.update(query, value);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
}
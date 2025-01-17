package com.team.legendary.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.team.legendary.persistence.entity.Customer;


@Repository
public class CustomerDaoImpl implements CustomerDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public List<Customer> findAll() {
		List<Customer> customers = this.jdbcTemplate.query("select * from Customer", new CustomerMapper());
    	return customers;
	}

	public Customer findOne(String name) {
		List<Customer> customers = this.jdbcTemplate.query("select * from Customer where customerName=" + name, new CustomerMapper());
    	if (customers.isEmpty()) return null;
    	return customers.get(0);
	}

	public void create(Customer entity) {
		final String INSERT_SQL = "insert into Customer (customerName,password) values(?,?)";
    	final Customer temp = entity;
    	
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"customerName"});
		            ps.setString(1, temp.getName());	
		            ps.setString(2, temp.getPassword());
		            return ps;
		        }
		    },
		    keyHolder);

	}
	
	public static final class CustomerMapper implements RowMapper<Customer> {

		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Customer customer = new Customer();
			customer.setName(rs.getString("customerName"));
			customer.setPassword(rs.getString("password"));
			return customer;
		}
    	
    }

}

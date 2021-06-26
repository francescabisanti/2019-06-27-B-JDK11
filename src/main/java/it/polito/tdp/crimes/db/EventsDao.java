package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listAllCategorie(){
		String sql = "SELECT DISTINCT e.offense_category_id as id "
				+ "FROM EVENTS e "
				+ "ORDER BY e.offense_type_id ASC " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> listAllMesi(){
		String sql = "SELECT DISTINCT MONTH(e.reported_date) AS m "
				+ "FROM EVENTS e "
				+ "ORDER BY m ASC " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("m"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> listAllVertici(String categoria, Integer mese){
		String sql = "SELECT DISTINCT e.offense_type_id AS id "
				+ "FROM EVENTS e "
				+ "WHERE e.offense_category_id=? AND MONTH(e.reported_date) =? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1,categoria);
			st.setInt(2, mese);
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("id"));
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> listAllAdiacenza(String categoria, Integer mese){
		String sql = "SELECT e1.offense_type_id AS t1, e2.offense_type_id AS t2, COUNT(DISTINCT e1.neighborhood_id) AS peso "
				+ "FROM EVENTS e1, EVENTS e2 "
				+ "WHERE e1.neighborhood_id=e2.neighborhood_id AND e1.offense_type_id> e2.offense_type_id AND e1.offense_category_id=e2.offense_category_id AND e1.offense_category_id=? "
				+ "AND MONTH(e1.reported_date)= MONTH(e2.reported_date) AND MONTH(e1.reported_date)=? "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id "
				+ "HAVING peso!=0 " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1,categoria);
			st.setInt(2, mese);
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String tipo1= res.getString("t1");
				String tipo2= res.getString("t2");
				Double peso= res.getDouble("peso");
				if(tipo1!= null && tipo2!=null) {
					Adiacenza a = new Adiacenza (tipo1,tipo2, peso);
					list.add(a);
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}

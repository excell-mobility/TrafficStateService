package com.excellmobility.trafficstate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "TrafficState Service")
public class TrafficStateService
{
  @Value("${dataplatform.dburl}")
  private String dbUrl;
  @Value("${dataplatform.dbuser}")
  private String dbUser;
  @Value("${dataplatform.dbpassword}")
  private String dbPassword;
  
  
  @RequestMapping(value = { "/TrafficStateService/update/{sequenceId}" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
  @ApiOperation(value = "Update the sequence of edge_reckords from the raw-db", produces = "application/json")
  public void update(@PathVariable int sequenceId)
  {
    System.out.println(dbUrl);
    System.out.println("UPDATE " + sequenceId + " (" + loadAndWriteLos(sequenceId) + " edges)");
  }

  @RequestMapping(value = { "/TrafficStateService/clear" }, method = { org.springframework.web.bind.annotation.RequestMethod.PUT })
  @ApiOperation(value = "Clear all LOS-Stats", produces = "application/json")
  public void clear()
  {
    Connection con = null;
    Statement st = null;

    try
    {
      con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
      st = con.createStatement();
      st.executeUpdate("DELETE FROM proc.edges_los");

    } catch (SQLException ex)
    {
      ex.printStackTrace();

    } finally
    {
      try
      {
        if (st != null) st.close();
        if (con != null) con.close();
      } catch (SQLException ex)
      {}
    }
  }

  private static int getLos(double speed)
  {
    if (speed < 0) return 0;
    if (speed < 3) return 6;
    if (speed < 5) return 5;
    if (speed < 8) return 4;
    if (speed < 11) return 3;
    if (speed < 14) return 2;
    return 1;
  }

  public int loadAndWriteLos(int sequenceId)
  {
    Connection con = null;
    Statement stR = null;
    PreparedStatement stU = null;
    ResultSet rs = null;
    int n = 0;
    try
    {
      con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
      con.setAutoCommit(false);
      stR = con.createStatement();
      rs = stR.executeQuery("SELECT * FROM raw.edge_records WHERE record_sequence=" + sequenceId + " ORDER BY entertime");
      stU = con.prepareStatement("INSERT INTO proc.edges_los (sid,did,reverse,los_class,time) VALUES (?,?,?,?,extract(epoch from now())) ON CONFLICT (sid,reverse) DO UPDATE SET los_class = ?, time = extract(epoch from now())");

      double speed = 13;
      while (rs.next())
      {
        long enterTime = rs.getLong("entertime");
        long exitTime = rs.getLong("exittime");
        double length = rs.getDouble("length");
        if (length > 1 && enterTime > 0 && exitTime > enterTime && (length * 1000 /(exitTime - enterTime) < 70))
        {
          if (length < 80) speed = (length * 1000 / (exitTime - enterTime)) * 0.6 + speed * 0.4;
          else speed = length * 1000 / (exitTime - enterTime);
          stU.setString(1, rs.getString("sid"));
          stU.setString(2, rs.getString("did"));
          stU.setBoolean(3, rs.getBoolean("reverse"));
          stU.setInt(4, getLos(speed));
          stU.setInt(5, getLos(speed));
          stU.addBatch();
          n++;
        }
      }
      stU.executeBatch();
      con.commit();

    } catch (SQLException ex)
    {
      ex.printStackTrace();

    } finally
    {
      try
      {
        if (rs != null) rs.close();
        if (stR != null) stR.close();
        if (stU != null) stU.close();
        if (con != null) con.close();
      } catch (SQLException ex)
      {}
    }
    return n;
  }
}

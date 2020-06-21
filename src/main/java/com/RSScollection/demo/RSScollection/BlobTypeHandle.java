package com.RSScollection.demo.RSScollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.tool.schema.extract.spi.ExtractionContext.DatabaseObjectAccess;

public class BlobTypeHandle extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        ByteArrayInputStream bis;
        int length = 0;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(parameter);
            
            byte[] tmp = baos.toByteArray();
            bis = new ByteArrayInputStream(tmp);
            length = tmp.length;

            ps.setBinaryStream(i, bis, length);	
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding error");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = (Blob) rs.getBlob(columnName);
        byte[] returnValue = null;
        List<String> res = new ArrayList<String>();

        if (null != blob){
            returnValue = blob.getBytes(1, (int) blob.length());
            try{
                ByteArrayInputStream bais = new ByteArrayInputStream(returnValue);
                ObjectInputStream ois = new ObjectInputStream(bais);
                System.out.println("ois print is " + ois);
                res = (List<String>) ois.readObject();
            }catch (UnsupportedEncodingException e) {  
                throw new RuntimeException("Blob Decoding Error!");  
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

}